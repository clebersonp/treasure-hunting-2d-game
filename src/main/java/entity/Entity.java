package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import ai.PathFinder;
import main.GamePanel;
import main.Sound;
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

	protected int sprintNum = 1;

	protected Rectangle solidArea = new Rectangle(new Point(0, 0), new Dimension(48, 48));
	protected Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	protected int solidAreaDefaultX, solidAreaDefaultY;
	protected boolean collisionOn;
	private String[] dialogues = new String[20];
	private int dialogueIndex = 0;

	protected BufferedImage image, image2, image3;
	private boolean collision;

	private boolean invincible;

	// COUNTER
	private int invincibleCounter = 0;
	private int actionLockCounter = 0;
	private int dyingCounter = 0;
	protected int spriteCounter = 0;
	private int shotAvailableCounter = 0;
	private int knockBackCounter = 0;

	private boolean attacking = false;

	private GamePanel gp;

	private boolean alive = true;
	private boolean dying = false;
	private boolean hpBarOn = false;
	private int hpBarCounter = 0;
	private boolean onPath = false;
	private boolean knockBack = false;

	// CHARACTER ATTRIBUTES
	private EntityType type;
	private String name;
	private int defaultSpeed;
	private int speed;
	private int maxLife;
	private int life;
	private int maxMana;
	private int mana;
	private int ammo;
	private int maxAmmo;
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
	private Entity currentLight;
	private Projectile projectile;

	// ITEM ATTRIBUTES
	private int attackValue;
	private int defenseValue;
	private String description = "";
	private int useCost;
	private int value;
	private int price;
	private int knockBackPower = 0;
	private boolean stackable = false;
	private int amount = 1;
	private int lightRadius;

	// INVENTORY
	private final List<Entity> inventory = new ArrayList<>();
	private final int maxInventorySize = 20;

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

	public void checkCollision() {
		this.collisionOn = false;
		this.gp.getCollisionChecker().checkTile(this);
		this.gp.getCollisionChecker().checkObject(this, false);
		this.gp.getCollisionChecker().checkEntity(this, this.gp.getNpcs());
		this.gp.getCollisionChecker().checkEntity(this, this.gp.getMonsters());
		this.gp.getCollisionChecker().checkEntity(this, this.gp.getInteractiveTiles());
		boolean entityContactedPlayer = this.gp.getCollisionChecker().checkPlayer(this);

		if (EntityType.MONSTER.equals(this.type) && entityContactedPlayer) {
			this.damagePlayer(this.attack);
		}
	}

	public void update() {

		if (this.knockBack) {

			this.checkCollision();

			if (this.isCollisionOn()) {
				this.knockBackCounter = 0;
				this.knockBack = false;
				this.speed = this.defaultSpeed;
			} else {
				switch (this.gp.getPlayer().getDirection()) {
				case UP -> this.worldY -= this.speed;
				case DOWN -> this.worldY += this.speed;
				case LEFT -> this.worldX -= this.speed;
				case RIGHT -> this.worldX += this.speed;
				}
			}

			this.knockBackCounter++;
			if (this.knockBackCounter == 10) {
				this.knockBackCounter = 0;
				this.knockBack = false;
				this.speed = this.defaultSpeed;
			}

		} else {
			this.setAction();

			this.checkCollision();

			// IF COLLISION IS FALSE< PLAYER CAN MOVE
			if (!collisionOn) {
				switch (this.direction) {
				case UP -> this.worldY -= this.speed;
				case DOWN -> this.worldY += this.speed;
				case LEFT -> this.worldX -= this.speed;
				case RIGHT -> this.worldX += this.speed;
				}
			}
		}

		this.spriteCounter++;
		if (this.spriteCounter > 25) {
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

		// COUNTER FOR SHOT A NEW PROJECTILE
		if (this.getShotAvailableCounter() < 40) {
			this.setShotAvailableCounter(this.getShotAvailableCounter() + 1);
		}
	}

	public void damagePlayer(int attack) {
		if (!this.gp.getPlayer().isInvincible()) {
			// we can give damage
			new Sound(Sound.RECEIVE_DAMAGE, false).play();
			int damage = this.getAttack() - this.gp.getPlayer().getDefense();
			if (damage < 0) {
				damage = 0;
			}
			this.gp.getPlayer().decreaseLife(damage);
			this.gp.getPlayer().setInvincible(Boolean.TRUE);
		}
	}

	protected abstract void loadImages();

	public void speak() {

		if (this.getDialogueIndex() >= this.getDialogues().length
				|| this.getDialogues()[this.getDialogueIndex()] == null) {
			this.setDialogueIndex(0);
		}
		this.getGp().getUi().setCurrentDialogue(this.getDialogues()[this.getDialogueIndex()]);
		this.setDialogueIndex(this.getDialogueIndex() + 1);

	}

	public void interact() {

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
			this.alive = Boolean.FALSE;
		}
	}

	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}

	public boolean use(Entity entity) {
		return false;
	}

	public int getDetected(Entity user, Entity[][] target, String targetName) {
		int objIndex = -1;

		// Check the surrounding object
		int nextWorldX = user.getLeftX();
		int nextWorldY = user.getTopY();

		switch (user.getDirection()) {
		case UP -> nextWorldY = user.getTopY() - 1;
		case DOWN -> nextWorldY = user.getBottomY() + 1;
		case LEFT -> nextWorldX = user.getLeftX() - 1;
		case RIGHT -> nextWorldX = user.getRightX() + 1;
		}

		int col = nextWorldX / this.gp.getTileSize();
		int row = nextWorldY / this.gp.getTileSize();

		for (int i = 0; i < target[this.gp.getCurrentMap()].length; i++) {
			Entity entityTarget = target[this.gp.getCurrentMap()][i];
			if (Objects.nonNull(entityTarget)) {
				if (entityTarget.getCol() == col && entityTarget.getRow() == row
						&& entityTarget.getName().equalsIgnoreCase(targetName)) {

					objIndex = i;
					break;
				}
			}
		}

		return objIndex;
	}

	public void checkDrop() {

	}

	public void dropItem(Entity droppedItem) {
		for (int i = 0; i < this.gp.getObjects()[this.gp.getCurrentMap()].length; i++) {
			if (Objects.isNull(this.gp.getObjects()[this.gp.getCurrentMap()][i])) {
				this.gp.getObjects()[this.gp.getCurrentMap()][i] = droppedItem;
				this.gp.getObjects()[this.gp.getCurrentMap()][i].worldX = this.worldX; // the dead monster's worldX
				this.gp.getObjects()[this.gp.getCurrentMap()][i].worldY = this.worldY; // the dead monster's worldY
				break;
			}
		}
	}

	public Color getParticleColor() {
		return null;
	}

	public int getParticleSize() {
		return 0; // pixels
	}

	public int getParticleSpeed() {
		return 0;
	}

	public int getParticleMaxLife() {
		return 0;
	}

	public void generateParticle(Entity generator, Entity target) {

		final Color particleColor = generator.getParticleColor();
		final int particleMaxLife = generator.getParticleMaxLife();
		final int particleSize = generator.getParticleSize();
		final int particleSpeed = generator.getParticleSpeed();

		final Particle p1 = new Particle(this.gp, target, particleColor, particleSize, particleSpeed, particleMaxLife,
				-2, -1);
		final Particle p2 = new Particle(this.gp, target, particleColor, particleSize, particleSpeed, particleMaxLife,
				2, -1);
		final Particle p3 = new Particle(this.gp, target, particleColor, particleSize, particleSpeed, particleMaxLife,
				-2, 1);
		final Particle p4 = new Particle(this.gp, target, particleColor, particleSize, particleSpeed, particleMaxLife,
				2, 1);
		this.gp.getParticles().add(p1);
		this.gp.getParticles().add(p2);
		this.gp.getParticles().add(p3);
		this.gp.getParticles().add(p4);

	}

	public void searchPath(int goalRow, int goalCol) {
		int startRow = (this.worldY + this.solidArea.y) / this.gp.getTileSize();
		int startCol = (this.worldX + this.solidArea.x) / this.gp.getTileSize();

		final PathFinder pathFinder = this.gp.getPathFinder();
		pathFinder.setNodes(startRow, startCol, goalRow, goalCol);

		// If find the path
		if (pathFinder.search() && !pathFinder.getPathList().isEmpty()) {

			// Next WorldX & WorldY
			int nextY = pathFinder.getPathList().get(0).getRow() * this.gp.getTileSize();
			int nextX = pathFinder.getPathList().get(0).getCol() * this.gp.getTileSize();

			// Entity's solidArea position(collision)
			int entityLeftX = this.worldX + this.solidArea.x;
			int entityRightX = this.worldX + this.solidArea.x + this.solidArea.width;
			int entityTopY = this.worldY + this.solidArea.y;
			int entityBottomY = this.worldY + this.solidArea.y + this.solidArea.height;

			// Define the entity's directions
			if (entityTopY > nextY && entityLeftX >= nextX && entityRightX < nextX + this.gp.getTileSize()) {
				this.direction = Direction.UP;
			} else if (entityTopY < nextY && entityLeftX >= nextX && entityRightX < nextX + this.gp.getTileSize()) {
				this.direction = Direction.DOWN;
			} else if (entityTopY >= nextY && entityBottomY < nextY + this.gp.getTileSize()) {
				// left or right
				if (entityLeftX > nextX) {
					this.direction = Direction.LEFT;
				} else if (entityLeftX < nextX) {
					this.direction = Direction.RIGHT;
				}
			} else if (entityTopY > nextY && entityLeftX > nextX) {
				// Up or Left
				this.direction = Direction.UP;
				this.checkCollision();
				if (this.collisionOn) {
					this.direction = Direction.LEFT;
				}
			} else if (entityTopY > nextY && entityLeftX < nextX) {
				// Up or Right
				this.direction = Direction.UP;
				this.checkCollision();
				if (this.collisionOn) {
					this.direction = Direction.RIGHT;
				}
			} else if (entityTopY < nextY && entityLeftX < nextX) {
				// Down or Right
				this.direction = Direction.DOWN;
				this.checkCollision();
				if (this.collisionOn) {
					this.direction = Direction.RIGHT;
				}
			} else if (entityTopY < nextY && entityLeftX > nextX) {
				// Down or Left
				this.direction = Direction.DOWN;
				this.checkCollision();
				if (this.collisionOn) {
					this.direction = Direction.LEFT;
				}
			}

			// If reaches the goal, stop the search
//			int nextRow = pathFinder.getPathList().get(0).getRow();
//			int nextCol = pathFinder.getPathList().get(0).getCol();
//			
//			if(nextRow == goalRow && nextCol == goalCol) {
//				this.onPath = false;
//			}
		}
	}

	public int getLeftX() {
		return this.worldX + this.solidArea.x;
	}

	public int getRightX() {
		return this.getLeftX() + this.solidArea.width;
	}

	public int getTopY() {
		return this.worldY + this.solidArea.y;
	}

	public int getBottomY() {
		return this.getTopY() + this.solidArea.height;
	}

	public int getCol() {
		return this.getLeftX() / this.gp.getTileSize();
	}

	public int getRow() {
		return this.getTopY() / this.gp.getTileSize();
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
		if (this.life <= 0) {
			this.life = 0;
			this.alive = false;
		} else if (this.life >= this.maxLife) {
			this.life = this.maxLife;
			this.alive = true;
		}
	}

	public void decreaseLife(int damage) {
		this.life -= damage;
		if (this.life <= 0) {
			this.life = 0;
			this.alive = false;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Rectangle getAttackArea() {
		return attackArea;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
		if (this.mana <= 0) {
			this.mana = 0;
		} else if (this.mana >= this.maxMana) {
			this.mana = this.maxMana;
		}
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public int getUseCost() {
		return useCost;
	}

	public void setUseCost(int useCost) {
		this.useCost = useCost;
	}

	public int getShotAvailableCounter() {
		return shotAvailableCounter;
	}

	public void setShotAvailableCounter(int shotAvailableCounter) {
		this.shotAvailableCounter = shotAvailableCounter;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}

	public void setMaxAmmo(int maxAmmo) {
		this.maxAmmo = maxAmmo;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public List<Entity> getInventory() {
		return inventory;
	}

	public int getMaxInventorySize() {
		return maxInventorySize;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isOnPath() {
		return onPath;
	}

	public void setOnPath(boolean onPath) {
		this.onPath = onPath;
	}

	public boolean isKnockBack() {
		return knockBack;
	}

	public void setKnockBack(boolean knockBack) {
		this.knockBack = knockBack;
	}

	public int getDefaultSpeed() {
		return defaultSpeed;
	}

	public void setDefaultSpeed(int defaultSpeed) {
		this.defaultSpeed = defaultSpeed;
	}

	public int getKnockBackCounter() {
		return knockBackCounter;
	}

	public void setKnockBackCounter(int knockBackCounter) {
		this.knockBackCounter = knockBackCounter;
	}

	public int getKnockBackPower() {
		return knockBackPower;
	}

	public void setKnockBackPower(int knockBackPower) {
		this.knockBackPower = knockBackPower;
	}

	public boolean isStackable() {
		return stackable;
	}

	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Entity getCurrentLight() {
		return currentLight;
	}

	public void setCurrentLight(Entity currentLight) {
		this.currentLight = currentLight;
	}

	public int getLightRadius() {
		return lightRadius;
	}

	public void setLightRadius(int lightRadius) {
		this.lightRadius = lightRadius;
	}

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT, ANY;
	}

	public static enum EntityType {
		PLAYER, NPC, MONSTER, SWORD, AXE, SHIELD, CONSUMABLE, PICKUP_ONLY, OBSTACLE, LIGHT;
	}

}
