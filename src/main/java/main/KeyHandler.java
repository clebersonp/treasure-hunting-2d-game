package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private boolean upPressed, downPressed, leftPressed, rightPressed, shotKeyPressed;

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

		// TITLE STATE
		if (this.gp.getGameState() == GamePanel.TITLE_STATE) {
			this.titleState(keyCode);
		}

		// PLAY STATE
		else if (this.gp.getGameState() == GamePanel.PLAY_STATE) {
			this.playState(keyCode);
		}

		// PAUSE STATE
		else if (this.gp.getGameState() == GamePanel.PAUSE_STATE) {
			this.pauseState(keyCode);
		}

		// DIALOGUE STATE
		else if (this.gp.getGameState() == GamePanel.DIALOGUE_STATE) {
			this.dialogueState(keyCode);
		}

		// CHARACTER STATE
		else if (this.gp.getGameState() == GamePanel.CHARACTER_STATE) {
			this.characterState(keyCode);
		}

		// OPTIONS STATE
		else if (this.gp.getGameState() == GamePanel.OPTIONS_STATE) {
			this.optionsState(keyCode);
		}
	}

	public void optionsState(int keyCode) {

		int maxCommandNum = 0;
		switch (this.gp.getUi().getSubState()) {
		case UI.SubState.LEVEL_0 -> maxCommandNum = 5;
		case UI.SubState.LEVEL_3 -> maxCommandNum = 1;
		}

		switch (keyCode) {
		case KeyEvent.VK_ESCAPE -> {
			this.gp.getUi().setCommandNum(0);
			this.gp.getUi().setSubState(UI.SubState.LEVEL_0);
			this.gp.setGameState(GamePanel.PLAY_STATE);
		}
		case KeyEvent.VK_ENTER -> this.enterPressed = true;
		case KeyEvent.VK_W, KeyEvent.VK_UP -> {
			this.gp.getUi().setCommandNum(this.gp.getUi().getCommandNum() - 1);
			new Sound(Sound.INVENTORY_CURSOR).play();
			if (this.gp.getUi().getCommandNum() < 0) {
				this.gp.getUi().setCommandNum(maxCommandNum);
			}
		}
		case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
			this.gp.getUi().setCommandNum(this.gp.getUi().getCommandNum() + 1);
			new Sound(Sound.INVENTORY_CURSOR).play();
			if (this.gp.getUi().getCommandNum() > maxCommandNum) {
				this.gp.getUi().setCommandNum(0);
			}
		}
		case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
			if (this.gp.getUi().getSubState() == UI.SubState.LEVEL_0) {
				if (this.gp.getUi().getCommandNum() == 1 && this.gp.getMusic().getMusicVolumeScale() > 0) {
					this.gp.getMusic().setMusicVolumeScale(this.gp.getMusic().getMusicVolumeScale() - 1);
					new Sound(Sound.INVENTORY_CURSOR).play();
				}
				if (this.gp.getUi().getCommandNum() == 2 && Sound.getSeVolumeScale() > 0) {
					Sound.setSeVolumeScale(Sound.getSeVolumeScale() - 1);
					new Sound(Sound.INVENTORY_CURSOR).play();
				}
			}
		}
		case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
			if (this.gp.getUi().getSubState() == UI.SubState.LEVEL_0) {
				if (this.gp.getUi().getCommandNum() == 1 && this.gp.getMusic().getMusicVolumeScale() < 5) {
					this.gp.getMusic().setMusicVolumeScale(this.gp.getMusic().getMusicVolumeScale() + 1);
					new Sound(Sound.INVENTORY_CURSOR).play();
				}
				if (this.gp.getUi().getCommandNum() == 2 && Sound.getSeVolumeScale() < 5) {
					Sound.setSeVolumeScale(Sound.getSeVolumeScale() + 1);
					new Sound(Sound.INVENTORY_CURSOR).play();
				}
			}
		}
		}
	}

	public void titleState(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_W, KeyEvent.VK_UP -> this.gp.getUi().setCommandNum(this.gp.getUi().getCommandNum() - 1);
		case KeyEvent.VK_S, KeyEvent.VK_DOWN -> this.gp.getUi().setCommandNum(this.gp.getUi().getCommandNum() + 1);
		case KeyEvent.VK_ENTER -> {
			if (this.gp.getUi().getCommandNum() == 0) {
				this.gp.setGameState(GamePanel.PLAY_STATE);
				this.gp.playMusic(this.gp.getMusic());
			} else if (this.gp.getUi().getCommandNum() == 1) {
				// LOAD GAME LATER
				System.out.println("// TODO LOAD GAME LATER");
			} else if (this.gp.getUi().getCommandNum() == 2) {
				System.exit(0);
			}
		}
		}

		if (this.gp.getUi().getCommandNum() < 0) {
			this.gp.getUi().setCommandNum(2);
		} else if (this.gp.getUi().getCommandNum() > 2) {
			this.gp.getUi().setCommandNum(0);
		}
	}

	public void playState(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_W, KeyEvent.VK_UP -> this.upPressed = true;
		case KeyEvent.VK_S, KeyEvent.VK_DOWN -> this.downPressed = true;
		case KeyEvent.VK_A, KeyEvent.VK_LEFT -> this.leftPressed = true;
		case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> this.rightPressed = true;
		case KeyEvent.VK_T -> this.checkDrawTime = !this.checkDrawTime;
		case KeyEvent.VK_P -> this.gp.setGameState(GamePanel.PAUSE_STATE);
		case KeyEvent.VK_C -> this.gp.setGameState(GamePanel.CHARACTER_STATE);
		case KeyEvent.VK_ENTER -> this.enterPressed = true;
		case KeyEvent.VK_F -> this.shotKeyPressed = true;
		case KeyEvent.VK_ESCAPE -> this.gp.setGameState(GamePanel.OPTIONS_STATE);
		}
	}

	public void pauseState(int keyCode) {
		if (keyCode == KeyEvent.VK_P) {
			this.gp.setGameState(GamePanel.PLAY_STATE);
		}
	}

	public void dialogueState(int keyCode) {
		if (keyCode == KeyEvent.VK_ENTER) {
			this.gp.setGameState(GamePanel.PLAY_STATE);
		}
	}

	public void characterState(int keyCode) {
		if (keyCode == KeyEvent.VK_C) {
			this.gp.setGameState(GamePanel.PLAY_STATE);
		}
		// Inventory Cursor
		if ((keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) && (this.gp.getUi().getSlotInventoryRow() > 0)) {
			this.gp.getUi().setSlotInventoryRow(this.gp.getUi().getSlotInventoryRow() - 1);
			new Sound(Sound.INVENTORY_CURSOR).play();
		}
		if ((keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) && (this.gp.getUi().getSlotInventoryRow() < 3)) {
			this.gp.getUi().setSlotInventoryRow(this.gp.getUi().getSlotInventoryRow() + 1);
			new Sound(Sound.INVENTORY_CURSOR).play();
		}
		if ((keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) && (this.gp.getUi().getSlotInventoryCol() > 0)) {
			this.gp.getUi().setSlotInventoryCol(this.gp.getUi().getSlotInventoryCol() - 1);
			new Sound(Sound.INVENTORY_CURSOR).play();
		}
		if ((keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) && (this.gp.getUi().getSlotInventoryCol() < 4)) {
			this.gp.getUi().setSlotInventoryCol(this.gp.getUi().getSlotInventoryCol() + 1);
			new Sound(Sound.INVENTORY_CURSOR).play();
		}
		if (keyCode == KeyEvent.VK_ENTER) {
			this.gp.getPlayer().selectInventorySlotItem();
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
		case KeyEvent.VK_F -> this.shotKeyPressed = false;
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

	public boolean isShotKeyPressed() {
		return shotKeyPressed;
	}

	public void setShotKeyPressed(boolean shotKeyPressed) {
		this.shotKeyPressed = shotKeyPressed;
	}

}
