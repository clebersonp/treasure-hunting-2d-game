package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import object.OBJ_Heart;
import object.SuperObject;

public class UI {

	private Font purisaB, underdog, pressStart;
	private GamePanel gp;
	private String currentDialogue = "";
	private int commandNum = 0;
	private BufferedImage heart_full, heart_half, heart_blank;

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
		SuperObject obj_Heart = new OBJ_Heart(this.gp);
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
		}
		case GamePanel.PAUSE_STATE -> {
			this.drawPlayerLife(g2);
			this.drawPauseScreen(g2);
		}
		case GamePanel.DIALOGUE_STATE -> {
			this.drawPlayerLife(g2);
			this.drawDialogScreen(g2);
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
	}

	private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {

		Color black = new Color(0, 0, 0, 210);
		g2.setColor(black);
		g2.fillRoundRect(x, y, width, height, 25, 25);

		Color white = new Color(255, 255, 255, 180);
		g2.setStroke(new BasicStroke(5F));
		g2.setColor(white);
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 15, 15);

		x += this.gp.getTileSize();
		y += this.gp.getTileSize();
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		g2.setColor(Color.WHITE);
		for (String line : this.currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}

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
