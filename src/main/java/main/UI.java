package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.InputStream;

public class UI {

	private Font purisaB, underdog;
	private GamePanel gp;
	private String currentDialogue = "";

	public UI(GamePanel gp) {
		this.gp = gp;

		try (InputStream isPurisa = getClass().getResourceAsStream("/fonts/purisa_bold.ttf");
				InputStream isUnderdog = getClass().getResourceAsStream("/fonts/underdog_regular.ttf");) {

			this.purisaB = Font.createFont(Font.TRUETYPE_FONT, isPurisa);
			this.underdog = Font.createFont(Font.TRUETYPE_FONT, isUnderdog);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void draw(Graphics2D g2) {

		g2.setFont(this.underdog);
		g2.setFont(this.purisaB);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);

		switch (this.gp.getGameState()) {
		case GamePanel.PLAY_STATE -> this.drawPlayScreen(g2);
		case GamePanel.PAUSE_STATE -> this.drawPauseScreen(g2);
		case GamePanel.DIALOGUE_STATE -> this.drawDialogScreen(g2);
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
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
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

}
