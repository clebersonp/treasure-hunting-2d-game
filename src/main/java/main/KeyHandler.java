package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private boolean upPressed, downPressed, leftPressed, rightPressed;

	private boolean enterPressed;

	// DEBUG
	private boolean checkDrawTime;

	private GamePanel gp;

	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		// PLAY STATE
		if (this.gp.getGameState() == GamePanel.PLAY_STATE) {

			switch (keyCode) {
			case KeyEvent.VK_W, KeyEvent.VK_UP -> this.upPressed = true;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> this.downPressed = true;
			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> this.leftPressed = true;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> this.rightPressed = true;
			case KeyEvent.VK_T -> this.checkDrawTime = !this.checkDrawTime;
			case KeyEvent.VK_P -> this.gp.setGameState(GamePanel.PAUSE_STATE);
			case KeyEvent.VK_ENTER -> this.enterPressed = true;
			}

		}

		// PAUSE STATE
		else if (this.gp.getGameState() == GamePanel.PAUSE_STATE) {
			if (keyCode == KeyEvent.VK_P) {
				this.gp.setGameState(GamePanel.PLAY_STATE);
			}
		}

		// DIALOGUE STATE
		else if (this.gp.getGameState() == GamePanel.DIALOGUE_STATE) {
			if (keyCode == KeyEvent.VK_ENTER) {
				this.gp.setGameState(GamePanel.PLAY_STATE);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_W, KeyEvent.VK_UP -> this.upPressed = false;
		case KeyEvent.VK_S, KeyEvent.VK_DOWN -> this.downPressed = false;
		case KeyEvent.VK_A, KeyEvent.VK_LEFT -> this.leftPressed = false;
		case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> this.rightPressed = false;
		}
	}

	public boolean hasAnyDirectionKeyPressed() {
		return this.upPressed || this.downPressed || this.leftPressed || this.rightPressed;
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public boolean isCheckDrawTime() {
		return checkDrawTime;
	}

	public boolean isEnterPressed() {
		return enterPressed;
	}

	public void setEnterPressed(boolean enterPressed) {
		this.enterPressed = enterPressed;
	}

}
