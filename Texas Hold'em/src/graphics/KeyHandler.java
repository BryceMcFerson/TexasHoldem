package graphics;

import java.awt.event.*;

public class KeyHandler implements KeyListener {

	public boolean enter, paused, started, userCheck, userFold, userCall, userRaise, userAllIn;
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (!paused) {
			if (code == KeyEvent.VK_ENTER) {
				enter = true;
			}
			if (code == KeyEvent.VK_SPACE) {
				userCheck = true;
			}
			if (code == KeyEvent.VK_C) {
				userCall = true;
			}
			if (code == KeyEvent.VK_R) {
				userRaise = true;
			}
			if (code == KeyEvent.VK_A) {
				userAllIn = true;
			}
			if (code == KeyEvent.VK_F) {
				userFold = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_ESCAPE) {
			paused = !paused;
		}
		if (!paused) {
			if (code == KeyEvent.VK_ENTER) {
				enter = false;
			}
			if (code == KeyEvent.VK_SPACE) {
				userCheck = false;
			}
			if (code == KeyEvent.VK_C) {
				userCall = false;
			}
			if (code == KeyEvent.VK_R) {
				userRaise = false;
			}
			if (code == KeyEvent.VK_A) {
				userAllIn = false;
			}
			if (code == KeyEvent.VK_F) {
				userFold = false;
			}
		}
		
	}

}
