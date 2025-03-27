package handComparison;

import java.util.Arrays;
import java.util.Collections;

import deckOfCards.Card;

public class HandComparisonStatics {
	//Checks to see if a straight draw is present(only used for flop and turn).
	public static int straightDraw(Card[] hand) {
		Card[] copy = new Card[hand.length + 1];
		int count = 0;
		for (int x = 0; x <= 12; x++) {
			for (int a = 0; a < hand.length; a++) {
				copy[a] = hand[a];
			}
			copy[hand.length] = new Card((x*4));
			Collections.sort(Arrays.asList(copy));
			double st = straightCheck(copy);
			if (st != -1) {
				count++;
			}
		}
		return count;
	}
	//Checks to see if a flush draw is present(only used for flop and turn)
	public static boolean flushDraw(Card[] hand) {
		int count = 0; 
		for (int x = hand.length - 1; x >= 4; x--) {
			for (int a = x - 1; a >= 0; a--) {
				if (hand[a].suitEquals(hand[x])) {
					count++;
				}
			}
			if (count >= 3) {
				for (int z = hand.length - 1; z >=0; z--) {
				}
				return true;
			}
			count = 0;
		}
		return false;
	}
	//Sets power if no higher hand is present
	public static double highCardPower(Card[] hand) {
		double num = 10000000000D;
		num += hand[hand.length - 1].getValue() * 100000000;
		num += hand[hand.length - 2].getValue() * 1000000;
		num += hand[hand.length - 3].getValue() * 10000;
		num += hand[hand.length - 4].getValue() * 100;
		num += hand[hand.length - 5].getValue();
		return num;
	}
	
	//Pair check
	public static double pairCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		for (int x = hand.length - 1; x > 0; x--) {
			if (hand[x].valueEquals(hand[x-1])) {
				madeHand[0] = hand[x];
				madeHand[1] = hand[x-1];
				if (x == hand.length - 1) {
					madeHand[2] = hand[x-2];
					madeHand[3] = hand[x-3];
					madeHand[4] = hand[x-4];
				}
				else if (x == hand.length - 2) {
					madeHand[2] = hand[hand.length - 1];
					madeHand[3] = hand[x-2];
					madeHand[4] = hand[x-3];
				}
				else if (x == hand.length - 3) {
					madeHand[2] = hand[hand.length - 1];
					madeHand[3] = hand[hand.length - 2];
					madeHand[4] = hand[x-2];
				}
				else {
					madeHand[2] = hand[hand.length - 1];
					madeHand[3] = hand[hand.length - 2];
					madeHand[4] = hand[hand.length - 3];
				}
				
				return 20000000000D + (madeHand[0].getValue() * 100000000) + 
						(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
						10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
			}
		}
		return -1;
	}

	//Two Pair Check
	public static double twoPairCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		int pairs = 0;
		for (int x = hand.length - 1; x > 0; x--) {
			if (hand[x].valueEquals(hand[x-1]) && pairs == 1) {
				madeHand[2] = hand[x];
				madeHand[3] = hand[x-1];
				if (madeHand[4].valueEquals(hand[x])) {
					madeHand[4] = hand[x-2];
				}
				return 30000000000D + (madeHand[0].getValue() * 100000000) + 
						(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
						10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
			}
			if (hand[x].valueEquals(hand[x-1])) {
				madeHand[0] = hand[x];
				madeHand[1] = hand[x-1];
				if (x != hand.length - 1) {
					madeHand[4] = hand[hand.length - 1];
				}
				else {
					madeHand[4] = hand[x-2];
				}
				pairs = 1;
			}
		}
		return -1;
	}

	//Three of a Kind Check
	public static double threeOfAKindCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		for (int x = hand.length - 1; x > 1; x--) {
			if (hand[x].valueEquals(hand[x-2])) {
				madeHand[0] = hand[x];
				madeHand[1] = hand[x-1];
				madeHand[2] = hand[x-2];
				if (x < hand.length - 2) {
					madeHand[3] = hand[hand.length - 1];
					madeHand[4] = hand[hand.length - 2];
				}
				else if (x == hand.length - 2) {
					madeHand[3] = hand[hand.length - 1];
					madeHand[4] = hand[x - 3];
				}
				else if (x == hand.length - 1){
					madeHand[3] = hand[x-3];
					madeHand[4] = hand[x-4];
				}
				return 40000000000D + (madeHand[0].getValue() * 100000000) + 
						(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
						10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
			}
		}
		return -1;
	}

