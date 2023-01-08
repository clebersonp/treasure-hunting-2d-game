package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_Mana;

public class UI {

	private Font purisaB, underdog, pressStart;
	private GamePanel gp;
	private String currentDialogue = "";
	private int commandNum = 0;
	private BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;

	List<String> messages = new ArrayList<>();
	List<Integer> messagesCounter = new ArrayList<>();
	private int slotInventoryCol = 0;
	private int slotInventoryRow = 0;
	private int subState = SubState.LEVEL_0;

	public UI(GamePanel gp) {
		this.gp = gp;

		try (InputStream isPurisa = getClass().getResourceAsStream("/fonts/purisa_bold.ttf");
				InputStream isUnderdog = getClass().getResourceAsStream("/fonts/underdog_regular.ttf");
				InputStream isPressStart = getClass().getResourceAsStream("/fonts/pressStart2P_Regular.ttf")) {

			this.purisaB = Font.createFont(Font.TRUETYPE_FONT, isPurisa);
			this.underdog = Font.createFont(Font.TRUETYPE_FONT, isUnderdog);
			this.pressStart = Font.createFont(Font.TRUETYPE_FONT, isPressStart);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// CREATE HUD OBJECT
		Entity obj_Heart = new OBJ_Heart(this.gp);
		this.heart_full = obj_Heart.getImage();
		this.heart_half = obj_Heart.getImage2();
		this.heart_blank = obj_Heart.getImage3();

		// CREATE MANA OBJECT
		Entity obj_Mana = new OBJ_Mana(this.gp);
		this.crystal_full = obj_Mana.getImage();
		this.crystal_blank = obj_Mana.getImage2();

	}

	public void draw(Graphics2D g2) {

		g2.setFont(this.underdog);
//		g2.setFont(this.purisaB);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);

		switch (this.gp.getGameState()) {
		case GamePanel.TITLE_STATE -> this.drawTitleScreen(g2);
		case GamePanel.PLAY_STATE -> {
			this.drawPlayerLife(g2);
			this.drawPlayScreen(g2);
			this.drawMessages(g2);
		}
		case GamePanel.PAUSE_STATE -> {
			this.drawPlayerLife(g2);
			this.drawPauseScreen(g2);
		}
		case GamePanel.DIALOGUE_STATE -> {
			this.drawPlayerLife(g2);
			this.drawDialogScreen(g2);
		}
		case GamePanel.CHARACTER_STATE -> {
			this.drawCharacterScreen(g2);
			this.drawInventory(g2);
		}
		case GamePanel.OPTIONS_STATE -> {
			this.drawOptionsScreen(g2);
		}
		case GamePanel.GAME_OVER_STATE -> {
			this.drawGameOverScreen(g2);
		}
		}

	}

	private void drawGameOverScreen(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));

		text = "Game Over";
		// Shadow
		g2.setColor(Color.BLACK);
		x = this.getXTextPositionCenter(g2, text);
		y = this.gp.getTileSize() * 4;
		g2.drawString(text, x, y);
		// Main
		g2.setColor(Color.WHITE);
		g2.drawString(text, x - 4, y - 4);

