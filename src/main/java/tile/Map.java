package tile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Map extends TileManager {

	BufferedImage worldMap[];
	private boolean miniMapOn = false;

	public Map(GamePanel gp) {
		super(gp);
		this.createWorldMap();
	}

	public void createWorldMap() {
		this.worldMap = new BufferedImage[this.getGp().getMaxMap()];
		int worldMapWidth = this.getGp().getTileSize() * this.getGp().getMaxWorldCol();
		int worldMapHeight = this.getGp().getTileSize() * this.getGp().getMaxWorldRow();

		for (int i = 0; i < this.getGp().getMaxMap(); i++) {
			this.worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = this.worldMap[i].createGraphics();

			for (int row = 0; row < this.getGp().getMaxWorldRow(); row++) {
				for (int col = 0; col < this.getGp().getMaxWorldCol(); col++) {
					int tileNum = this.getMapTileNum()[i][row][col];
					int x = this.getGp().getTileSize() * col;
					int y = this.getGp().getTileSize() * row;
					g2.drawImage(this.getTiles()[tileNum].getImage(), x, y, null);
				}
			}
		}
	}

	public void drawFullMapScreen(Graphics2D g2) {

		// Background Color
		g2.setColor(Color.black);
		g2.fillRect(0, 0, this.getGp().getScreenWidth(), this.getGp().getScreenWidth());

		// Draw Map
		int width = 600;
		int height = 600;
		int x = this.getGp().getScreenWidth() / 2 - width / 2;
		int y = this.getGp().getScreenHeight() / 2 - height / 2;

		g2.drawImage(this.worldMap[this.getGp().getCurrentMap()], x, y, width, height, null);

		// Draw Player
		double scale = (this.getGp().getTileSize() * this.getGp().getMaxWorldCol()) / width;
		int playerX = (int) (x + this.getGp().getPlayer().getWorldX() / scale);
		int playerY = (int) (y + this.getGp().getPlayer().getWorldY() / scale);
		int playerSize = (int) (this.getGp().getTileSize() / scale);
		g2.drawImage(this.getGp().getPlayer().getDown1(), playerX, playerY, playerSize, playerSize, null);

		// MARK THE PLAYER
		g2.setColor(new Color(220, 10, 10, 100));
		g2.fillOval(playerX - 10, playerY - 10, playerSize + 20, playerSize + 20);

		// Hint
		g2.setFont(this.getGp().getUi().getUnderdog().deriveFont(20f));
		g2.setColor(Color.white);
		g2.drawString("Press M to close!", 790, 550);
	}

	public void drawMiniMap(Graphics2D g2) {

		if (this.miniMapOn) {
			// Draw Map
			int width = 200;
			int height = 200;
			int x = this.getGp().getScreenWidth() - width - 50;
			int y = 50;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
			g2.drawImage(this.worldMap[this.getGp().getCurrentMap()], x, y, width, height, null);

			// Draw Player
			double scale = (this.getGp().getTileSize() * this.getGp().getMaxWorldCol()) / width;
			int playerX = (int) (x + this.getGp().getPlayer().getWorldX() / scale);
			int playerY = (int) (y + this.getGp().getPlayer().getWorldY() / scale);
			int playerSize = (int) (this.getGp().getTileSize() / scale);
			g2.drawImage(this.getGp().getPlayer().getDown1(), playerX, playerY + 1, playerSize, playerSize, null);

			// MARK THE PLAYER
			g2.setColor(new Color(220, 10, 10, 100));
			g2.fillOval(playerX - 5, playerY - 5, playerSize + 10, playerSize + 10);

			// RESET ALPHA
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}

	}

	public BufferedImage[] getWorldMap() {
		return worldMap;
	}

	public void setWorldMap(BufferedImage[] worldMap) {
		this.worldMap = worldMap;
	}

	public boolean isMiniMapOn() {
		return miniMapOn;
	}

	public void setMiniMapOn(boolean miniMapOn) {
		this.miniMapOn = miniMapOn;
	}

}