	//Straight Check
	public static double straightCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		int num = 0;
		for (int x = hand.length - 1; x > 0; x--) {
			if (hand[x].isAdjacentTo(hand[x-1])) {
				madeHand[num] = hand[x];
				num ++;
			}
			if (!(hand[x].isAdjacentTo(hand[x-1])) &&
					!(hand[x].valueEquals(hand[x-1]))) {
				num = 0;
			}
			if (num == 4) {
				madeHand[4] = hand[x-1];
				return 50000000000D + (madeHand[0].getValue() * 100000000) + 
						(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
						10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
			}
		}
		
		num = 0;
		
		aceLow(hand);
		Collections.sort(Arrays.asList(hand));
		for (int x = hand.length - 1; x > 0; x--) {
			if (hand[x].isAdjacentTo(hand[x-1])) {
				madeHand[num] = hand[x];
				num ++;
			}
			if (!(hand[x].isAdjacentTo(hand[x-1])) &&
					!(hand[x].valueEquals(hand[x-1]))) {
				num = 0;
			}
			if (num == 4) {
				madeHand[4] = hand[x-1];
				aceHigh(hand);
				Collections.sort(Arrays.asList(hand));
				return 50000000000D + (madeHand[0].getValue() * 100000000) + 
						(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
						10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
			}
		}
		aceHigh(hand);
		Collections.sort(Arrays.asList(hand));
		num = 0;
		return -1;
	}
	
	//Flush Check
	public static double flushCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		int count = 0;
		for (int a = hand.length - 1; a >= 4; a--) {
			int z = a;
			for (int x = a - 1; x >= 0; x--) {
				if (hand[a].suitEquals(hand[x])) {
					count++;
					madeHand[count] = hand[x];
				}
				if (count >= 4) {
					madeHand[0] = hand[a];
					return 60000000000D + (madeHand[0].getValue() * 100000000) + 
							(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
									10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
				}
			}
			a = z;
			count = 0;
		}
		return -1;
	}

	//Full House Check
	public static double fullHouseCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		boolean trips = false;
		int tripsPosition = -1;
		for (int x = hand.length - 1; x > 1; x--) {
			if (hand[x].valueEquals(hand[x-2])) {
				trips = true;
				tripsPosition = x;
				madeHand[0] = hand[x];
				madeHand[1] = hand[x-1];
				madeHand[2] = hand[x-2];
			}
		}
		if (trips) {
			for (int x = hand.length - 1; x > 0; x--) {
				if (hand[x].valueEquals(hand[x-1]) && x != tripsPosition
						&& x != tripsPosition - 1) {
					madeHand[3] = hand[x];
					madeHand[4] = hand[x-1];
					return 70000000000D + (madeHand[0].getValue() * 100000000) + 
							(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
									10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
				}
			}
		}
		return -1;
	}
	
	//Four of a Kind Check
	public static double fourOfAKindCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		for (int x = hand.length - 1; x > 2; x--) {
			if (hand[x].valueEquals(hand[x-3])) {	
				madeHand[0] = hand[x];
				madeHand[1] = hand[x-1];
				madeHand[2] = hand[x-2];
				madeHand[3] = hand[x-3];
				if (x < hand.length - 1) {
					madeHand[4] = hand[hand.length - 1];
				}
				else {
					madeHand[4] = hand[x-4];
				}
				return 80000000000D + (madeHand[0].getValue() * 100000000) + 
						(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
						10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
			}
		}
		return -1;
	}
	
	//Straight Flush Check
	public static double straightFlushCheck(Card[] hand) {
		Card[] madeHand = new Card[5];
		int count = 0;
		for (int a = hand.length - 1; a >= 4; a--) {
			int z = a;
			for (int x = a - 1; x >= 0; x--) {
				if (hand[a].suitEquals(hand[x]) && 
						hand[a].isAdjacentTo(hand[x])) {
					madeHand[count] = hand[a];
					count++;
					if (count == 4) {
						madeHand[4] = hand[x];
						return 90000000000D + (madeHand[0].getValue() * 100000000) + 
								(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
										10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
					}
					a = x;
				}
			}
			a = z;
			count = 0;
		}
		
		aceLow(hand);
		Collections.sort(Arrays.asList(hand));
		count = 0;
		for (int a = hand.length - 1; a >= 4; a--) {
			int z = a;
			for (int x = a - 1; x >= 0; x--) {
				if (hand[a].suitEquals(hand[x]) && 
						hand[a].isAdjacentTo(hand[x])) {
					madeHand[count] = hand[a];
					count++;
					if (count == 4) {
						madeHand[4] = hand[x];
						aceHigh(hand);
						Collections.sort(Arrays.asList(hand));
						return 90000000000D + (madeHand[0].getValue() * 100000000) + 
								(madeHand[1].getValue() * 1000000) + (madeHand[2].getValue() * 
										10000) + (madeHand[3].getValue() * 100) + madeHand[4].getValue();
					}
					a = x;
				}
			}
			a = z;
			count = 0;
		}
		aceHigh(hand);
		Collections.sort(Arrays.asList(hand));
		return -1;
	}
	
	//Changes all aces in the hand to low value
	public static void aceLow(Card[] hand) {
		for (int x = 0; x < hand.length; x++) {
			if (hand[x].getValue() == 14)
				hand[x].aceLow();
		}
	}
	
	//Changes all aces in the hand to High value 
	public static void aceHigh(Card[] hand) {
		for (int x = 0; x < hand.length; x++) {
			if (hand[x].getValue() == 1)
				hand[x].aceHigh();
		}
	}

}


