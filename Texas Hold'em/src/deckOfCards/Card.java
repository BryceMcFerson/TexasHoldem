package deckOfCards;

import java.awt.*;

import javax.swing.ImageIcon;

import graphics.Moveable;

public class Card implements Comparable<Card>, Moveable {
	
	private int index;
	private String cardName;
	private int value;
	private String suit;
	
	private double x;
	private double y;
	private boolean revealed = false;
	private static Image cardBack = new ImageIcon("Pictures/Cards/CardBack.png").getImage();
	private Image cardFront;
	
	//Card constructor creates a card depending on index
 	public Card(int index) {
		if (index > 51 || index < 0) {
			throw new IllegalArgumentException("Deck Index must be between 0 and 51.");
		}
		this.index = index;
		this.cardName = cardName(index);
		if (value > 10) {
			this.cardFront = new ImageIcon("Pictures/Cards/" + cardName.charAt(0) + "of" + suit + ".png").getImage();
		}
		else {
			this.cardFront = new ImageIcon("Pictures/Cards/" + value + "of" + suit + ".png").getImage();
		}
		x = 676;
		y = 529;
	}
	
 	//Card copy constructor
 	public Card(Card card) {
 		this(card.index);
 	}
 	
 	//Default Constructor for GUI purposes
 	public Card(int x, int y) {
 		this.cardFront = new ImageIcon("Pictures/1.png").getImage();
 		this.x = x;
 		this.y = y;
 	}
 	
	//Card construction helper method
 	private String cardName(int index) {
		String suit = "";
		String num = "";
		if (index % 4 == 0) {
			suit = "Clubs";
		}
		if (index % 4 == 1) {
			suit = "Spades";
		}
		if (index % 4 == 2) {
			suit = "Hearts";
		}
		if (index % 4 == 3) {
			suit = "Diamonds";
		}
		
		this.suit = suit;
		
		if (index / 4 == 0) {
			num = "Two";
			this.value = 2;
		}
		if (index / 4 == 1) {
			num = "Three";
			this.value = 3;
		}
		if (index / 4 == 2) {
			num = "Four";
			this.value = 4;
		}
		if (index / 4 == 3) {
			num = "Five";
			this.value = 5;
		}
		if (index / 4 == 4) {
			num = "Six";
			this.value = 6;
		}
		if (index / 4 == 5) {
			num = "Seven";
			this.value = 7;
		}
		if (index / 4 == 6) {
			num = "Eight";
			this.value = 8;
		}
		if (index / 4 == 7) {
			num = "Nine";
			this.value = 9;
		}
		if (index / 4 == 8) {
			num = "Ten";
			this.value = 10;
		}
		if (index / 4 == 9) {
			num = "Jack";
			this.value = 11;
		}
		if (index / 4 == 10) {
			num = "Queen";
			this.value = 12;
		}
		if (index / 4 == 11) {
			num = "King";
			this.value = 13;
		}
		if (index / 4 == 12) {
			num = "Ace";
			this.value = 14;
		}
		return num + " of " + suit;
	}
	
	public String toString() {
		return this.cardName;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public String getCardName() {
		return cardName;
	}
	@Override
	public int compareTo(Card card) {
		return this.value - card.value;
	}
	
	public boolean valueEquals(Card card) {
		if (card.value == this.value) {
			return true;
		}
		return false;
	}

	//returns true if card is adjacent in value to current card
	public boolean isAdjacentTo(Card card) {
		int val = card.value - this.value;
		if (val == -1 || val == 1) 
			return true;
		
		return false;
	}	
	
	//Returns true if suits are equal
	public boolean suitEquals(Card card) {
		if (card.suit.equals(this.suit)) {
			return true;
		}
		return false;
	}
	
	public boolean equals(Card card) {
		if (this.suitEquals(card) && this.valueEquals(card)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Changes the value of the ace to be on the low end instead of the high end
	public void aceLow() {
		if (this.value == 14) {
			this.value = 1;
		}
	}
	
	//Changes the value of the ace back to the high end
	public void aceHigh() {
		if (this.value == 1) {
			this.value = 14;
		}
	}
	
	public void revealCard() {
		revealed = true;
	}
	
	public void blockCard() {
		revealed = false;
	}
	
	public Image getCardImage() {
		if (revealed) {
			return cardFront;
		}
		else {
			return cardBack;
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void increaseX(double x) {
		this.x += x;
	}
	public void increaseY(double y) {
		this.y += y;
	}
}
