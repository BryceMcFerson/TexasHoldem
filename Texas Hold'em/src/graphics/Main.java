package graphics;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Texas Hold'em");
		window.setIconImage(new ImageIcon("Pictures/BM-Logo.png").getImage());
		GamePanel gamePanel = new GamePanel(9);
		window.add(gamePanel);
		window.pack();
		
		window.setLocation(75, 50);
		window.setVisible(true);
		
		gamePanel.startGameThread();

	}

}
