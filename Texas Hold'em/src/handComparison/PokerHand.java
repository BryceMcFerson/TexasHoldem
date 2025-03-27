package handComparison;

import java.util.Arrays;
import java.util.Collections;

import deckOfCards.Card;
import deckOfCards.Deck;

public class PokerHand implements Comparable<PokerHand>{

	//Declaration of Instance Variables
	private Card[] riverHand = new Card[7];
	private PreFlopHand preFlopHand;
	private Card[] flop = new Card[3];
	private Card turn;
	private Card river;
	private Card[] board = new Card[5];
	private static int Counter = 0;
	private StringBuffer finalHandName = new StringBuffer("N/A");
	private int preFlopStrength;
	private double riverPower;
	private double flopPower;
	private double turnPower;
	
	//Creates playerHand based on deck and how many other
		//pokerHands have been generated
	public PokerHand(Deck deck) {
		
		//Instantiation of pokerHand instance variable
		for (int x = 0; x < 5; x++) {	
			riverHand[x] = deck.getCard(x);
		}
		riverHand[5] = deck.getCard(Counter + 5);
		riverHand[6] = deck.getCard(Counter + 6);
		preFlopHand = new PreFlopHand(riverHand[5], riverHand[6]);
		flop[0] = riverHand[0];
		flop[1] = riverHand[1];
		flop[2] = riverHand[2];
		turn = riverHand[3];
		river = riverHand[4];
		board[0] = flop[0];
		board[1] = flop[1];
		board[2] = flop[2];
		board[3] = turn;
		board [4] = river;
		
		Card[] turnHand = new Card[] {riverHand[6], riverHand[5], riverHand[0], 
				riverHand[1], riverHand[2], riverHand[3]};
		Card[] flopHand = new Card[] {riverHand[6], riverHand[5], riverHand[0], 
				riverHand[1], riverHand[2]};
		
		//Sorting hand in ascending order based on card index
		Collections.sort(Arrays.asList(riverHand));
		Collections.sort(Arrays.asList(turnHand));
		Collections.sort(Arrays.asList(flopHand));
	
		//Increment count
		Counter += 2;
		
		//Determine hand power
		preFlopStrength = preFlopHand.getStrength();
		flopPower = powerCheck(flopHand);
		turnPower = powerCheck(turnHand);
		riverPower = powerCheck(riverHand);
		
	}
	//Custom Constructor for CPU class use. 
	public PokerHand(Card[] board, PreFlopHand hand) {
		this.board = board;
		this.preFlopHand = hand;
		riverHand[0] = board[0];
		riverHand[1] = board[1];
		riverHand[2] = board[2];
		riverHand[3] = board[3];
		riverHand[4] = board[4];
		riverHand[5] = hand.getHand()[0];
		riverHand[6] = hand.getHand()[1];
		Card[] turnHand = new Card[] {riverHand[0], riverHand[1], 
				riverHand[2], riverHand[3], riverHand[5], riverHand[6]};
		Card[] flopHand = new Card[] {riverHand[0], riverHand[1], 
				riverHand[2], riverHand[5], riverHand[6]};
		Collections.sort(Arrays.asList(riverHand));
		Collections.sort(Arrays.asList(turnHand));
		Collections.sort(Arrays.asList(flopHand));
		flopPower = powerCheck(flopHand);
		turnPower = powerCheck(turnHand);
		riverPower = powerCheck(riverHand);
		
	}
	
	public double getFlopPower() {
		return flopPower;
	}
	public double getTurnPower() {
		return turnPower;
	}
	
	//Creates string of card names.	
	public String toString() {
		StringBuffer hand = new StringBuffer();
		for (int x = 6; x >= 0; x--) {
			hand.append(riverHand[x].toString());
			if (x != 0)
				hand.append("\n");
		}
		return hand.toString();
	}

	//Resets Counter static variable between hands. 
	public static void resetPlayerCounter() {
		Counter = 0;
	}
	
	public String preFlopAbbreviated() {
		return preFlopHand.abbreviatedName();
	}
	
	public int getPreFlopStrength() {
		return preFlopStrength;
	}
	
	public double getRiverPower() {
		return riverPower;
	}
	
	public PreFlopHand getPreFlopHand() {
		return preFlopHand;
	}
	
	public Card[] getBoard() {
		return board;
	}
	
	//Sets the primary hand power
 	private double powerCheck(Card[] hand) {

		double power = HandComparisonStatics.straightFlushCheck(hand);
		finalHandName.replace(0, finalHandName.length(), "Straight Flush");
		if (power < 0) {
			power = HandComparisonStatics.fourOfAKindCheck(hand);
			finalHandName.replace(0, finalHandName.length(), "Four of a Kind");
		}
		if (power < 0) {
			power = HandComparisonStatics.fullHouseCheck(hand);
			finalHandName.replace(0, finalHandName.length(), "Full House");
		}
		if (power < 0) {
			power = HandComparisonStatics.flushCheck(hand);
			finalHandName.replace(0, finalHandName.length(), "Flush");
		}
		if (power < 0) {
			power = HandComparisonStatics.straightCheck(hand);
			finalHandName.replace(0, finalHandName.length(), "Straight");
		}
		if (power < 0) {
			power = HandComparisonStatics.threeOfAKindCheck(hand);
			finalHandName.replace(0, finalHandName.length(), "Three of a Kind");
		}
		if (power < 0) {
			power = HandComparisonStatics.twoPairCheck(hand);
			finalHandName.replace(0, finalHandName.length(), "Two Pair");
		}
		if (power < 0) {
			power = HandComparisonStatics.pairCheck(hand);
			finalHandName.replace(0, finalHandName.length(), "One Pair");
		}
		if (power < 0) {
			power = HandComparisonStatics.highCardPower(hand);
			finalHandName.replace(0, finalHandName.length(), "High Card");
		}
		
		if (hand.length == 5) {
			if (power < 60000000000D) {
				if (HandComparisonStatics.flushDraw(hand)) {
					power += (60000000000D - power) * .387;
				}
				if (HandComparisonStatics.straightDraw(hand) == 2) {
					power += ((50000000000D) - power) * .315;
				}
				if (HandComparisonStatics.straightDraw(hand) == 1) {
					power += ((50000000000D) - power) * .1575;
				}
			}
			else if (power < 50000000000D) {
				if (HandComparisonStatics.straightDraw(hand) == 2) {
					power += ((50000000000D) - power) * .315;
				}
				if (HandComparisonStatics.straightDraw(hand) == 1) {
					power += ((50000000000D) - power) * .1575;
				}
			}
		}
		else if (hand.length == 6) {
			int straightDraw = HandComparisonStatics.straightDraw(hand);
			if (power < 60000000000D) {
				if (HandComparisonStatics.flushDraw(hand)) {
					power += (60000000000D - power) * .197;
				}
				if (straightDraw == 2) {
					power += ((50000000000D) - power) * .174;
				}
				if (straightDraw == 1) {
					power += ((50000000000D) - power) * .0852;
				}
			}
			else if (power < 50000000000D) {
				if (straightDraw == 2) {
					power += ((50000000000D) - power) * .174;
				}
				if (straightDraw == 1) {
					power += ((50000000000D) - power) * .0852;
				}
			}
		}
		
		return power;
		
	}

	@Override
	public int compareTo(PokerHand hand) {
		if (this.riverPower == hand.riverPower) {
			return 0;
		}
		if (this.riverPower > hand.riverPower) {
			return 1;
		}
		else {
			return -1;
		}
	}

}
