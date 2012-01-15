package bots;

import core.Action;
import core.Botpit;

public class CallBot implements IBot {

	private Botpit gi;
	private int seat;
	private String name;
	private String c1;
	private String c2;

	public CallBot(Botpit gi, int seat, String name) {
		this.gi = gi;
		this.seat = seat;
		this.name = name;
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
	public void new_hand() {
		// TODO Auto-generated method stub

	}
	@Override
	public void game_start() {
	}
	@Override
	public void hole_cards(String c1, String c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	@Override
	public String[] get_cards() {
		return new String[]{c1, c2};
	}
	@Override
	public Action get_action() {
		return Action.call(gi);
	}
	@Override
	public void player_action(Action ac) {
		// TODO Auto-generated method stub
		
	}

}
