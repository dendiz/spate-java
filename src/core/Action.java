package core;

public class Action {
	static int BIGBLIND = 0;
	static int SMALLBLIND = 1;
	static int FOLD = 2;
	static int CHECK = 3;
	static int CALL = 4;
	static int BET = 5;
	static int RAISE = 6;
	static int WAITING = 7;
	
	public int type;
	public float tocall;
	public float amount;
	
	public Action(int type,float tocall,float amount) {
		this.type = type;
		this.tocall = tocall;
		this.amount = amount;
	}
	public static String get_string(int type) {
		String[] types = new String[]{"bigblind","smallblind","fold","check","call","bet","raise","waiting"};
		return types[type];
	}
	
	public static Action bigblind(float topost) {
		return new Action(BIGBLIND, 0, topost);
	}
	
	public static Action smallblind(float topost) {
		return new Action(SMALLBLIND, 0, topost);
	}
	
	public static Action fold(Botpit gi) {
		return new Action(FOLD, gi.get_to_call(gi.current_player), 0);
	}
	
	public static Action check() {
		return new Action(CHECK, 0, 0);
	}
	
	public static Action check_or_fold(Botpit gi) {
		if (gi.get_to_call(gi.current_player) == 0) return Action.check();
		return new Action(FOLD, gi.get_to_call(gi.current_player), 0);
	}
	
	public static Action call(Botpit gi) {
		if (gi.get_to_call(gi.current_player) == 0) return Action.check();
		return new Action(CALL, gi.get_to_call(gi.current_player), 0);
	}
	
	public static Action waiting() {
		return new Action(WAITING, 0, 0);
	}
	
	public static Action bet(Botpit gi) {
		return new Action(BET, 0, gi.get_bb_size());
	}
	
	public static Action raise(Botpit gi) {
		int cp = gi.current_player;
		if (gi.get_min_raise(cp) == 0) return Action.call(gi);
		if (gi.get_to_call(cp) == 0) return Action.bet(gi);
		return new Action(RAISE, gi.get_to_call(cp), gi.get_min_raise(cp));
	}	
	
	
}
