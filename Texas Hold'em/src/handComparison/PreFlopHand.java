package handComparison;

//First four imports only necessary if recreating preFlop power list. 
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import deckOfCards.Deck;
import deckOfCards.Card;

public class PreFlopHand implements Comparable<PreFlopHand>{

	Card[] hand = new Card[2];
	int strength;
	
	public PreFlopHand(Card a, Card b) {
		hand[0] = a;
		hand[1] = b;
		Collections.sort(Arrays.asList(hand));
		strength();
	}
	
	//Constructor specifically for preFlop power file constructor
	public PreFlopHand(Deck deck) {
		this(deck.getCard(0), deck.getCard(1));
	}
	
	public int compareTo(PreFlopHand x) {
		return x.strength - this.strength;
	}

	public String abbreviatedName() {
		StringBuffer temp = new StringBuffer();
		if (hand[1].getValue() > 10) {
			temp.append(hand[1].toString().charAt(0));
		}
		else {
			temp.append(hand[1].getValue());
		}
		if (hand[0].getValue() > 10) {
			temp.append("-" + hand[0].toString().charAt(0));
		}
		else {
			temp.append("-" + hand[0].getValue());
		}
		if (hand[0].suitEquals(hand[1])) {
			temp.append('s');
		}
		else if (!(hand[0].equals(hand[1]))) {
			temp.append('o');
		}
		return temp.toString();
	}
	
	public String toString() {
		return hand[0].getCardName() + ", " + hand[1].getCardName();
	}
	
 	public void strength() {
		strength = (hand[0].getValue() + hand[1].getValue()) *10 + 10;
		int diff = Math.abs(hand[0].getValue()-hand[1].getValue());
		if (diff == 0) {
			strength += hand[0].getValue()*17 + 100;
		}
		if (hand[0].suitEquals(hand[1])) {
			strength += (hand[0].getValue()+hand[1].getValue()) + 25;
		}
		if (diff < 5 && diff > 0) {
			strength += Math.abs((hand[0].getValue()+hand[1].getValue() + 60)/Math.sqrt(diff));
		}
		if (hand[1].getValue() == 14) {
			strength += hand[0].getValue()*4 + 25;
			if (hand[0].getValue() < 6) {
				strength += 50;
			}
		}
		if (hand[0].getValue() + hand[1].getValue() > 19) {
			strength += 50;
		}
	}
	
 	@Override
 	public boolean equals(Object x) {
 		if (!this.abbreviatedName().equals(((PreFlopHand)x).abbreviatedName())) {
 			return false;
 		}
 		if (this.strength != ((PreFlopHand)x).strength) {
 			return false;
 		}
 		return true;
 	}
 	
 	public int getStrength() {
 		return strength;
 	}
 	
 	public Card[] getHand() {
 		return hand;
 	}
 	
 	public Card getCard(int a) {
 		return hand[a];
 	}
 	
 	//Creates List of powers for Pre Flop Hands. Only needed to run once to save powers. 
// 	public static void main(String[] args) {
// 		Deck deck = new Deck();
// 		deck.shuffle();
// 		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter("preflophandstrength.txt"));
//			writer.write("Pre Flop Hands and Associated Strength Levels Examples:\n");
//			ArrayList<PreFlopHand> hands = new ArrayList<>();
//			PreFlopHand pfHand = new PreFlopHand(deck);
//			for (int x = 0; x < 1500; x++) {
//				if (!hands.contains(pfHand)) {
//					hands.add(pfHand);
//				}
//				deck.shuffle();
//				pfHand = new PreFlopHand(deck);
//			}
//			hands.sort(null);
//			for (int x = 0; x < hands.size(); x++) {
//				writer.write(hands.get(x).getStrength() + ": " + hands.get(x).abbreviatedName() + "\n");
//			}
//			System.out.println("Total Entries: " + hands.size());
//			writer.close();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
// 	}
 	
}
