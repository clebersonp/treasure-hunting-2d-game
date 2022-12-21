package tile_interactive;

import java.awt.Graphics2D;

import entity.Entity;
import main.GamePanel;

public abstract class InteractiveTile extends Entity {

	private boolean destructible = false;

	public InteractiveTile(GamePanel gp, int col, int row) {
		super(gp);
	}

	public void update() {

	}

	public boolean isCorrectItem(Entity entity) {
		return false;
	}

	public void playSE() {

	}

	@Override
	public void draw(Graphics2D g2) {

		int screenX = this.worldX - this.getGp().getPlayer().getWorldX() + this.getGp().getPlayer().getScreenX();
		int screenY = this.worldY - this.getGp().getPlayer().getWorldY() + this.getGp().getPlayer().getScreenY();

		// desenha o obj somente o tamanho da screen(tela)
		if (this.worldX + this.getGp().getTileSize() > this.getGp().getPlayer().getWorldX()
				- this.getGp().getPlayer().getScreenX()
				&& this.worldX - this.getGp().getTileSize() < this.getGp().getPlayer().getWorldX()
						+ this.getGp().getPlayer().getScreenX()
				&& this.worldY + this.getGp().getTileSize() > this.getGp().getPlayer().getWorldY()
						- this.getGp().getPlayer().getScreenY()
				&& this.worldY - this.getGp().getTileSize() < this.getGp().getPlayer().getWorldY()
						+ this.getGp().getPlayer().getScreenY()) {

			g2.drawImage(this.down1, screenX, screenY, null);
		}
	}

	public InteractiveTile getDestroyedForm() {
		return null;
	}

	public boolean isDestructible() {
		return destructible;
	}

	public void setDestructible(boolean destructible) {
		this.destructible = destructible;
	}

}
