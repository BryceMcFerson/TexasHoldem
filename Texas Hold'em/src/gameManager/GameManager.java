package gameManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import deckOfCards.Card;
import deckOfCards.Deck;
import handComparison.PokerHand;
import players.CPU;
import players.Player;
import players.TablePositions;
import players.User;

public class GameManager {
	
	public static int pot = 0;
	public ArrayList<Player> playerOrder;
	public Player[] playerRank;
	public ArrayList<Player> nonFoldedPlayerRank;
	public Comparator<Player> stackComp;
	public Card[] communityCards;
	private Deck deck;
	public static int smallBlind = 1;
	public static int bigBlind = 2;
	public static boolean gameOver = false;
	
	public GameManager(int playerNum, String userName, int startingStack, Deck deck) {
		if (playerNum < 2 || playerNum > 9) {
			throw new IllegalArgumentException("Invalid Player Number.");
		}
		
		if (startingStack <= 500) {
			smallBlind = 1;
		}
		else if (startingStack > 500 && startingStack <= 2500) {
			smallBlind = 2;
		}
		else if (startingStack > 2500 && startingStack <= 5000) {
			smallBlind = 5;
		}
		else if (startingStack > 5000 && startingStack <= 10000) {
			smallBlind = 10;
		}
		else {
			smallBlind = 25;
		}
		bigBlind = smallBlind*2;
		
		playerOrder = new ArrayList<Player>();
		playerOrder.add(new User(userName, startingStack));
		for (int x = 1; x < playerNum; x++) {
			playerOrder.add(new CPU(startingStack));
		}
		playerRank = new Player[playerNum];
		for (int x = 0; x < playerRank.length; x++) {
			playerRank[x] = playerOrder.get(x);
		}
		nonFoldedPlayerRank = new ArrayList<Player>(playerRank.length);
		this.deck = deck;
		this.deck.shuffle();
	}
	
	
	
	public static int getPot() {
		return pot;
	}
	
	public static void addToPot(int a) {
		pot+= a;
	}
	
	public void deal() {
		PokerHand.resetPlayerCounter();
		deck.shuffle();
		for (int x = 0; x < playerOrder.size(); x++) {
			playerOrder.get(x).assignHand(new PokerHand(deck));
			if (playerOrder.get(x) instanceof CPU) {
				((CPU)playerOrder.get(x)).setAggressiveness();
			}
		}
		Collections.sort(Arrays.asList(playerRank));
		for (int x = 0; x < playerRank.length; x++) {
			nonFoldedPlayerRank.add(playerRank[x]);
		}
		this.communityCards = playerOrder.get(0).getHand().getBoard();
		System.gc();
		
	}
	
	public void payOut() {
		//Sets array of ranked non-folded players in order
		for (int x = 0; x < nonFoldedPlayerRank.size(); x++) {
			if (nonFoldedPlayerRank.get(x).isFolded()) {
				nonFoldedPlayerRank.remove(x);
				x--;
			}
		}
		//for each non-folded player(starting with the highest rank), see how many ways the pot is split
		for (int x = nonFoldedPlayerRank.size()-1; x >= 0; x--) {
			int startIndex = x;
			int count = 0;
			if (x != 0) {
				//check to see if current player is the same riverpower as next player in order and process accordingly
				for (int z = x - 1; z >= 0; z--) {
					if (Double.compare(nonFoldedPlayerRank.get(x).getHand().getRiverPower(), 
						nonFoldedPlayerRank.get(z).getHand().getRiverPower()) == 0) {
						count++;
					}
				}
				payOutHelper(startIndex, startIndex - count, count + 1);
				x = startIndex - count;
			}
			else {
				payOutHelper(0, 0, 1);
			}
		}
		for (int x = 0; x < playerOrder.size(); x++) {
			playerOrder.get(x).isNotAllIn();
		}
	}
	
