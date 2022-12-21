package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public class Particle extends Entity {

	private Entity generator;
	private Color color;
	private int size;
	private int xd;
	private int yd;

	public Particle(GamePanel gp, Entity generator, Color color, int size, int speed, int maxLife, int xd, int yd) {
		super(gp);
		this.generator = generator;
		this.color = color;
		this.size = size;
		this.setSpeed(speed);
		this.setMaxLife(maxLife);
		this.setLife(maxLife);
		this.xd = xd;
		this.yd = yd;

		int offset = (gp.getTileSize() / 2) - (size / 2);
		this.worldX = generator.worldX + offset;
		this.worldY = generator.worldY + offset;
	}

	@Override
	protected void loadImages() {

	}

	@Override
	public void update() {
		
		this.decreaseLife(1);
		
		this.worldX += this.xd * this.getSpeed();
		this.worldY += this.yd * this.getSpeed();
		
		if (this.getLife() < this.getMaxLife() / 3) {
			this.yd++;
		}
		
		if (this.getLife() <= 0) {
			this.setAlive(Boolean.FALSE);
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		int screenX = this.worldX - this.getGp().getPlayer().getWorldX() + this.getGp().getPlayer().getScreenX();
		int screenY = this.worldY - this.getGp().getPlayer().getWorldY() + this.getGp().getPlayer().getScreenY();

		g2.setColor(this.color);
		if (this.getLife() < this.getMaxLife() / 3) {
			this.changeAlpha(g2, 0.4F);
		} 
		g2.fillRoundRect(screenX, screenY, this.size, this.size, 1, 1);
		
		// RESET
		this.changeAlpha(g2, 1.0F);
	}

	public Entity getGenerator() {
		return generator;
	}

	public void setGenerator(Entity generator) {
		this.generator = generator;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getXd() {
		return xd;
	}

	public void setXd(int xd) {
		this.xd = xd;
	}

	public int getYd() {
		return yd;
	}

	public void setYd(int yd) {
		this.yd = yd;
	}

}
