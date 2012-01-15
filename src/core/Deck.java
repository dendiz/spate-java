package core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {
	public String[] cards = new String[52];
	public String[] virgin_deck = new String[] { "2c", "2h", "2s", "2d", "3c",
			"3h", "3s", "3d", "4c", "4h", "4s", "4d", "5c", "5h", "5s", "5d",
			"6c", "6h", "6s", "6d", "7c", "7h", "7s", "7d", "8c", "8h", "8s",
			"8d", "9c", "9h", "9s", "9d", "Tc", "Th", "Ts", "Td", "Jc", "Jh",
			"Js", "Jd", "Qc", "Qh", "Qs", "Qd", "Kc", "Kh", "Ks", "Kd", "Ac",
			"Ah", "As", "Ad" };
	public int current_card = 0;

	public Deck() {
		reset();
	}

	public void reset() {
		System.arraycopy(virgin_deck, 0, cards, 0, virgin_deck.length);
		current_card = 0;
	}

	public void shuffle() {
		List<String> c = Arrays.asList(cards);
		Collections.shuffle(c);
		cards = c.toArray(new String[] {});
	}

	public String deal() {
		if (current_card >= 52)
			throw new RuntimeException("The deck was finished?");
		return cards[current_card++];
	}

	public void extract(String[] hand) {
		for (int i = 0; i < cards.length; i++) {
			for (int j = 0; j < hand.length; j++) {
				if (cards[i].equals(hand[j])) {
					cards[i] = "XX";
				}
			}
		}

		String[] tcards = new String[cards.length - hand.length];
		for (int i = 0, j = 0; i < cards.length; i++) {
			if (!cards[i].equals("XX")) {
				tcards[j++] = cards[i];
			}
		}
		cards = tcards;
	}
}
