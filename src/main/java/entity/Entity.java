package entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public abstract class Entity {

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

	protected Rectangle solidArea = new Rectangle(new Point(0, 0), new Dimension(48, 48));
	protected int solidAreaDefaultX, solidAreaDefaultY;
	protected boolean collisionOn;
	private String[] dialogues = new String[20];
	private int dialogueIndex = 0;

	// CHARACTER LIFE
	private int maxLife;
	private int life;

	private GamePanel gp;

	public Entity(GamePanel gp) {
		super();
		this.gp = gp;
	}

	public BufferedImage setup(String imagePath) {
		BufferedImage scaledImage = null;
		try {
			scaledImage = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream(imagePath)),
					this.gp.getTileSize(), this.gp.getTileSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scaledImage;
	}

	public void setAction() {
	}

	public void update() {

		this.setAction();

		this.collisionOn = false;
		this.gp.getCollisionChecker().checkTile(this);
		this.gp.getCollisionChecker().checkObject(this, false);
		this.gp.getCollisionChecker().checkPlayer(this);

		// IF COLLISION IS FALSE< PLAYER CAN MOVE
		if (!collisionOn) {
			switch (this.direction) {
			case UP -> this.worldY -= this.speed;
			case DOWN -> this.worldY += this.speed;
			case LEFT -> this.worldX -= this.speed;
			case RIGHT -> this.worldX += this.speed;
			}
		}

		this.spriteCounter++;
		if (this.spriteCounter > 10) {
			if (this.sprintNum == 1) {
				this.sprintNum++;
			} else if (this.sprintNum == 2) {
				this.sprintNum--;
			}
			this.spriteCounter = 0;
		}
	}

	protected abstract void getImage();

	public void speak() {

		if (this.getDialogues()[this.getDialogueIndex()] == null) {
			this.setDialogueIndex(0);
		}
		this.getGp().getUi().setCurrentDialogue(this.getDialogues()[this.getDialogueIndex()]);
		this.setDialogueIndex(this.getDialogueIndex() + 1);

		switch (this.getGp().getPlayer().getDirection()) {
		case UP -> this.direction = Direction.DOWN;
		case DOWN -> this.direction = Direction.UP;
		case LEFT -> this.direction = Direction.RIGHT;
		case RIGHT -> this.direction = Direction.LEFT;
		}

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

	public GamePanel getGp() {
		return gp;
	}

	public String[] getDialogues() {
		return dialogues;
	}

	public void setDialogues(String[] dialogues) {
		this.dialogues = dialogues;
	}

	public int getDialogueIndex() {
		return dialogueIndex;
	}

	public void setDialogueIndex(int dialogueIndex) {
		this.dialogueIndex = dialogueIndex;
	}

	public BufferedImage getUp1() {
		return up1;
	}

	public BufferedImage getUp2() {
		return up2;
	}

	public BufferedImage getDown1() {
		return down1;
	}

	public BufferedImage getDown2() {
		return down2;
	}

	public BufferedImage getLeft1() {
		return left1;
	}

	public BufferedImage getLeft2() {
		return left2;
	}

	public BufferedImage getRight1() {
		return right1;
	}

	public BufferedImage getRight2() {
		return right2;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT, ANY;
	}

	public abstract void draw(Graphics2D g2);

}
