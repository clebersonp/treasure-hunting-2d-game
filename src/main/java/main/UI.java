package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.Locale;

import object.OBJ_Key;

public class UI {

	private static final Font ARIAL_40 = new Font("Arial", Font.PLAIN, 40);
	private static final Font ARIAL_80_BOLD = new Font("Arial", Font.BOLD, 80);
	private final OBJ_Key ObjKey;
	private GamePanel gp;
	private boolean messageOn;
	private String message = "";
	private int messageTimer = 0;
	private boolean gameFinished;
	private double playTime = 0.0;
	private DecimalFormat decimalFormat; 

	public UI(GamePanel gp) {
		this.gp = gp;
		ObjKey = new OBJ_Key(gp);
		this.decimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
		this.decimalFormat.applyPattern("#0.00");
	}

	public void showMessage(String text) {
		this.message = text;
		this.messageOn = true;
	}

	public void draw(Graphics2D g2) {

		if (this.gameFinished) {

			g2.setFont(ARIAL_40);
			g2.setColor(Color.WHITE);

			String text;
			int textLength;
			int x, y;

			text = "You found the treasure!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = this.gp.getScreenWidth() / 2 - textLength / 2;
			y = this.gp.getScreenHeight() / 2 - this.gp.getTileSize() * 3;
			g2.drawString(text, x, y);
			
			text = String.format("Your Time is: %s!", this.decimalFormat.format(playTime));
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = this.gp.getScreenWidth() / 2 - textLength / 2;
			y = this.gp.getScreenHeight() / 3 - this.gp.getTileSize() * 3;
			g2.drawString(text, x, y);
			
			g2.setFont(ARIAL_80_BOLD);
			g2.setColor(Color.YELLOW);
			text = "Congratulations!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = this.gp.getScreenWidth() / 2 - textLength / 2;
			y = this.gp.getScreenHeight() / 2 + this.gp.getTileSize() * 2;
			g2.drawString(text, x, y);

			this.gp.finishedGame();

		} else {
			g2.setFont(ARIAL_40);
			g2.setColor(Color.WHITE);
			g2.drawImage(ObjKey.getImage(), this.gp.getTileSize() / 2, this.gp.getTileSize() / 2,
					this.gp.getTileSize(), this.gp.getTileSize(), null);
			g2.drawString(String.format("x %s", this.gp.getPlayer().getHasKey()), 74, 65);

			// TIME
			playTime += (double) 1 / this.gp.getFPS();

			g2.setFont(ARIAL_40);
			g2.setColor(Color.WHITE);
			g2.drawString(String.format("Time: %s", this.decimalFormat.format(playTime)), this.gp.getTileSize() * 11,
					65);

			// MESSAGE
			if (this.messageOn) {

				g2.setFont(g2.getFont().deriveFont(30F));
				g2.drawString(this.message, this.gp.getTileSize() / 2, this.gp.getTileSize() * 5);

				this.messageTimer++;

				if (this.messageTimer > this.gp.getFPS() * 2) {
					this.message = "";
					this.messageTimer = 0;
					this.messageOn = false;
				}
			}
		}

	}

	public void setGameFinished(boolean gameFinished) {
		this.gameFinished = gameFinished;
	}

}
