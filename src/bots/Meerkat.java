package bots;

import spadeseval.evaluator.SpadesEval;
import core.Action;
import core.Botpit;
import core.Deck;

public class Meerkat implements IBot {
	private Botpit gi;
	private int seat;
	private String name;
	private String c1;
	private String c2;
	public Meerkat(Botpit gi, int seat, String name) {
		this.gi = gi;
		this.seat = seat;
		this.name = name;
	}
	@Override
	public void game_start() {
		// TODO Auto-generated method stub

	}

	@Override
	public Action get_action() {
		if (gi.game_stage == Botpit.PREFLOP) return pfAction();
		return fAction();
	}

	private Action fAction() {
		// number of players left in the hand (including us)
		int ourSeat = seat;
		// amount to call
		double toCall = gi.get_to_call(ourSeat);
		
		// immediate pot odds
		double PO = toCall / (double) (gi.pot + toCall);
		
		// compute our current hand rank
		
		//ArrayList<ArrayList<String>> pockets = new ArrayList<ArrayList<String>>();
		String[][] pockets = new String[gi.players.length][2];
		pockets[0] = get_cards();
		
		
		for (int i=0;i<gi.players.length - 1;i++) {
			pockets[i] = new String[]{"__","__"};
			
		}
		
		float HRN = 0;
		try {
			HRN = SpadesEval.ev(pockets, gi.board)[0];
		} catch (Exception e) {}
		// compute a fast approximation of our hand potential
		double PPOT = 0.0;
		if (gi.game_stage  < Botpit.RIVER) {
			try {
				PPOT = ppot1(c1, c2, gi.board);
			} catch (Exception e) {}
			//System.out.println("ppot execution time: " + (System.currentTimeMillis() - start));			
		}
		
		// here is an example of how to step through
		// all the opponents at the table:
		
//		println(" | HRn = " + Math.round(HRN * 10) / 10.0 +
//		" PPot = " + Math.round(PPOT * 10) / 10.0 +
//		" PotOdds = " + Math.round(PO * 10) / 10.0)
		
		if (HRN == 1.0) {
			// dah nuts -- raise the roof!
			return Action.raise(gi);
		}
		
		// consider checking or betting:
		if (toCall == 0) {
			if (Math.random() < HRN * HRN) {
				return Action.bet(gi); // bet a hand in proportion to it's strength
			}
			if (Math.random() < PPOT) {
				return Action.bet(gi); // semi-bluff
			}
			// just check
			return Action.check();
		} else {
			// consider folding, calling or raising:
			if (Math.random() < Math.pow(HRN, 1 + gi.raises)) {
				// raise in proportion to the strength of our hand
				return Action.raise(gi);
			}
			
			if (HRN * HRN * gi.pot > toCall || PPOT > PO) {
				// if we have draw odds or a strong enough hand to call
				return Action.call(gi);
			}
			
			return Action.check_or_fold(gi);
		}
	}
	
	private boolean is_face_card(String c) {
		String[] faces = new String[] {"T","J","Q","K","A"};
		char r = c.charAt(0);
		for (String f : faces) {
			if (f.equals(r)) return true;
		}
		return false;
	}
	private Action pfAction() {
		//def toCall = gi.getAmountToCall(seat)
		if (c1.charAt(0) == c2.charAt(0)) {
			if (is_face_card(c1)) {
				return Action.raise(gi);
			}
			return Action.call(gi);
		}
		if (is_face_card(c1)&& is_face_card(c2)) {
			if (c1.charAt(1) == c2.charAt(1)) {
				return Action.raise(gi);
			}
			return Action.call(gi);
		}
		
		if (c1.charAt(1) == c2.charAt(1)) {
			
			for (String r1 : new String[]{"T","J","Q","K","A"}) {
				for (String r2 : new String[]{"T","J","Q","K","A"}) {
					if ((r1+r2).equals(c1.charAt(0)+c1.charAt(0))) {
						return Action.call(gi);
					}
				}
			}
			
			if ((c1.charAt(0) == 'A' && c2.charAt(0) == '2') ||
			(c2.charAt(0) == 'A' && c1.charAt(0) == '2')) {
				return Action.raise(gi);
			}
			// call any suited ace
			if ((c1.charAt(0) == 'A' || c2.charAt(0)== 'A')) {
				return Action.call(gi);
			}
		}
		if (gi.get_to_call(seat) <= gi.get_bb_size()) {
			if (Math.random() < 0.05) {
				return Action.call(gi);
			}
		}
		
		// check or fold
		return Action.check_or_fold(gi);
	}
	@Override
	public String[] get_cards() {
		return new String[]{c1, c2};
	}

