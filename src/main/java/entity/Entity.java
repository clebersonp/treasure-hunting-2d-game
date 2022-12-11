package entity;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

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

	// Entity Attack images
	protected BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1,
			attackRight2;

	// Entity direction, Default Down p objetos statics carregar como a image down
	protected Direction direction = Direction.DOWN;

	protected int spriteCounter = 0;
	protected int sprintNum = 1;

	protected Rectangle solidArea = new Rectangle(new Point(0, 0), new Dimension(48, 48));
	protected Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	protected int solidAreaDefaultX, solidAreaDefaultY;
	protected boolean collisionOn;
	private String[] dialogues = new String[20];
	private int dialogueIndex = 0;

	protected BufferedImage image, image2, image3;
	protected String name;
	protected boolean collision;

	private boolean invincible;
	private int invincibleCounter = 0;

	// CHARACTER LIFE
	private int maxLife;
	private int life;

	private int actionLockCounter = 0;

	private boolean attacking = false;

	protected TYPE type;

	private GamePanel gp;

	public Entity(GamePanel gp) {
		super();
		this.gp = gp;
	}

	public BufferedImage setup(String imagePath, int width, int height) {
		BufferedImage scaledImage = null;
		try {
			scaledImage = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream(imagePath)), width,
					height);
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
		this.gp.getCollisionChecker().checkEntity(this, this.gp.getNpcs());
		this.gp.getCollisionChecker().checkEntity(this, this.gp.getMonsters());
		boolean contactedPlayer = this.gp.getCollisionChecker().checkPlayer(this);

		if (TYPE.MONSTER.equals(this.type) && contactedPlayer) {
			if (!this.gp.getPlayer().isInvincible()) {
				// we can give damage
				this.gp.getPlayer().decreaseLife(1);
				this.gp.getPlayer().setInvincible(Boolean.TRUE);
			}
		}

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

		// This needs to be outside of key if statement!
		if (this.isInvincible()) {
			this.incrementInvincibleCounter();
			if (this.getInvincibleCounter() > 40) { // 40 segundos
				this.setInvincible(Boolean.FALSE);
				this.resetInvincibleCounter();
			}
		}
	}

	protected abstract void loadImages();

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

			BufferedImage image = null;

			image = switch (this.direction) {
			case UP -> this.sprintNum == 1 ? this.up1 : this.up2;
			case DOWN -> this.sprintNum == 1 ? this.down1 : this.down2;
			case LEFT -> this.sprintNum == 1 ? this.left1 : this.left2;
			case RIGHT -> this.sprintNum == 1 ? this.right1 : this.right2;
			default -> throw new IllegalArgumentException("Unexpected value for player direction: " + this.direction);
			};

			if (this.isInvincible()) {
				this.hitEffect(g2);
			}

			g2.drawImage(image, screenX, screenY, null);

			// RESET ALPHA
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

		}

	}

	public void hitEffect(Graphics2D g2) {
		long modResult = System.currentTimeMillis() % 2 + new Random().nextInt(2);
		if (modResult == 2) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4F));
		} else {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8F));
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

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getImage2() {
		return image2;
	}

	public BufferedImage getImage3() {
		return image3;
	}

	public boolean isCollision() {
		return collision;
	}

	public int getActionLockCounter() {
		return actionLockCounter;
	}

	public void setActionLockCounter(int actionLockCounter) {
		this.actionLockCounter = actionLockCounter;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

	public void incrementInvincibleCounter() {
		this.invincibleCounter++;
	}

	public void resetInvincibleCounter() {
		this.invincibleCounter = 0;
	}

	public int getInvincibleCounter() {
		return invincibleCounter;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT, ANY;
	}

	public static enum TYPE {
		PLAYER, NPC, MONSTER;
	}

}
