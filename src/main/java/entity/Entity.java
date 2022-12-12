package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
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
	protected boolean collision;

	private boolean invincible;
	private int invincibleCounter = 0;

	private int actionLockCounter = 0;

	private boolean attacking = false;

	private GamePanel gp;

	private boolean alive = true;
	private boolean dying = false;
	private int dyingCounter = 0;
	private boolean hpBarOn = false;
	private int hpBarCounter = 0;

	// CHARACTER ATTRIBUTES
	private EntityType type;
	private String name;
	private int speed;
	private int maxLife;
	private int life;
	private int level;
	private int strength;
	private int dexterity;
	private int attack;
	private int defense;
	private int exp;
	private int nextLevelExp;
	private int coin;
	private Entity currentWeapon;
	private Entity currentShield;

	// ITEM ATTRIBUTES
	private int attackValue;
	private int defenseValue;

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

	public void damageReaction() {
	}

	public void update() {

		this.setAction();

		this.collisionOn = false;
		this.gp.getCollisionChecker().checkTile(this);
		this.gp.getCollisionChecker().checkObject(this, false);
		this.gp.getCollisionChecker().checkEntity(this, this.gp.getNpcs());
		this.gp.getCollisionChecker().checkEntity(this, this.gp.getMonsters());
		boolean entityContactedPlayer = this.gp.getCollisionChecker().checkPlayer(this);

		if (EntityType.MONSTER.equals(this.type) && entityContactedPlayer) {
			if (!this.gp.getPlayer().isInvincible()) {
				// we can give damage
				this.gp.playSoundEffects(this.gp.getReceiveDamage());
				int damage = this.getAttack() - this.gp.getPlayer().getDefense();
				if (damage < 0) {
					damage = 0;
				}
				this.gp.getPlayer().decreaseLife(damage);
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

			// Monster HP bar
			if (EntityType.MONSTER.equals(this.type) && this.isHpBarOn()) {
				this.checkHealthBar(g2, screenX, screenY);

				this.hpBarCounter++;
				if (this.hpBarCounter > 600) { // 10 secs
					this.hpBarCounter = 0;
					this.hpBarOn = false;
				}
			}

			if (this.isInvincible()) {
				this.hpBarOn = true;
				this.changeAlpha(g2, 0.4F);
			}

			if (this.dying) {
				this.dyingAnimation(g2);
			}

			g2.drawImage(image, screenX, screenY, null);

			// RESET ALPHA
			this.changeAlpha(g2, 1F);
		}
	}

	public void checkHealthBar(Graphics2D g2, int screenX, int screenY) {
		double oneScale = (double) this.getGp().getTileSize() / this.maxLife; // Ex: 48 / 4 = 12.0 -> cada life sera de
		double hpBarValue = oneScale * this.life; // Ex: 12.0 * 1 = 12.0 sera o tamanho da hpBar
		g2.setColor(new Color(35, 35, 35));
		g2.fillRect(screenX - 1, screenY - 16, this.getGp().getTileSize() + 2, 12);
		g2.setColor(new Color(255, 0, 30));
		g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
	}

	private void dyingAnimation(Graphics2D g2) {
		this.dyingCounter++;

		int counter = 5;

		if (this.dyingCounter <= counter) {
			this.changeAlpha(g2, 0F);
		} else if (this.dyingCounter > counter && dyingCounter <= counter * 2) {
			this.changeAlpha(g2, 1F);
		} else if (this.dyingCounter > counter * 2 && dyingCounter <= counter * 3) {
			this.changeAlpha(g2, 0F);
		} else if (this.dyingCounter > counter * 3 && dyingCounter <= counter * 4) {
			this.changeAlpha(g2, 1F);
		} else if (this.dyingCounter > counter * 4 && dyingCounter <= counter * 5) {
			this.changeAlpha(g2, 0F);
		} else if (this.dyingCounter > counter * 5 && dyingCounter <= counter * 6) {
			this.changeAlpha(g2, 1F);
		} else if (this.dyingCounter > counter * 6 && dyingCounter <= counter * 7) {
			this.changeAlpha(g2, 0F);
		} else if (this.dyingCounter > counter * 7 && dyingCounter <= counter * 8) {
			this.changeAlpha(g2, 1F);
		} else if (this.dyingCounter > counter * 8) {
			this.dying = Boolean.FALSE;
			this.alive = Boolean.FALSE;
		}
	}

	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
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
	
	public void decreaseLife(int damage) {
		if ((this.life - damage) < 0) {
			this.life = 0;
		} else {
			this.life -= damage;
		}
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

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}

	public int getDyingCounter() {
		return dyingCounter;
	}

	public void setDyingCounter(int dyingCounter) {
		this.dyingCounter = dyingCounter;
	}

	public boolean isHpBarOn() {
		return hpBarOn;
	}

	public void setHpBarOn(boolean hpBarOn) {
		this.hpBarOn = hpBarOn;
	}

	public int getHpBarCounter() {
		return hpBarCounter;
	}

	public void setHpBarCounter(int hpBarCounter) {
		this.hpBarCounter = hpBarCounter;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getNextLevelExp() {
		return nextLevelExp;
	}

	public void setNextLevelExp(int nextLevelExp) {
		this.nextLevelExp = nextLevelExp;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public Entity getCurrentWeapon() {
		return currentWeapon;
	}

	public void setCurrentWeapon(Entity currentWeapon) {
		this.currentWeapon = currentWeapon;
	}

	public Entity getCurrentShield() {
		return currentShield;
	}

	public void setCurrentShield(Entity currentShield) {
		this.currentShield = currentShield;
	}

	public int getAttackValue() {
		return attackValue;
	}

	public void setAttackValue(int attackValue) {
		this.attackValue = attackValue;
	}

	public int getDefenseValue() {
		return defenseValue;
	}

	public void setDefenseValue(int defenseValue) {
		this.defenseValue = defenseValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT, ANY;
	}

	public static enum EntityType {
		PLAYER, NPC, MONSTER;
	}

}