		// Retry
		g2.setFont(g2.getFont().deriveFont(50F));
		text = "Retry";
		x = this.getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize() * 4;
		g2.drawString(text, x, y);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.BLACK);
			g2.drawString(">", x - (this.gp.getTileSize() - 4), y + 4);
			g2.drawString(text, x + 4, y + 4);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);
		}

		// Back to the title screen
		text = "Quit";
		x = this.getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize();
		g2.drawString(text, x, y);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.BLACK);
			g2.drawString(">", x - (this.gp.getTileSize() - 4), y + 4);
			g2.drawString(text, x + 4, y + 4);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);
		}

	}

	public void drawOptionsScreen(Graphics2D g2) {

		g2.setColor(Color.WHITE);
		g2.setFont(g2.getFont().deriveFont(32F));

		// SUB WINDOW
		final int frameX = this.gp.getTileSize() * 6;
		final int frameY = this.gp.getTileSize();
		final int frameWidth = this.gp.getTileSize() * 8;
		final int frameHeight = this.gp.getTileSize() * 11;
		this.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

		switch (this.subState) {
		case SubState.LEVEL_0 -> this.optionsTop(g2, frameX, frameY);
		case SubState.LEVEL_1 -> this.optionsFullScreenNotification(g2, frameX, frameY);
		case SubState.LEVEL_2 -> this.optionsControl(g2, frameX, frameY);
		case SubState.LEVEL_3 -> this.optionsEndGameCofirmation(g2, frameX, frameY);
		}

		// RESET ENTER KEY
		this.gp.getKeyHandler().setEnterPressed(false);

	}

	private void optionsTop(Graphics2D g2, int frameX, int frameY) {
		int textX;
		int textY;

		// TITLE
		String text = "Options";
		textX = this.getXTextPositionCenter(g2, text);
		textY = frameY + this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		g2.setFont(g2.getFont().deriveFont(25F));

		// FULL SCREEN ON/OFF
		text = "Full Screen";
		textX = frameX + this.gp.getTileSize();
		textY += this.gp.getTileSize() * 2;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.gp.setFullScreenOn(!this.gp.isFullScreenOn());
				this.subState = SubState.LEVEL_1;
			}
		}

		// MUSIC VOLUME
		text = "Music";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
		}

		// SE VOLUME
		text = "SE";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 2) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
		}

		// CONTROL
		text = "Control";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 3) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_2;
				this.commandNum = 0;
			}
		}

		// END GAME
		text = "End Game";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 4) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_3;
				this.commandNum = 0;
			}

		}

		// BACK
		text = "Back";
		textY += this.gp.getTileSize() * 2;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 5) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.commandNum = 0;
				this.gp.setGameState(GamePanel.PLAY_STATE);
				// CONFIG
				this.gp.getConfig().save();
			}
		}

		g2.setStroke(new BasicStroke(3F));

		// FULL SCREEN CHECK BOX
		textX = frameX + (int) (this.gp.getTileSize() * 4.5);
		textY = frameY + this.gp.getTileSize() * 2 + 24;
		g2.drawRect(textX, textY, 24, 24);
		if (this.gp.isFullScreenOn()) {
			g2.fillRect(textX, textY, 24, 24);
		}

		// MUSIC VOLUME
		textY += this.gp.getTileSize();
		g2.drawRect(textX, textY, 120, 24);
		g2.fillRect(textX, textY, 24 * this.gp.getMusic().getMusicVolumeScale(), 24);

		// SE VOLUME
		textY += this.gp.getTileSize();
		g2.drawRect(textX, textY, 120, 24);
		g2.fillRect(textX, textY, 24 * Sound.getSeVolumeScale(), 24);

	}

	private void optionsFullScreenNotification(Graphics2D g2, int frameX, int frameY) {
		int textX = frameX + this.gp.getTileSize();
		int textY = frameY + this.gp.getTileSize() * 3;

		this.currentDialogue = "The change will take \neffect after restarting \nthe game.";
		for (String line : this.currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// BACK
		String text = "Back";
		textY += this.gp.getTileSize() * 5;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
			}
		}
	}

	private void optionsControl(Graphics2D g2, int frameX, int frameY) {

		int textX;
		int textY;

		// TITLE
		String text = "Control";
		textX = this.getXTextPositionCenter(g2, text);
		textY = frameY + this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		textX = frameX + this.gp.getTileSize();
		textY += this.gp.getTileSize();

		g2.setFont(g2.getFont().deriveFont(20F));

		text = "Move";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

		text = "Confirm/Attack";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

		text = "Shoot/Cast";
		g2.drawString(text, textX, textY);

		text = "Character Screen";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		text = "Pause";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		text = "Options";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize() * 2;

		text = "Back";
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
				this.commandNum = 3;
			}
		}

		textX = frameX + this.gp.getTileSize() * 6;
		textY = frameY + this.gp.getTileSize() * 2;

		text = "AWSD";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

		text = "ENTER";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "F";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "C";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "P";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "ESC";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

	}

	private void optionsEndGameCofirmation(Graphics2D g2, int frameX, int frameY) {
		int textX = frameX + this.gp.getTileSize();
		int textY = frameY + this.gp.getTileSize() * 3;

		this.currentDialogue = "Quit the game and \nreturn to the title \nscreen?";

		for (String line : this.currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// YES
		String text = "Yes";
		textX = this.getXTextPositionCenter(g2, text);
		textY += this.gp.getTileSize() * 2;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
				this.gp.setGameState(GamePanel.TITLE_STATE);
				// CONFIG
				this.gp.getConfig().save();
				this.gp.getMusic().stop();
			}
		}

		// NO
		text = "No";
		textX = this.getXTextPositionCenter(g2, text);
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
				this.commandNum = 4;
			}
		}
	}

	private void drawInventory(Graphics2D g2) {

		// FRAME
		final int frameX = (this.gp.getTileSize() * 13) + (this.gp.getTileSize() / 2);
		final int frameY = this.gp.getTileSize();
		final int frameWidth = this.gp.getTileSize() * 6;
		final int frameHeight = this.gp.getTileSize() * 5;
		this.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

		// SLOT
		final int slotXStart = frameX + 18;
		final int slotYStart = frameY + 18;
		int slotX = slotXStart;
		int slotY = slotYStart;
		int slotSize = this.gp.getTileSize() + 3;

		// DRAW PLAYER'S INVENTORY ITEMS
		for (int i = 0; i < this.gp.getPlayer().getInventory().size(); i++) {

			// EQUIP CURSOR
			if (this.gp.getPlayer().getInventory().get(i) == this.gp.getPlayer().getCurrentWeapon()
					|| this.gp.getPlayer().getInventory().get(i) == this.gp.getPlayer().getCurrentShield()) {

				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, this.gp.getTileSize(), this.gp.getTileSize(), 10, 10);

			}

			g2.drawImage(this.gp.getPlayer().getInventory().get(i).getDown1(), slotX, slotY, null);

			slotX += slotSize;
			if (i == 4 || i == 9 || i == 14) {
				slotX = slotXStart;
				slotY += slotSize;
			}
		}

		// CURSOR
		int cursorX = slotXStart + (slotSize * this.slotInventoryCol);
		int cursorY = slotYStart + (slotSize * this.slotInventoryRow);
		int cursorWidth = this.gp.getTileSize();
		int cursorHeight = this.gp.getTileSize();
		// DRAW CURSOR
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(3F));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

		// DESCRIPTION FRAME
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight;
		int dFrameWidth = frameWidth;
		int dFrameHeight = this.gp.getTileSize() * 3;

		// DRAW DESCRIPTION TEXT
		int textX = dFrameX + 20;
		int textY = dFrameY + this.gp.getTileSize();
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22));

		int itemIndex = this.getInventoryItemIndexOnSlot();
		if (itemIndex < this.gp.getPlayer().getInventory().size()) {

			this.drawSubWindow(g2, dFrameX, dFrameY, dFrameWidth, dFrameHeight);

			final Entity item = this.gp.getPlayer().getInventory().get(itemIndex);
			String description = item.getDescription();
			for (String line : description.split("\n")) {
				g2.drawString(line, textX, textY);
				textY += 32;
			}
		}
	}

	public int getInventoryItemIndexOnSlot() {
		return this.slotInventoryCol + (this.slotInventoryRow * 5);
	}

	private void drawMessages(Graphics2D g2) {

		int messageX = this.gp.getTileSize();
		int messageY = this.gp.getTileSize() * 4;

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

		for (int i = 0; i < this.messages.size(); i++) {

			if (this.messages.get(i) != null) {

				// SHADOW
				g2.setColor(Color.BLACK);
				g2.drawString(this.messages.get(i), messageX + 2, messageY + 2);

				g2.setColor(Color.WHITE);
				g2.drawString(this.messages.get(i), messageX, messageY);

				int counter = this.messagesCounter.get(i) + 1;
				this.messagesCounter.set(i, counter);

				messageY += 50;

				if (this.messagesCounter.get(i) > 180) { // 3 seconds
					this.messages.remove(i);
					this.messagesCounter.remove(i);
				}

			}
		}
	}

	private void drawPlayerLife(Graphics2D g2) {
		int x = this.gp.getTileSize() / 2;
		int y = this.gp.getTileSize() / 2;
		int i = 0;

		// DRAW MAX LIFE
		while (i < this.gp.getPlayer().getMaxLife() / 2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += this.gp.getTileSize();
		}

		// RESET
		x = this.gp.getTileSize() / 2;
		y = this.gp.getTileSize() / 2;
		i = 0;

		// DRAW CURRENT LIFE
		while (i < this.gp.getPlayer().getLife()) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if (i < this.gp.getPlayer().getLife()) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += this.gp.getTileSize();
		}

		// DRAW MAX MANA
		x = this.gp.getTileSize() / 2;
		y = this.gp.getTileSize() + 25;
		i = 0;
		while (i < this.gp.getPlayer().getMaxMana()) {
			g2.drawImage(this.crystal_blank, x, y, null);
			i++;
			x += 33;
		}

		// DRAW MANA
		x = this.gp.getTileSize() / 2;
		y = this.gp.getTileSize() + 25;
		i = 0;
		while (i < this.gp.getPlayer().getMana()) {
			g2.drawImage(crystal_full, x, y, null);
			i++;
			x += 33;
		}
	}

	private void drawTitleScreen(Graphics2D g2) {
		// BACKGROUND COLOR
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

		// TITLE NAME
		g2.setFont(this.pressStart.deriveFont(Font.BOLD, 35F));
		String text = "Blue Boy Adventure";
		int x = this.getXTextPositionCenter(g2, text);
		int y = this.gp.getTileSize() * 3;

		// SHADOW COLOR
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(text, x + 7, y + 5);
		// MAIN COLOR
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);

		// Blue Boy image
		x = this.gp.getScreenWidth() / 2 - (this.gp.getTileSize() * 2) / 2;
		y += this.gp.getTileSize();
		g2.drawImage(this.gp.getPlayer().getDown1(), x, y, this.gp.getTileSize() * 2, this.gp.getTileSize() * 2, null);

		// MENU
		g2.setFont(this.pressStart.deriveFont(Font.PLAIN, 25F));
		text = "NEW GAME";
		x = getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize() * 4;
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (x - this.gp.getTileSize()) + 3, y + 2);
			g2.drawString(text, x + 3, y + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);
		} else {
			g2.setColor(Color.WHITE);
			g2.drawString(text, x, y);
		}

		text = "LOAD GAME";
		x = getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize();
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (x - this.gp.getTileSize()) + 3, y + 2);
			g2.drawString(text, x + 3, y + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);

		} else {
			g2.setColor(Color.WHITE);
			g2.drawString(text, x, y);
		}

		text = "QUIT";
		x = getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize();
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);
		if (this.commandNum == 2) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (x - this.gp.getTileSize()) + 3, y + 2);
			g2.drawString(text, x + 3, y + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);

		} else {
			g2.setColor(Color.WHITE);
			g2.drawString(text, x, y);
		}
	}

	private void drawDialogScreen(Graphics2D g2) {

		// WINDOW
		int x = this.gp.getTileSize() * 2;
		int y = this.gp.getTileSize() / 2;
		int width = this.gp.getScreenWidth() - (this.gp.getTileSize() * 4);
		int height = this.gp.getTileSize() * 4;

		this.drawSubWindow(g2, x, y, width, height);

		x += this.gp.getTileSize();
		y += this.gp.getTileSize();
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		g2.setColor(Color.WHITE);
		for (String line : this.currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
	}

	private void drawCharacterScreen(Graphics2D g2) {
		// CREATE A FRAME
		final int frameX = this.gp.getTileSize() / 2;
		final int frameY = this.gp.getTileSize() / 2;
		final int frameWidth = this.gp.getTileSize() * 5;
		final int frameHeight = (this.gp.getTileSize() * 10) + 10;

		this.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

		// TEXT
		g2.setColor(Color.WHITE);
		g2.setFont(g2.getFont().deriveFont(28F));

		int textX = frameX + 20;
		int textY = frameY + this.gp.getTileSize();
		final int lineHeight = 38;

		// NAMES
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += this.gp.getPlayer().getCurrentWeapon().getDown1().getHeight() - 10;
		g2.drawString("Weapon", textX, textY);
		textY += this.gp.getPlayer().getCurrentShield().getDown1().getHeight() - 10;
		g2.drawString("Shield", textX, textY);

		// VALUES
		int tailX = (frameX + frameWidth) - 30;
		// RESET textY
		textY = frameY + this.gp.getTileSize();
		String value;

		value = String.valueOf(this.gp.getPlayer().getLevel());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getLife() + "/" + this.gp.getPlayer().getMaxLife());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getMana() + "/" + this.gp.getPlayer().getMaxMana());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getStrength());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getDexterity());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getAttack());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getDefense());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getExp());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getNextLevelExp());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getCoin());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += 10;

		textX = tailX - this.gp.getPlayer().getCurrentWeapon().getDown1().getWidth() + 10;
		g2.drawImage(this.gp.getPlayer().getCurrentWeapon().getDown1(), textX, textY, this.gp.getTileSize() - 10,
				this.gp.getTileSize() - 10, null);
		textY += this.gp.getPlayer().getCurrentWeapon().getDown1().getHeight() - 10;

		textX = tailX - this.gp.getPlayer().getCurrentShield().getDown1().getWidth() + 10;
		g2.drawImage(this.gp.getPlayer().getCurrentShield().getDown1(), textX, textY, this.gp.getTileSize() - 10,
				this.gp.getTileSize() - 10, null);

	}

	private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {

		Color black = new Color(0, 0, 0, 210);
		g2.setColor(black);
		g2.fillRoundRect(x, y, width, height, 25, 25);

		Color white = new Color(255, 255, 255, 180);
		g2.setStroke(new BasicStroke(5F));
		g2.setColor(white);
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 15, 15);
	}

	private void drawPlayScreen(Graphics2D g2) {
		// TODO Auto-generated method stub
	}

	public void drawPauseScreen(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

		String text = "PAUSED";
		int x = this.getXTextPositionCenter(g2, text);
		int y = this.gp.getScreenHeight() / 2;

		g2.drawString(text, x, y);
	}

	private int getXTextPositionCenter(Graphics2D g2, String text) {
		int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return this.gp.getScreenWidth() / 2 - textLength / 2;
	}

	private int getXTextPositionAlignToRight(Graphics2D g2, String text, int tailX) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return tailX - length;
	}

	public void addMessage(String text) {
		this.messages.add(text);
		this.messagesCounter.add(0);
	}

	public String getCurrentDialogue() {
		return currentDialogue;
	}

	public void setCurrentDialogue(String currentDialogue) {
		this.currentDialogue = currentDialogue;
	}

	public int getCommandNum() {
		return commandNum;
	}

	public void setCommandNum(int commandNum) {
		this.commandNum = commandNum;
	}

	public int getSlotInventoryCol() {
		return slotInventoryCol;
	}

	public void setSlotInventoryCol(int slotInventoryCol) {
		this.slotInventoryCol = slotInventoryCol;
	}

	public int getSlotInventoryRow() {
		return slotInventoryRow;
	}

	public void setSlotInventoryRow(int slotInventoryRow) {
		this.slotInventoryRow = slotInventoryRow;
	}

	public int getSubState() {
		return subState;
	}

	public void setSubState(int subState) {
		this.subState = subState;
	}

	public static interface SubState {
		public static final int LEVEL_0 = 0;
		public static final int LEVEL_1 = 1;
		public static final int LEVEL_2 = 2;
		public static final int LEVEL_3 = 3;
	}

}
