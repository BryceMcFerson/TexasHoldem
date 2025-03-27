package players;

import deckOfCards.Card;
import gameManager.GamePositions;
import handComparison.PokerHand;
import handComparison.PreFlopHand;

public class Player implements Comparable<Player> {

	protected String name;
	protected int chipStack;
	protected BetStack betStack;
	protected int chipsContributed = 0;
	protected static int highestBet = 0;
	protected static int playerNum;
	protected GamePositions gamePosition;
	protected boolean folded = false;
	protected boolean allIn = false;
	protected PokerHand hand;
	protected PreFlopHand preFlopHand;
	protected TablePositions tablePosition;
	protected Card[] board;
	
	public Player(String name, int stackSize) {
		this.name = name;
		this.chipStack = stackSize;
		this.betStack = new BetStack(0);
		this.tablePosition = TablePositions.values()[playerNum];
		betStack.setX(tablePosition.getChipX());
		betStack.setY(tablePosition.getChipY());
		playerNum ++;
	}
	
	public void assignHand(PokerHand hand) {
		this.hand = hand;
		this.preFlopHand = hand.getPreFlopHand();
		this.board = hand.getBoard();
	}
	
	public int getChipsContributed() {
		return chipsContributed;
	}
	
	public void resetChipsContributed() {
		chipsContributed = 0;
	}
	
	public static void resetHighestBet() {
		highestBet = 0;
	}
	
	public static int getHighestBet() {
		return highestBet;
	}
	
	public BetStack getBetStack() {
		return betStack;
	}
	
	public void adjustChipsContributed(int x) {
		chipsContributed -= x;
		if (chipsContributed < 0) {
			chipsContributed = 0;
		}
	}
	
	public void recieveChips(int a) {
		chipStack += a;
	}
	
	public void raise(int a) {
		if (a < chipStack) {
			bet1(a);
		}
		else {
			goAllIn();
		}
	}
	
	public void bet1(int a) {
		if (folded) {
			throw new IllegalArgumentException("Cannot bet after you have Folded.");
		}
		if (allIn) {
			throw new IllegalArgumentException("Cannot bet once player is all in.");
		}
		if (a < chipStack && a != 0) {
			chipStack -= a;
			chipsContributed += a;
			if (chipsContributed > highestBet) {
				highestBet = chipsContributed;
			}
		}
		else if (a == chipStack && a != 0) {
			allIn = true;
			chipStack -= a;
			chipsContributed += a;
			if (chipsContributed > highestBet) {
				highestBet = chipsContributed;
			}
		}
		else if (a != 0) {
			throw new IllegalArgumentException("Bet exceeds player stack size.");
		}
	}
	
	public void bet2(int a) {
		betStack.setChipSum(betStack.getChipSum() + a);
	}
	
	public void goAllIn() {
		allIn = true;
	}
	
	public void call() {
		if (chipStack > highestBet) {
			this.bet1(highestBet - this.chipsContributed);
		}
		else {
			this.bet1(chipStack);
		}
	}
	
	public void fold() {
		folded = true;
	}
	public void unfold() {
		folded = false;
	}
	
	public boolean isFolded() {
		return folded;
	}
	public boolean isAllIn() {
		return allIn;
	}
	public void isNotAllIn() {
		allIn = false;
	}
	
	public void setGamePosition(int a) {
		gamePosition = GamePositions.values()[a];
	}
	
	public TablePositions getTablePosition() {
		return tablePosition;
	}
	
	public int getStackSize() {
		return chipStack;
	}
	
	public String getName() {
		return name;
	}
	
	public int compareTo(Player p) {
		if (hand.getRiverPower() > p.hand.getRiverPower()) {
			return 1;
		}
		else if (hand.getRiverPower() < p.hand.getRiverPower()) {
			return -1;
		}
		else  {
			return p.chipStack-chipStack;
		}
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Player)) {
			return false;
		}
		Player p = (Player)o;
		if (p.name.equals(this.name) && p.chipStack == this.chipStack && 
				p.tablePosition == this.tablePosition) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public PokerHand getHand() {
		return hand;
	}
	
	public PreFlopHand getPreFlopHand() {
		return preFlopHand;
	}
	
}
