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

public class UI {

	private Font purisaB, underdog, pressStart;
	private GamePanel gp;
	private String currentDialogue = "";
	private int commandNum = 0;
	private BufferedImage heart_full, heart_half, heart_blank;

	List<String> messages = new ArrayList<>();
	List<Integer> messagesCounter = new ArrayList<>();

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
		}
		}

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
		final int frameHeight = this.gp.getTileSize() * 10;

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
		textY += this.gp.getPlayer().getCurrentShield().getDown1().getHeight() + 5;
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
		textY += this.gp.getPlayer().getCurrentWeapon().getDown1().getHeight() + 5;

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

}
