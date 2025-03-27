package players;

import java.util.Arrays;
import java.util.Collections;

import deckOfCards.Card;
import gameManager.GameManager;
import handComparison.PokerHand;
import handComparison.PreFlopHand;

public class CPU extends Player {

	private static int CPUNum = 1;
	private int aggressiveness;
	private double percentHandsBeatingFlop;
	private double percentHandsBeatingTurn;
	private double percentHandsBeatingRiver;
	private boolean bluff = false;
	
	
	public CPU(int stackSize) {
		super("CPU" + CPUNum, stackSize);
		CPUNum++;
	}
	@Override
	public void assignHand(PokerHand hand) {
		super.hand = hand;
		super.preFlopHand = hand.getPreFlopHand();
		super.board = hand.getBoard();
		getStats();
	}
	
	public int decide() {
		
		double random = Math.random();
		if (allIn || folded) {
			return -1;
		}
		else if (this.chipsContributed < Player.highestBet && random >= 0 && random < 0.2) {
			return 0;
		}
		else if (this.chipsContributed == Player.highestBet && random >= 0 && random < 0.7) {
			return 1;
		}
		else if (this.chipsContributed < Player.highestBet && random >= .2 && random < 0.85) {
			return 2;
		}
		else if (random < 1 && this.chipsContributed + this.betStack.getChipSum() + this.chipStack >= Player.highestBet) {
			return (int)(Player.highestBet*random*2);
		}
		else {
			return 3;
		}
	}
	
	public void setAggressiveness() {
		aggressiveness = (int)(Math.random()*10) + 5;
	}
	
	public void setBluff() {
		if ((int)(Math.random()*100) <= aggressiveness) {
			bluff = true;
		}
	}
	
	public void getStats() {
		int count = 0;
		Double[] flopPowers = new Double[1225];
		Double[] turnPowers = new Double[1225];
		Double[] riverPowers = new Double[1225];
		for (int x = 0; x < 51; x++) {
			for (int y = x + 1; y < 52; y++) {
				Card cx = new Card(x);
				Card cy = new Card(y);
				if (!(cx.equals(cy)) && !(cx.equals(super.preFlopHand.getHand()[0])) &&
						!(cx.equals(super.preFlopHand.getHand()[1])) && !(cy.equals(super.preFlopHand.getHand()[0]))
						&& !(cy.equals(super.preFlopHand.getHand()[1]))) {
					flopPowers[count] = (new PokerHand(board, new PreFlopHand(cx, cy)).getFlopPower());
					turnPowers[count] = (new PokerHand(board, new PreFlopHand(cx, cy)).getTurnPower());
					riverPowers[count] = (new PokerHand(board, new PreFlopHand(cx, cy)).getRiverPower());
					count++;
					
				}
			}
		}
		Arrays.sort(flopPowers);
		Arrays.sort(turnPowers);
		Arrays.sort(riverPowers);
		percentHandsBeatingFlop = percentHandsLosingTo(flopPowers, super.hand.getFlopPower());
		percentHandsBeatingTurn = percentHandsLosingTo(flopPowers, super.hand.getTurnPower());
		percentHandsBeatingRiver = percentHandsLosingTo(flopPowers, super.hand.getRiverPower());
	}
	
	private double percentHandsLosingTo(Double[] array, double power) {
		double count = 0;
		for (int x = 0; x < array.length; x++) {
			if (array[x] >= power) {
				count = x;
				x = array.length;
			}
		}
		return count/(double)array.length;
	}
	
}
