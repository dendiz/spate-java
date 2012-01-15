package bots;

import core.Action;

public interface IBot {
	public int 	get_seat();
	public void new_hand();
	public String get_name();
	public void game_start();
	public void hole_cards(String c1, String c2);
	public String[] get_cards();
	public Action get_action();
	public void player_action(Action ac);
}
