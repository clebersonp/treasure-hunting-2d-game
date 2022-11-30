package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObject {

	protected BufferedImage image;
	protected String name;
	protected boolean collision;
	protected int worldX, worldY;
	protected Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	protected int solidAreaDefaultX = 0;
	protected int solidAreaDefaultY = 0;

	public void draw(Graphics2D g2, GamePanel gp) {

		int screenX = this.worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
		int screenY = this.worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

		// desenha o obj somente o tamanho da screen(tela)
		if (this.worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX()
				&& this.worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX()
				&& this.worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY()
				&& this.worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {
			g2.drawImage(this.image, screenX, screenY, null);

			// Debug position player
//			System.out.println(String.format("OBJECT_NAME: %s > SCREEN_X: %s, SCREEN_Y: %s, WORLD_X: %s, WORLD_Y: %s",
//					this.name, screenX, screenY, this.worldX, this.worldY));
		}
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public int getWorldX() {
		return worldX;
	}

	public void setWorldX(int worldX) {
		this.worldX = worldX;
	}

	public int getWorldY() {
		return worldY;
	}

	public void setWorldY(int worldY) {
		this.worldY = worldY;
	}

	public BufferedImage getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public Rectangle getSolidArea() {
		return solidArea;
	}

	public int getSolidAreaDefaultX() {
		return solidAreaDefaultX;
	}

	public int getSolidAreaDefaultY() {
		return solidAreaDefaultY;
	}

}
