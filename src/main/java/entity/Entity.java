package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

	// Entity position
	protected int worldX, worldY;

	// Entity speed
	protected int speed;

	// Entity Images
	protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

	// Entity direction
	protected Direction direction;

	protected int spriteCounter = 0;
	protected int sprintNum = 1;

	protected Rectangle solidArea;
	protected int solidAreaDefaultX, solidAreaDefaultY;
	protected boolean collisionOn;

	public int getWorldX() {
		return worldX;
	}

	public int getWorldY() {
		return worldY;
	}

	public boolean isCollisionOn() {
		return collisionOn;
	}

	public void setCollisionOn(boolean collisionOn) {
		this.collisionOn = collisionOn;
	}

	public Rectangle getSolidArea() {
		return solidArea;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getSpeed() {
		return speed;
	}

	public int getSolidAreaDefaultX() {
		return solidAreaDefaultX;
	}

	public int getSolidAreaDefaultY() {
		return solidAreaDefaultY;
	}

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}

}
