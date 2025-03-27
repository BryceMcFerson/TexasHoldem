package tests;

import deckOfCards.Deck;
import gameManager.GameManager;
import handComparison.PokerHand;
import handComparison.PreFlopHand;
import players.CPU;
import players.Player;

public class Test {

	public static void main(String[] args) {
		
		Deck deck = new Deck();
//		deck.printDeck();
//		System.out.println();
//		System.out.println("-----------------");
//		System.out.println();
		deck.shuffle();
//		deck.printDeck();
		
//		PokerHand hand = new PokerHand(deck);
//		hand.printFinalHandName();
//		System.out.println("---------------");
//		System.out.println("PreFlop Hand: \n");
//		
//		hand.printPreFlopHand();
//		System.out.println("---------------");
//		System.out.println("Flop: \n");
//		hand.printFlop();
//		System.out.println("---------------");
//		System.out.println("Flop Power: ");
//		hand.printFlopPower();
//		System.out.println("---------------");
//		System.out.println("Turn: \n");
//		hand.printTurn();
//		System.out.println("---------------");
//		System.out.println("Turn Power: ");
//		hand.printTurnPower();
//		System.out.println("---------------");
//		System.out.println("River: \n");
//		hand.printRiver();
//		System.out.println("---------------");
//		System.out.println("River Power: ");
//		hand.printRiverPower();		
//		hand.printPreFlopStrength();
		
//		GameManager gameManager = new GameManager(4, "Bryce", 101, deck);
//		gameManager.playerOrder[1].recieveChips(100);
//		gameManager.playerOrder[2].recieveChips(200);
//		gameManager.playerOrder[3].recieveChips(300);
//		gameManager.deal();
//		
//		System.out.println(gameManager.playerOrder[0].getName() + "'s Stack Size: " + gameManager.playerOrder[0].getStackSize());
//		System.out.println(gameManager.playerOrder[1].getName() + "'s Stack Size: " + gameManager.playerOrder[1].getStackSize());
//		System.out.println(gameManager.playerOrder[2].getName() + "'s Stack Size: " + gameManager.playerOrder[2].getStackSize());
//		System.out.println(gameManager.playerOrder[3].getName() + "'s Stack Size: " + gameManager.playerOrder[3].getStackSize());
//		
//		gameManager.playerOrder[0].bet1(100);
//		gameManager.playerOrder[1].bet1(100);
//		gameManager.playerOrder[2].bet1(100);
//		gameManager.playerOrder[3].bet1(100);
//		gameManager.playerOrder[1].fold();
//		gameManager.playerOrder[2].fold();
//		System.out.println("Pot: " + GameManager.pot);
//		for (int x = 0; x < gameManager.playerOrder.length; x++) {
//			gameManager.playerOrder[x].getBetStack().toPot();
//		}
//		gameManager.payOut();
//		System.out.println("Pot: " + GameManager.pot);
//		System.out.println(gameManager.playerOrder[0].getName() + "'s Stack Size: " + gameManager.playerOrder[0].getBetStack().getChipSum());
//		System.out.println(gameManager.playerOrder[1].getName() + "'s Stack Size: " + gameManager.playerOrder[1].getBetStack().getChipSum());
//		System.out.println(gameManager.playerOrder[2].getName() + "'s Stack Size: " + gameManager.playerOrder[2].getBetStack().getChipSum());
//		System.out.println(gameManager.playerOrder[3].getName() + "'s Stack Size: " + gameManager.playerOrder[3].getBetStack().getChipSum());
//		
//		System.out.println(gameManager.playerOrder[0].getHand().getRiverPower());
//		System.out.println(gameManager.playerOrder[1].getHand().getRiverPower());
//		System.out.println(gameManager.playerOrder[2].getHand().getRiverPower());
//		System.out.println(gameManager.playerOrder[3].getHand().getRiverPower());
//		CPU cpu = new CPU(100);
//		PokerHand cpuHand = new PokerHand(deck);
//		cpu.assignHand(cpuHand);
//		System.out.println(Math.random());
//		System.out.println(Math.random());
		

	}

}
