package deckOfCards;

import java.util.Arrays;
import java.util.Collections;

public class Deck {

	private int[] deckIndex = new int[52];
	private Card[] deck = new Card[52];
	
	//Deck constructor creates standard 52 card deck
	public Deck() {
		for (int x = 0; x < 52; x++) {
			deckIndex[x] = x;
			deck[x] = new Card(x);
		}
	}
	//Returns card at specified index
	public Card getCard(int index) {
		return deck[index];
	}
	
	//prints the deck
	public void printDeck() {
		System.out.println(this.toString());
	}
	
	//Returns the deck as a string
	public String toString() {
		StringBuffer deck = new StringBuffer();
		for (int x = 0; x < 52; x++) {
			deck.append(this.deck[x].toString());
			if (x != 51) {
				deck.append("\n");
			}
		}
		return deck.toString();
	}
	
	//Shuffles the deck
	public void shuffle() {
		Collections.shuffle(Arrays.asList(this.deck));
	}
	//Changes the value of all aces in the deck to be low
	public void aceLow() {
		for (int x = 0; x < 52; x++) {
			if (deck[x].getValue() == 14) {
				deck[x].aceLow();
			}
		}
	}
	
	//Changes the value of all aces in the deck to be high
	public void aceHigh() {
		for (int x = 0; x < 52; x++) {
			if (deck[x].getValue() == 14) {
				deck[x].aceHigh();
			}
		}
	}
}