	private void payOutHelper(int startIndex, int endIndex, int count) {
		//Gives chips back to each player being payed out
		for (int x = startIndex; x >= endIndex; x--) {
			pot -= nonFoldedPlayerRank.get(x).getChipsContributed();
			nonFoldedPlayerRank.get(x).getBetStack().addChips(nonFoldedPlayerRank.get(x).getChipsContributed());
		}
		//Pays out each player in payout index up to their own chipsContributed amount if 
		//player is not one of the players being paid out. 
		int extraChips = 0;
		for (int x = startIndex; x >= endIndex; x--) {
			int highestAmountReceived = 0;
			for (int z = 0; z < playerRank.length; z++) {
				if (!playerContains(playerRank[z], nonFoldedPlayerRank, startIndex, endIndex)) {
					if (playerRank[z].getChipsContributed() < nonFoldedPlayerRank.get(x).getChipsContributed()) {
						nonFoldedPlayerRank.get(x).getBetStack().addChips(playerRank[z].getChipsContributed()/count);
						pot -= playerRank[z].getChipsContributed()/count + playerRank[z].getChipsContributed()%count;
						if (highestAmountReceived < playerRank[z].getChipsContributed()/count + playerRank[z].getChipsContributed()%count) {
							highestAmountReceived = playerRank[z].getChipsContributed()/count + playerRank[z].getChipsContributed()%count;
						}
						extraChips += playerRank[z].getChipsContributed()%count;
						playerRank[z].adjustChipsContributed(playerRank[z].getChipsContributed()/count + playerRank[z].getChipsContributed()%count);
					}
					else if (playerRank[z].getChipsContributed() >= nonFoldedPlayerRank.get(x).getChipsContributed()) {
						nonFoldedPlayerRank.get(x).getBetStack().addChips(nonFoldedPlayerRank.get(x).getChipsContributed()/count);
						pot -= nonFoldedPlayerRank.get(x).getChipsContributed()/count + nonFoldedPlayerRank.get(x).getChipsContributed()%count;
						if (highestAmountReceived < nonFoldedPlayerRank.get(x).getChipsContributed()/count + nonFoldedPlayerRank.get(x).getChipsContributed()%count) {
							highestAmountReceived = nonFoldedPlayerRank.get(x).getChipsContributed()/count + nonFoldedPlayerRank.get(x).getChipsContributed()%count;
						}
						extraChips += nonFoldedPlayerRank.get(x).getChipsContributed()%count;
						playerRank[z].adjustChipsContributed(nonFoldedPlayerRank.get(x).getChipsContributed()/count + 
								nonFoldedPlayerRank.get(x).getChipsContributed()%count);
					}
				}
			}
			for (int a = x-1; a >= endIndex; a--) {
				if (a != x) {
					nonFoldedPlayerRank.get(a).adjustChipsContributed(highestAmountReceived);
				}
			}
			count--;
		}
		
		for (int x = 0; extraChips != 0; x++) {
			if (playerContains(playerOrder.get(x), nonFoldedPlayerRank, startIndex, endIndex)) {
				playerOrder.get(x).getBetStack().addChips(1);
				extraChips--;
			}
			if (x == playerOrder.size() - 1) {
				x = -1;
			}
		}
		
		//Adjusts chips contributed variable, so nobody can be payed out from these players anymore
		for (int x = startIndex; x >= endIndex; x--) {
			nonFoldedPlayerRank.get(x).adjustChipsContributed(nonFoldedPlayerRank.get(x).getChipsContributed());
		}
		
	}
	
	public void nextHand() {
		for (int x = 0; x < playerOrder.size(); x++) {
			if (playerOrder.get(x).getStackSize() == 0) {
				if (playerOrder.get(x) instanceof User) {
					gameOver = true;
				}
				playerOrder.remove(x);
				x--;
			}
		}
		Player player = playerOrder.get(0);
		for (int x = 0; x < playerOrder.size()-1; x++) {
			playerOrder.set(x, playerOrder.get(x+1));
			playerOrder.get(x).setGamePosition(x);;
		}
		playerOrder.set(playerOrder.size()-1, player);
		playerOrder.get(playerOrder.size()-1).setGamePosition(8);
		for (int x = 0; x < playerOrder.size(); x++) {
			playerOrder.get(x).resetChipsContributed();
			playerOrder.get(x).unfold();
		}
		Player.resetHighestBet();
		nonFoldedPlayerRank.clear();
	}
	
	public Card[] getCommunityCards() {
		return communityCards;
	}
	
	private static boolean playerContains(Player target, ArrayList<Player> array, int startIndex, int endIndex) {
		for (int x = startIndex; x >= endIndex; x--) {
			if (target.equals(array.get(x))) {
				return true;
			}
		}
		return false;
	}
	
}
