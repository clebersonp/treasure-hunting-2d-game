package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private boolean upPressed, downPressed, leftPressed, rightPressed;

	// DEBUG
	private boolean checkDrawTime;

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
//		System.out.println(String.format("KeyPressedCode: %s, KeyPressed: %s", keyCode, KeyEvent.getKeyText(keyCode)));
		switch (keyCode) {
		case KeyEvent.VK_W, KeyEvent.VK_UP -> this.upPressed = true;
		case KeyEvent.VK_S, KeyEvent.VK_DOWN -> this.downPressed = true;
		case KeyEvent.VK_A, KeyEvent.VK_LEFT -> this.leftPressed = true;
		case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> this.rightPressed = true;
		case KeyEvent.VK_T -> this.checkDrawTime = !this.checkDrawTime;
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

}
