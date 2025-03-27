package players;

import gameManager.GameManager;
import graphics.Moveable;

public class BetStack implements Moveable {
	
	private int chipSum;
	private double x;
	private double y;
	
	public BetStack(int chipSum) {
		this.chipSum = chipSum;
	}
	
	public void addChips(int a) {
		chipSum += a;
	}
	
	public void setChipSum(int a) {
		chipSum = a;
	}
	
	public int getChipSum() {
		return chipSum;
	}
	
	public void toPot() {
		GameManager.addToPot(chipSum);
		chipSum = 0;
	}
	
	public void toPlayer(Player player) {
		player.recieveChips(chipSum);
		chipSum = 0;
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
