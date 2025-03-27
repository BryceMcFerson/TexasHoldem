package graphics;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import deckOfCards.Card;
import deckOfCards.Deck;
import gameManager.GameManager;
import players.BetStack;
import players.CPU;
import players.Player;
import players.User;

public class GamePanel extends JPanel implements Runnable {

	//GamePanel is the hub of the game. 
	
	private final int screenWidth = 1400;
	private final int screenHeight = 850;
	
	private Thread gameThread;
	private KeyHandler keyHandler = new KeyHandler();
	private boolean userTurn;
	private final int FPS = 120;
	private final int deckX = 670;
	private final int deckY = 535;
	private double x = deckX + 6;
	private double y = deckY - 6;
	private double originalMovementSpeed = 30;
	private double movementSpeed = originalMovementSpeed;
	private Color backGroundColor = new Color(0x2c2c2c);
	private Image chip1 = new ImageIcon("Pictures/PokerChips/PokerChip1.png").getImage();
	private Image chip5 = new ImageIcon("Pictures/PokerChips/PokerChip5.png").getImage();
	private Image chip25 = new ImageIcon("Pictures/PokerChips/PokerChip25.png").getImage();
	private Image chip100 = new ImageIcon("Pictures/PokerChips/PokerChip100.png").getImage();
	private Image chip500 = new ImageIcon("Pictures/PokerChips/PokerChip500.png").getImage();
	private Image chip2500 = new ImageIcon("Pictures/PokerChips/PokerChip2500.png").getImage();
	private GameManager gameManager;
	private Card[] communityCards;
	private BetStack tempBetStack = new BetStack(0);
	MouseHandler mouse = new MouseHandler();
	JButton callButton = new JButton("Call");
	
	public GamePanel(int players) {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(backGroundColor);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
		this.setLayout(null);
		gameManager = new GameManager(players, "Bryce", 40000, new Deck());
		communityCards = new Card[5];
		gameManager.deal();
		setCommunityCards();
		
		callButton = new JButton("Call");
		callButton.setBounds(0, 0, 100, 100);
		callButton.setForeground(Color.white);
		callButton.setBackground(Color.red);
		callButton.addActionListener(e -> {
			System.out.println("CALLING");
		});
		callButton.setVisible(true);
		this.add(callButton);
		revalidate();
		repaint();
		
		
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		while (gameThread != null) {
			
				enter(200);
			
				dealCards();
				
				bettingRound(true);
				
				enter(200);
				
				betsToPot();
				
				enter(200);
				
				putFlop();
				
				bettingRound(false);
				
				enter(200);
				
				betsToPot();
				
				putTurn();
				
				bettingRound(false);
				
				betsToPot();
				
				enter(200);
				
				putRiver();
				
				bettingRound(false);
				
				betsToPot();
				
				enter(200);
				
				revealCards();
				
				enter(200);
				
				payOut();
				//gameManager.nextHand();
				setPositions();
				gameManager.deal();
				setCommunityCards();
//				setPositions();
//				gameManager.deal();
//				setCommunityCards();
		}
	}
	
	private void bettingRound(boolean preFlop) {
		int x = 0;
		boolean newBettingRound = true;
		if (preFlop) {
			raise(gameManager.playerOrder.get(0), GameManager.smallBlind);
			raise(gameManager.playerOrder.get(1), GameManager.smallBlind);
			if (gameManager.playerOrder.size() > 2) {
				x = 2;
			}
		}
		while (!bettingRoundOver() || newBettingRound) {
			boolean actionPerformed = false;
			if (gameManager.playerOrder.get(x) instanceof CPU) {
				if (!gameManager.playerOrder.get(x).isFolded() && !gameManager.playerOrder.get(x).isAllIn()) {
					betHelper(((CPU)gameManager.playerOrder.get(x)).decide(), 
						gameManager.playerOrder.get(x));
					actionPerformed = true;
				}
			}
			else if (gameManager.playerOrder.get(x) instanceof User) {
				userTurn = true;
				while (userTurn) {
					repaint();
					if (gameManager.playerOrder.get(x).isFolded() || gameManager.playerOrder.get(x).isAllIn()) {
						userTurn = false;
					}
					else if (keyHandler.userCheck) {
						check(gameManager.playerOrder.get(x));
						actionPerformed = true;
						userTurn = false;
					}
					else if (keyHandler.userFold) {
						fold(gameManager.playerOrder.get(x));
						actionPerformed = true;
						userTurn = false;
					}
					else if (keyHandler.userCall) {
						call(gameManager.playerOrder.get(x));
						actionPerformed = true;
						userTurn = false;
					}
					else if (keyHandler.userRaise) {
						raise(gameManager.playerOrder.get(x), GameManager.bigBlind*10);
						actionPerformed = true;
						userTurn = false;
					}
					else if (keyHandler.userAllIn) {
						goAllIn(gameManager.playerOrder.get(x));
						actionPerformed = true;
						userTurn = false;
					}
					repaint();
				}
			}
			if (actionPerformed) {
				repaint();
				pause(1000);
			}
			if (x == gameManager.playerOrder.size() - 1 && preFlop == false) {
				newBettingRound = false;
			}
			if (x == 1 && preFlop == true) {
				newBettingRound = false;
			}
			if (x != gameManager.playerOrder.size() - 1) {
				x++;
			}
			else {
				x = 0;
			}
		}
	}
	
