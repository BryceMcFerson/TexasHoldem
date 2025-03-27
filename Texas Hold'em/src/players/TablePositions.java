package players;

public enum TablePositions {
	One(475, 510, 480, 485, 470, 405), Two(250, 450, 265, 425, 355, 375), 
	Three(250, 130, 265, 250, 340, 280), Four(455, 70, 480, 185, 475, 240), 
	Five(670, 45, 675, 165, 665, 210), Six(890, 70, 900, 185, 875, 240), 
	Seven(1065, 130, 1065, 250, 990, 280), Eight(1065, 450, 1075, 425, 995, 385), 
	Nine(870, 510, 880, 485, 815, 440);
	
	private int x;
	private int y;
	private int chipX;
	private int chipY;
	private int betX;
	private int betY;
	
	private TablePositions(int x, int y, int chipX, int chipY, int betX, int betY) {
		this.x = x;
		this.y = y;
		this.chipX = chipX;
		this.chipY = chipY;
		this.betX = betX;
		this.betY = betY;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getChipX() {
		return chipX;
	}
	
	public int getChipY() {
		return chipY;
	}
	
	public int getBetX() {
		return betX;
	}
	
	public int getBetY() {
		return betY;
	}
	
}