	@Override
	public String get_name() {
		return name;
	}

	@Override
	public int get_seat() {
		return seat;
	}

	@Override
	public void hole_cards(String c1, String c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public void new_hand() {
		// TODO Auto-generated method stub

	}

	@Override
	public void player_action(Action ac) {
		// TODO Auto-generated method stub

	}
	public double ppot1(String c1, String c2, String[] board) throws Exception{
		double[][] HP = new double[3][3];
		double[] HPTotal = new double[3];
		int ourrank7, opprank;
		int index;
		String[] cards = new String[2 + board.length];
		cards[0] = c1;
		cards[1] = c2;
		for (int i = 2;i<board.length + 2;i++) {
			cards[i] = board[i-2];
		}
		int ourrank5 = SpadesEval.rank(cards);
		
		// remove all known cards
		Deck d = new Deck();
		d.extract(new String[]{c1,c2});
		d.extract(board);
		
		// pick first opponent card
		for (int i = 0; i < d.cards.length; i++) {
			String o1 = d.cards[i];
			// pick second opponent card
			for (int j = i + 1; j < d.cards.length; j++) {
				String o2 = d.cards[j];
				cards[0] = o1;
				cards[1] = o2;
				for (int n=2;n<board.length+2;n++) {
					cards[n] = board[n-2];
				}
				
				opprank = SpadesEval.rank(cards);
				if (ourrank5 > opprank) index = AHEAD;
				else if (ourrank5 == opprank) index = TIED;
				else index = BEHIND;
				HPTotal[index]++;
				
				// tally all possiblities for next board card
				for (int k = 0; k < d.cards.length; k++) {
					if (i == k || j == k) continue;
					String bc = d.cards[k];
					cards = new String[7];
					cards[0] = c1;
					cards[1] = c2;
					for (int n=2;n<board.length+2;n++) {
						cards[n] = board[n-2];
					}
					cards[6] = bc;
					
					ourrank7 = SpadesEval.rank(cards);
					cards[0] = o1;
					cards[1] = o2;
					for (int n=2;n<board.length+2;n++) {
						cards[n] = board[n-2];
					}
					cards[6] = bc;
					opprank = SpadesEval.rank(cards);
					if (ourrank7 > opprank) HP[index][AHEAD]++;
					else if (ourrank7 == opprank) HP[index][TIED]++;
					else HP[index][BEHIND]++;
					
				}
			}
		} /* end of possible opponent hands */
		
		double ppot = 0;//, npot = 0;
		double den1 = (45 * (HPTotal[BEHIND] + (HPTotal[TIED] / 2.0)));
		if (den1 > 0) {
			ppot = (HP[BEHIND][AHEAD] + (HP[BEHIND][TIED] / 2.0) +
			(HP[TIED][AHEAD] / 2.0)) / (double) den1;
		}
		//		if (den2 > 0) {
		//			npot = (HP[AHEAD][BEHIND] + (HP[AHEAD][TIED] / 2.0) +
		//					(HP[TIED][BEHIND] / 2.0)) / (double) den2;
		//		}
		return ppot;
	}
	
	// constants used in above method:
	private final static int AHEAD = 0;
	private final static int TIED = 1;
	private final static int BEHIND = 2;
}