	private void betHelper(int betType, Player player) {
		if (betType == -1) {
			return;
		}
		else if (betType == 0) {
			fold(player);
		}
		else if (betType == 1) {
			check(player);
		}
		else if (betType == 2) {
			call(player);
		}
		else if (betType == 3) {
			goAllIn(player);
		}
		else {
			raise(player, betType);
		}
	}
	
	private boolean bettingRoundOver() {
		for (int x = 0; x < gameManager.playerOrder.size(); x++) {
			if (gameManager.playerOrder.get(x).getChipsContributed() != Player.getHighestBet() && 
					!(gameManager.playerOrder.get(x).isFolded()) && !(gameManager.playerOrder.get(x).isAllIn())) {
				return false;
			}
		}
		return true;
	}
	
	private void enter(long x) {
		while (!keyHandler.enter) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pause(x);
	}
	
	private static void pause(long x) {
		try {
			Thread.sleep(x);				
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void putRiver() {
		originalMovementSpeed /= 3;
		communityCards[4].revealCard();
		moveTo(communityCards[4], 875, 280);
		repaint();
		originalMovementSpeed *= 3;
	}
	
	private void putTurn() {
		originalMovementSpeed /= 3;
		communityCards[3].revealCard();
		moveTo(communityCards[3], 795, 280);
		repaint();
		originalMovementSpeed *= 3;
	}
	
	private void putFlop() {
		originalMovementSpeed /= 3;
		communityCards[0].revealCard();
		moveTo(communityCards[0], 555, 280);
		repaint();
		communityCards[1].revealCard();
		moveTo(communityCards[1], 635, 280);
		repaint();
		communityCards[2].revealCard();
		moveTo(communityCards[2], 715, 280);
		repaint();
		originalMovementSpeed *= 3;
	}
	
	private void dealCards() {
		for (int i = 0; i < gameManager.playerOrder.size(); i++) {
					gameManager.playerOrder.get(i).getPreFlopHand().getCard(1).setX(deckX + 6);
					gameManager.playerOrder.get(i).getPreFlopHand().getCard(1).setY(deckY - 6);
					gameManager.playerOrder.get(i).getPreFlopHand().getCard(0).setX(deckX + 6);
					gameManager.playerOrder.get(i).getPreFlopHand().getCard(0).setY(deckY - 6);
					gameManager.playerOrder.get(i).unfold();
			}
		
		for (int g = 0; g < 2; g++) {
			for (int i = 0; i < gameManager.playerOrder.size(); i++) {
				if (g == 0) {
					moveTo(gameManager.playerOrder.get(i).getPreFlopHand().getCard(1), gameManager.playerOrder.get(i).getTablePosition().getX(), 
							gameManager.playerOrder.get(i).getTablePosition().getY());
				}
				else {
					moveTo(gameManager.playerOrder.get(i).getPreFlopHand().getCard(0), gameManager.playerOrder.get(i).getTablePosition().getX()+25, 
							gameManager.playerOrder.get(i).getTablePosition().getY());
				}
				
			}
		}
		repaint();
		for (int x = 0; x < gameManager.playerOrder.size(); x++) {
			if (gameManager.playerOrder.get(x) instanceof User) {
				gameManager.playerOrder.get(x).getPreFlopHand().getCard(0).revealCard();
				gameManager.playerOrder.get(x).getPreFlopHand().getCard(1).revealCard();
			}
		}
	}
	
	private void moveTo(Moveable moveable, int x, int y) {
		double pauseTime = 1000000000/FPS;
		while (x != moveable.getX() || y != moveable.getY()) {
			double nextDraw = System.nanoTime() + pauseTime;
			if (y == moveable.getY()) {
				if (x != moveable.getX()) {
					moveable.increaseX(Math.abs(x-moveable.getX())/(x-moveable.getX())*movementSpeed);
				}
			}
			else if (x == moveable.getX()) {
				if (y < moveable.getY()) {
					moveable.increaseY(-1*movementSpeed);
				}
				else {
					moveable.increaseY(movementSpeed);
				}
			}
			else {
				double slope = ((y - moveable.getY()))/(x - moveable.getX());
				if (slope > 1 || slope < -1) {
					movementSpeed /= Math.abs(slope);
				}
				else if (slope < 1 && slope > -1) {
					movementSpeed *= Math.abs(slope);
				}
				if (y < moveable.getY() && x < moveable.getX()) {
					moveable.increaseX((-1 / slope)*movementSpeed);
					moveable.increaseY(-1*slope*movementSpeed);
				}
				else if (y < moveable.getY() && x > moveable.getX()) {
					moveable.increaseX((-1 / slope)*movementSpeed);
					moveable.increaseY(slope*movementSpeed);
				}
				else if (y > moveable.getY() && x < moveable.getX()) {
					moveable.increaseX((1/slope)*movementSpeed);
					moveable.increaseY(-1*slope*movementSpeed);
				}
				else if (y > moveable.getY() && x > moveable.getX()) {
					moveable.increaseX((1/slope)*movementSpeed);
					moveable.increaseY(slope*movementSpeed);
				}
			}	
			repaint();
			movementSpeed = originalMovementSpeed;
			
			if (x - moveable.getX() < movementSpeed && x - moveable.getX() > -1*movementSpeed) {
				moveable.setX(x);
			}
			if (y - moveable.getY() < movementSpeed && y - moveable.getY() > -1*movementSpeed) {
				moveable.setY(y);
			}
			
			repaint();
			
			double remainingTime = (nextDraw - System.nanoTime())/1000000;
			if (remainingTime < 0) {
				remainingTime = 0;
			}
			pause((long)remainingTime);
		}
		moveable.setX(x);
		moveable.setY(y);
		repaint();
		
	}
	
	private void setPositions() {
		movementSpeed = originalMovementSpeed;
		for (int x = 0; x < communityCards.length; x++) {
			communityCards[x].blockCard();
			moveTo(communityCards[x], deckX+6, deckY-6);
		}
		for (int x = 0; x < gameManager.playerOrder.size(); x++) {
			gameManager.playerOrder.get(x).getPreFlopHand().getCard(0).blockCard();
			moveTo(gameManager.playerOrder.get(x).getPreFlopHand().getCard(0), deckX+6, deckY-6);
			gameManager.playerOrder.get(x).getPreFlopHand().getCard(1).blockCard();
			moveTo(gameManager.playerOrder.get(x).getPreFlopHand().getCard(1), deckX+6, deckY-6);
		}
		repaint();
		movementSpeed = originalMovementSpeed;
		gameManager.nextHand();
	}
	
	private void revealCards() {
		for (int x = 0; x < gameManager.playerOrder.size(); x++) {
			if (!gameManager.playerOrder.get(x).isFolded()) {
				gameManager.playerOrder.get(x).getPreFlopHand().getCard(0).revealCard();
				gameManager.playerOrder.get(x).getPreFlopHand().getCard(1).revealCard();
			}
		}
		repaint();
	}
	
	
	private void setCommunityCards() {
		communityCards = gameManager.getCommunityCards();
	}
	
	private void check(Player player) {
		
	}
	
	private void goAllIn(Player player) {
		bet(player, player.getStackSize());
		player.goAllIn();
		
	}
	
	private void raise(Player player, int amount) {
		if (player.isAllIn()) {
		}
		else if (player.getStackSize() + player.getChipsContributed() > amount) {
			bet(player, amount + (Player.getHighestBet() - player.getChipsContributed()));
		}
		else if (player.getStackSize() <= amount) {
			goAllIn(player);
		}
	}
	
	private void call(Player player) {
		if (player.getStackSize() + player.getBetStack().getChipSum() + player.getChipsContributed() > Player.getHighestBet()) {
			bet(player, Player.getHighestBet() - player.getChipsContributed());
		}
		else {
			goAllIn(player);
		}
	}
	
	private void bet(Player player, int amount) {
		if (player.getStackSize() >= amount) {
			boolean flag = false;
			if (player.getStackSize() == amount) {
				flag = true;
			}
			originalMovementSpeed /= 32;
			player.getBetStack().setX(player.getTablePosition().getBetX());
			player.getBetStack().setY(player.getTablePosition().getBetY());
			tempBetStack.setX(player.getTablePosition().getChipX());
			tempBetStack.setY(player.getTablePosition().getChipY());
			tempBetStack.setChipSum(amount);
			player.bet1(amount);
			moveTo(tempBetStack, player.getTablePosition().getBetX(), player.getTablePosition().getBetY());
			player.bet2(amount);
			tempBetStack.setChipSum(0);
			if (flag) {
				player.goAllIn();
			}
			originalMovementSpeed *= 32;
		}
		else {
			goAllIn(player);
		}
	}
	
	private void fold(Player player) {
		player.fold();
		player.getPreFlopHand().getCard(0).blockCard();
		moveTo(player.getPreFlopHand().getCard(0), deckX+6, deckY-6);
		player.getPreFlopHand().getCard(1).blockCard();
		moveTo(player.getPreFlopHand().getCard(1), deckX+6, deckY-6);
	}
	
	
	
	
	
	private void betsToPot() {
		for (int x = 0; x < gameManager.playerOrder.size(); x++) {
			moveTo(gameManager.playerOrder.get(x).getBetStack(), 450, 325);
			gameManager.playerOrder.get(x).getBetStack().toPot();
			gameManager.playerOrder.get(x).getBetStack().setX(gameManager.playerOrder.get(x).getTablePosition().getChipX());
			gameManager.playerOrder.get(x).getBetStack().setY(gameManager.playerOrder.get(x).getTablePosition().getChipY());
		}
	}
	
	private void payOut() {
		for (int x = 0; x < gameManager.playerOrder.size(); x++) {
			gameManager.playerOrder.get(x).getBetStack().setX(450);
			gameManager.playerOrder.get(x).getBetStack().setY(325);
		}
		gameManager.payOut();
		for (int x = 0; x < gameManager.playerOrder.size(); x++) {
			if (gameManager.playerOrder.get(x).getBetStack().getChipSum() > 0) {
				moveTo(gameManager.playerOrder.get(x).getBetStack(), gameManager.playerOrder.get(x).getTablePosition().getChipX(), 
						gameManager.playerOrder.get(x).getTablePosition().getChipY());
				gameManager.playerOrder.get(x).getBetStack().toPlayer(gameManager.playerOrder.get(x));
				repaint();
			}
			else {
				gameManager.playerOrder.get(x).getBetStack().setX(gameManager.playerOrder.get(x).getTablePosition().getChipX());
				gameManager.playerOrder.get(x).getBetStack().setY(gameManager.playerOrder.get(x).getTablePosition().getChipY());
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHints(rh);
		createTable(g2D);
		drawDeck(g2D);
		if (keyHandler.paused) {
			g2D.setColor(Color.GRAY);
			g2D.fillRect(0, 0, screenWidth, screenHeight);
		}
		if (userTurn) {
			callButton.setVisible(true);
			callButton.paintComponents(g2D);
		}
		g2D.dispose();
	}
	
	private void createTable(Graphics2D g2D) {
		Ellipse2D.Double e = new Ellipse2D.Double(25, 10, 1350, 650);
		g2D.setColor(Color.black);
		g2D.fill(e);
		Ellipse2D.Double e2 = new Ellipse2D.Double(50, 35, 1300, 600);
		g2D.setColor(new Color(0x380000));
		g2D.fill(e2);
		Ellipse2D.Double e3 = new Ellipse2D.Double(300, 200, 800, 270);
		g2D.setColor(Color.black);
		g2D.fill(e3);
		Ellipse2D.Double e4 = new Ellipse2D.Double(415, 285, 125, 85);
		g2D.setColor(new Color(0x800000));
		g2D.fill(e4);
	}
	
	private void drawDeck(Graphics2D g2D) {
		g2D.drawImage(new Card(deckX, deckY).getCardImage(), deckX, deckY, null);
		g2D.drawImage(new Card(deckX+2, deckY-2).getCardImage(), deckX + 2, deckY - 2, null);
		g2D.drawImage(new Card(deckX+4, deckY-4).getCardImage(), deckX + 4, deckY - 4, null);
		g2D.drawImage(new Card(deckX+6, deckY-6).getCardImage(), deckX + 6, deckY - 6, null);
		for (int i = gameManager.playerOrder.size()-1; i >= 0; i--) {
			g2D.drawImage(gameManager.playerOrder.get(i).getPreFlopHand().getCard(1).getCardImage(),
					(int)gameManager.playerOrder.get(i).getPreFlopHand().getCard(1).getX(), 
					(int)gameManager.playerOrder.get(i).getPreFlopHand().getCard(1).getY(), null);
			g2D.drawImage(gameManager.playerOrder.get(i).getPreFlopHand().getCard(0).getCardImage(),
					(int)gameManager.playerOrder.get(i).getPreFlopHand().getCard(0).getX(), 
					(int)gameManager.playerOrder.get(i).getPreFlopHand().getCard(0).getY(), null);
			drawChipStack(gameManager.playerOrder.get(i).getStackSize(), gameManager.playerOrder.get(i).getTablePosition().getChipX(), 
					gameManager.playerOrder.get(i).getTablePosition().getChipY(), g2D, false);
			g2D.setColor(Color.LIGHT_GRAY);
			g2D.setFont(new Font("Ariel", Font.BOLD, 16));
			g2D.drawString("" + (gameManager.playerOrder.get(i).getStackSize()), gameManager.playerOrder.get(i).getTablePosition().getChipX() + 70,
					gameManager.playerOrder.get(i).getTablePosition().getChipY());
			if (gameManager.playerOrder.get(i).getBetStack().getChipSum() > 0) {
				drawChipStack(gameManager.playerOrder.get(i).getBetStack().getChipSum(), 
						(int)gameManager.playerOrder.get(i).getBetStack().getX(), 
						(int)gameManager.playerOrder.get(i).getBetStack().getY(), g2D, false);
				g2D.drawString("" + gameManager.playerOrder.get(i).getBetStack().getChipSum(),
						gameManager.playerOrder.get(i).getTablePosition().getBetX(), 
						gameManager.playerOrder.get(i).getTablePosition().getBetY() + 32);
			}
			if (tempBetStack.getChipSum() > 0) {
				drawChipStack(tempBetStack.getChipSum(), (int)tempBetStack.getX(), (int)tempBetStack.getY(), g2D, false);
			}
		}
		for (int i = communityCards.length - 1; i >= 0; i--) {
			g2D.drawImage(communityCards[i].getCardImage(), (int)communityCards[i].getX(), (int)communityCards[i].getY(), null);
		}
		drawChipStack(GameManager.getPot(), 450, 325, g2D, true);
		g2D.drawString("Pot", 465, 300);
		g2D.drawString("" + gameManager.pot, 465, 360);
	}
	
	private void drawChipStack(int chipNum, int x, int y, Graphics2D g2D, boolean pot) {
		int multiplier = 1;
		if (pot) {
			multiplier = 3;
		}
		if (chipNum >= 7500/multiplier) {
			for (int i = 0; chipNum >= 7500/multiplier; i++) {
				g2D.drawImage(chip2500, x+45, y-(i*3)-14, null);
				chipNum -= 2500;
				if (chipNum < 7500/multiplier) {
					drawChipStack(chipNum, x, y, g2D, pot);
				}
			}
		}
		else if (chipNum >= 1500/multiplier && chipNum < 7500/multiplier) {
			for (int i = 0; chipNum >= 1500/multiplier; i++) {
				g2D.drawImage(chip500, x+27, y-(i*3)-14, null);
				chipNum -= 500;
				if (chipNum < 1500/multiplier) {
					drawChipStack(chipNum, x, y, g2D, pot);
				}
			}
		}
		else if (chipNum >= 300/multiplier && chipNum < 1500/multiplier) {
			for (int i = 0; chipNum >= 300/multiplier; i++) {
				g2D.drawImage(chip100, x+9, y-(i*3)-14, null);
				chipNum -= 100;
				if (chipNum < 300/multiplier) {
					drawChipStack(chipNum, x, y, g2D, pot);
				}
			}
		}
		else if (chipNum >= 75/multiplier && chipNum < 300/multiplier) {
			for (int i = 0; chipNum >= 75/multiplier; i++) {
				g2D.drawImage(chip25, x+34, y-(i*3), null);
				chipNum -= 25;
				if (chipNum < 75/multiplier) {
					drawChipStack(chipNum, x, y, g2D, pot);
				}
			}
		}
		else if (chipNum >= 15/multiplier && chipNum < 75/multiplier) {
			for (int i = 0; chipNum >= 15/multiplier; i++) {
				g2D.drawImage(chip5, x+17, y-(i*3), null);
				chipNum -= 5;
				if (chipNum < 15/multiplier) {
					drawChipStack(chipNum, x, y, g2D, pot);
				}
			}
		}
		else if (chipNum < 15/multiplier) {
			for (int i = 0; i < chipNum; i++) {
				g2D.drawImage(chip1, x, y-(i*3), null);
			}
		}
	}

}
