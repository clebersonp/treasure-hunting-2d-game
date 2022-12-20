package entity;

import static entity.Entity.Direction.DOWN;
import static entity.Entity.Direction.LEFT;
import static entity.Entity.Direction.RIGHT;
import static entity.Entity.Direction.UP;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.GamePanel;
import main.KeyHandler;
import main.Sound;
import object.OBJ_Axe;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_ShieldWood;
import object.OBJ_SwordNormal;
import tile_interactive.InteractiveTile;

public class Player extends Entity {

	private KeyHandler keyHandler;

	private int screenX;
	private int screenY;
	private boolean attackCancel = false;
	private final List<Entity> inventory = new ArrayList<>();
	private final int maxInventorySize = 20;

	public Player(final GamePanel gp, final KeyHandler keyHandler) {
		super(gp);

		this.keyHandler = keyHandler;

		this.setType(EntityType.PLAYER);

		// posicao do player na screen centralizado
		this.screenX = super.getGp().getScreenWidth() / 2 - (super.getGp().getTileSize() / 2); // 360
		this.screenY = super.getGp().getScreenHeight() / 2 - (super.getGp().getTileSize() / 2); // 264

		// Definir area de colisao do player
		super.solidArea.x = 8;
		super.solidArea.y = 16;
		super.solidAreaDefaultX = super.solidArea.x;
		this.solidAreaDefaultY = super.solidArea.y;
		super.solidArea.width = 32;
		super.solidArea.height = 31;

		this.setDefaultValues();
		this.loadImages();
		this.loadPlayerAttackImages();
		this.setInventoryItems();
	}

	private void setDefaultValues() {

		// posicao do player no world fixa no centro p inicio do game
		super.worldX = super.getGp().getTileSize() * 23; // 1104
		super.worldY = super.getGp().getTileSize() * 21; // 1008

		// Setar em qualquer posicao no mapa para testes
		super.worldX = super.getGp().getTileSize() * 23;
		super.worldY = super.getGp().getTileSize() * 35;

		super.setSpeed(4);
		super.direction = DOWN;

		// PLAYER STATUS
		this.setLevel(1);
		int defaultLife = 6;
		super.setMaxLife(defaultLife);
		super.setLife(defaultLife);
		super.setMaxMana(4);
		super.setMana(super.getMaxMana());
		super.setMaxAmmo(10);
		super.setAmmo(super.getMaxAmmo());
		this.setStrength(1); // The more strength he has, the more damage he gives.
		this.setDexterity(1); // The more dexterity he has, the less damage he receives.
		this.setExp(0);
		this.setNextLevelExp(5);
		this.setCoin(0);
//		this.setCurrentWeapon(new OBJ_SwordNormal(getGp()));
		this.setCurrentWeapon(new OBJ_Axe(getGp()));
		this.setCurrentShield(new OBJ_ShieldWood(getGp()));
		this.setAttack(this.getAttack()); // The total attack value is decided by strength and weapon
		this.setDefense(this.getDefense()); // The total defense value is decided by dexterity and shield
		this.setProjectile(new OBJ_Fireball(getGp()));
//		this.setProjectile(new OBJ_Rock(getGp()));

	}

	public void setInventoryItems() {
		this.inventory.add(this.getCurrentWeapon());
		this.inventory.add(this.getCurrentShield());
		this.inventory.add(new OBJ_Key(this.getGp()));
	}

	public void setAction() {

	}

	public void update() {

		if (this.isAttacking()) {
			this.attacking();
		} else if (this.keyHandler.hasAnyDirectionKeyPressed() || this.keyHandler.isEnterPressed()) {

			if (this.keyHandler.isUpPressed()) {
				super.direction = UP;
			} else if (this.keyHandler.isDownPressed()) {
				super.direction = DOWN;
			} else if (this.keyHandler.isLeftPressed()) {
				super.direction = LEFT;
			} else if (this.keyHandler.isRightPressed()) {
				super.direction = RIGHT;
			}

			// RESET AND CHECK TILE COLLISION AGAIN
			collisionOn = false;
			super.getGp().getCollisionChecker().checkTile(this);

			// CHECK OBJECT COLLISION
			int objectIndex = super.getGp().getCollisionChecker().checkObject(this, true);
			this.pickupObject(objectIndex);

			// CHECK NPCS COLLISION
			int collisionNpcIndex = super.getGp().getCollisionChecker().checkEntity(this, super.getGp().getNpcs());
			this.interactNPC(collisionNpcIndex);

			// CHECK MONSTER COLLISION
			int collisionMonsterIndex = super.getGp().getCollisionChecker().checkEntity(this,
					super.getGp().getMonsters());
			this.contactMonster(collisionMonsterIndex);

			// CHECK INTERACTIVE TILES COLLISION
			super.getGp().getCollisionChecker().checkEntity(this, this.getGp().getInteractiveTiles());

			// CHECK EVENT
			this.getGp().getEventHandler().checkEvent();

			// IF COLLISION IS FALSE AND ENTER KEY IS NOT PRESSED, PLAYER CAN MOVE
			if (!collisionOn && !this.keyHandler.isEnterPressed()) {
				switch (super.direction) {
				case UP:
					super.worldY -= super.getSpeed();
					break;
				case DOWN:
					super.worldY += super.getSpeed();
					break;
				case LEFT:
					super.worldX -= super.getSpeed();
					break;
				case RIGHT:
					super.worldX += super.getSpeed();
					break;
				}
			}

			if (this.keyHandler.isEnterPressed() && !this.attackCancel) {
				this.getGp().playSoundEffects(new Sound(Sound.SWING_WEAPON));
				this.setAttacking(Boolean.TRUE);
				this.spriteCounter = 0;
			}

			this.attackCancel = false;

			// Dps de checkar nos eventos a key, resetar
			this.keyHandler.setEnterPressed(Boolean.FALSE);

			super.spriteCounter++;
			if (super.spriteCounter > 10) {
				if (super.sprintNum == 1) {
					super.sprintNum++;
				} else if (super.sprintNum == 2) {
					super.sprintNum--;
				}
				super.spriteCounter = 0;
			}

		}

		// PROJECTILE
		if (this.getGp().getKeyHandler().isShotKeyPressed() && !this.getProjectile().isAlive()
				&& this.getShotAvailableCounter() == 40 && this.getProjectile().haveResource(this)) {
			// SET DEFAULT COORDINATES, DIRECTION AND USER
			this.getProjectile().set(this.worldX, this.worldY, this.direction, Boolean.TRUE, this);

			// ADD IT TO THE LIST
			this.getGp().getProjectiles().add(this.getProjectile());
			new Sound(Sound.BURNING).play();
			this.setShotAvailableCounter(0);

			// DECREASE THE PLAYER MANA
			this.getProjectile().subtractResource(this);
		}

		// This needs to be outside of key if statement!
		if (this.isInvincible()) {
			this.incrementInvincibleCounter();
			if (this.getInvincibleCounter() > 60) { // 60 segundos
				this.setInvincible(Boolean.FALSE);
				this.resetInvincibleCounter();
			}
		}

		// COUNTER FOR SHOT A NEW PROJECTILE
		if (this.getShotAvailableCounter() < 40) {
			this.setShotAvailableCounter(this.getShotAvailableCounter() + 1);
		}
	}

	private void attacking() {
		super.spriteCounter++;
		if (super.spriteCounter <= 5) {
			super.sprintNum = 1;
		}
		if (super.spriteCounter > 5 && super.spriteCounter <= 25) {
			super.sprintNum = 2;

			// save current information
			int currentWorldX = this.worldX;
			int currentWorldY = this.worldY;
			int currentSolidAreaWidth = this.solidArea.width;
			int currentSolidAreaHeight = this.solidArea.height;

			// Adjust player's worldX/Y for the attackArea collision
			switch (this.direction) {
			case UP -> this.worldY -= this.attackArea.height;
			case DOWN -> this.worldY += this.attackArea.height;
			case LEFT -> this.worldX -= this.attackArea.width;
			case RIGHT -> this.worldX += this.attackArea.width;
			}

			// attackAtea becomes solidArea
			this.solidArea.width = this.attackArea.width;
			this.solidArea.height = this.attackArea.height;

			// check monster collision with the updated worldX/Y and solidArea
			int monsterIndex = this.getGp().getCollisionChecker().checkEntity(this, this.getGp().getMonsters());
			this.damageMonster(monsterIndex, this.getAttack());

			// CHECK INTERACTIVE TILE COLLISON FOR INTERACT TO IT
			int interactiveTitleIndex = this.getGp().getCollisionChecker().checkEntity(this,
					this.getGp().getInteractiveTiles());
			this.damageInteractiveTile(interactiveTitleIndex);

			// Reset worldX/Y and solidArea of player
			this.worldX = currentWorldX;
			this.worldY = currentWorldY;
			this.solidArea.x = currentSolidAreaWidth;
			this.solidArea.y = currentSolidAreaHeight;

		}
		if (super.spriteCounter > 25) {
			super.sprintNum = 1;
			super.spriteCounter = 0;
			this.setAttacking(Boolean.FALSE);
		}
	}

	public void damageMonster(int index, int attack) {
		if (index >= 0) {
			final Entity[] monsters = this.getGp().getMonsters();
			final Entity monster = monsters[index];
			if (!monster.isInvincible()) {
				this.getGp().playSoundEffects(new Sound(Sound.HIT_MONSTER));

				int damage = attack - monster.getDefense();
				if (damage < 0) {
					damage = 0;
				}

				monster.decreaseLife(damage);

				this.getGp().getUi().addMessage(damage + " damage!");

				monster.setInvincible(Boolean.TRUE);
				monster.damageReaction();

				if (monster.getLife() <= 0) {
					monster.setDying(Boolean.TRUE);
					this.getGp().getUi().addMessage("Killed the " + monster.getName() + "!");
					this.getGp().getUi().addMessage("Exp + " + monster.getExp());
					this.setExp(this.getExp() + monster.getExp());
					this.checkLevelUp();
				}
			}
		}
	}

	public void damageInteractiveTile(int interactiveTileIndex) {
		if (interactiveTileIndex >= 0 && this.getGp().getInteractiveTiles()[interactiveTileIndex].isDestructible()
				&& this.getGp().getInteractiveTiles()[interactiveTileIndex].isCorrectItem(this)
				&& !this.getGp().getInteractiveTiles()[interactiveTileIndex].isInvincible()) {

			InteractiveTile interactiveTile = this.getGp().getInteractiveTiles()[interactiveTileIndex];

			interactiveTile.decreaseLife(1);
			interactiveTile.playSE();
			interactiveTile.setInvincible(Boolean.TRUE);

			if (interactiveTile.getLife() <= 0) {
				this.getGp().getInteractiveTiles()[interactiveTileIndex] = interactiveTile.getDestroyedForm();
			}

		}
	}

	public void checkLevelUp() {
		if (this.getExp() >= this.getNextLevelExp()) {
			this.setLevel(this.getLevel() + 1);
			this.setNextLevelExp(this.getNextLevelExp() * 2);
			this.setMaxLife(this.getMaxLife() + 2);
			this.setStrength(this.getStrength() + 1);
			this.setDexterity(this.getDexterity() + 1);
			this.setAttack(this.getAttack());
			this.setDefense(this.getDefense());
			this.setMaxMana(this.getMaxMana() + 1);

			this.getGp().playSoundEffects(new Sound(Sound.LEVEL_UP));
			this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
			this.getGp().getUi()
					.setCurrentDialogue("You are level " + this.getLevel() + " now!\n" + "You feel stronger!");
		}
	}

	public void selectInventorySlotItem() {
		int currentItemIndexOnSlot = this.getGp().getUi().getInventoryItemIndexOnSlot();

		if (currentItemIndexOnSlot < this.inventory.size()) {
			Entity selectedItem = this.inventory.get(currentItemIndexOnSlot);

			if (EntityType.SWORD.equals(selectedItem.getType()) || EntityType.AXE.equals(selectedItem.getType())) {
				this.setCurrentWeapon(selectedItem);
				this.setAttack(this.getAttack());
				this.loadPlayerAttackImages();
			}

			if (EntityType.SHIELD.equals(selectedItem.getType())) {
				this.setCurrentShield(selectedItem);
				this.setDefense(this.getDefense());
			}

			if (EntityType.CONSUMABLE.equals(selectedItem.getType())) {
				boolean itemUsed = selectedItem.use(this);
				if (itemUsed) {
					this.inventory.remove(selectedItem);
				}
			}
		}
	}

	private void contactMonster(int collisionMonsterIndex) {
		if (collisionMonsterIndex >= 0 && !this.isInvincible()
				&& !this.getGp().getMonsters()[collisionMonsterIndex].isDying()) {
			int damage = this.getGp().getMonsters()[collisionMonsterIndex].getAttack() - this.getDefense();
			if (damage < 0) {
				damage = 0;
			}
			this.decreaseLife(damage);
			this.getGp().playSoundEffects(new Sound(Sound.RECEIVE_DAMAGE));
			this.setInvincible(Boolean.TRUE);
		}
	}

	private void interactNPC(int collisionNpcIndex) {
		if (this.getGp().getKeyHandler().isEnterPressed()) {
			if (collisionNpcIndex >= 0) {
				this.attackCancel = true;
				this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
				this.getGp().getNpcs()[collisionNpcIndex].speak();
			}
		}
	}

	public void pickupObject(int objectIndex) {
		if (objectIndex >= 0) {

			// PICKUP ONLY ITEMS
			if (EntityType.PICKUP_ONLY.equals(this.getGp().getObjects()[objectIndex].getType())) {

				boolean isItemUsed = this.getGp().getObjects()[objectIndex].use(this);
				if (isItemUsed) {
					this.getGp().getObjects()[objectIndex] = null; // se for usado o item, entao sera removido do mapa
				}

			} else {

				// INVENTORY ITEMS
				String text;
				if (this.inventory.size() < this.maxInventorySize) {

					Entity object = this.getGp().getObjects()[objectIndex];
					this.inventory.add(object);
					new Sound(Sound.COIN).play();
					text = "Got a " + object.getName() + "!";

					// Remove the object from the map
					this.getGp().getObjects()[objectIndex] = null;

				} else {
					text = "You cannot carry any more!";
				}
				this.getGp().getUi().addMessage(text);
			}
		}
	}

	@Override
	public void draw(Graphics2D g2) {

		// TEST
		// g2.setColor(Color.YELLOW);
		// g2.fillRect(super.x, super.y, this.gp.getTileSize(), this.gp.getTileSize());

		BufferedImage image = null;
		int tempScreenX = this.screenX;
		int tempScreenY = this.screenY;

		image = switch (super.direction) {
		case UP -> {
			if (this.isAttacking()) {
				tempScreenY -= this.getGp().getTileSize();
				yield this.sprintNum == 1 ? this.attackUp1 : this.attackUp2;
			} else {
				yield this.sprintNum == 1 ? this.up1 : super.up2;
			}
		}
		case DOWN -> {
			if (this.isAttacking()) {
				yield super.sprintNum == 1 ? super.attackDown1 : super.attackDown2;
			} else {
				yield super.sprintNum == 1 ? super.down1 : super.down2;
			}
		}
		case LEFT -> {
			if (this.isAttacking()) {
				tempScreenX -= this.getGp().getTileSize();
				yield super.sprintNum == 1 ? super.attackLeft1 : super.attackLeft2;
			} else {
				yield super.sprintNum == 1 ? super.left1 : super.left2;
			}
		}
		case RIGHT -> {
			if (this.isAttacking()) {
				yield super.sprintNum == 1 ? super.attackRight1 : super.attackRight2;
			} else {
				yield super.sprintNum == 1 ? super.right1 : super.right2;
			}
		}
		default -> throw new IllegalArgumentException("Unexpected value for player direction: " + super.direction);
		};

		if (this.isInvincible()) {
			this.hitEffect(g2);
		}

		g2.drawImage(image, tempScreenX, tempScreenY, null);

		// RESET ALPHA
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

		// Debug player damage
//		g2.setFont(new Font("Arial", Font.PLAIN, 26));
//		g2.setColor(Color.WHITE);
//		g2.drawString(String.format("Invincible Counter: %d", this.getInvincibleCounter()), 10, 400);

		// Debug position player
//		System.out.println(String.format("PLAYER > SCREEN_X: %s, SCREEN_Y: %s, WORLD_X: %s, WORLD_Y: %s", this.screenX,
//				this.screenY, super.worldX, super.worldY));

		// debug collision area
//		g2.setColor(new Color(0, 0, 255, 100));
//		g2.fillRect(this.screenX + 8, this.screenY + 16, 32, 31);

	}

	public void hitEffect(Graphics2D g2) {
		long modResult = System.currentTimeMillis() % 2 + new Random().nextInt(2);
		if (modResult == 2) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
		} else {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
		}
	}

	public void loadImages() {
		super.up1 = this.setup("/player/boy_up_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.up2 = this.setup("/player/boy_up_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.right1 = this.setup("/player/boy_right_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.right2 = this.setup("/player/boy_right_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.down1 = this.setup("/player/boy_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.down2 = this.setup("/player/boy_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.left1 = this.setup("/player/boy_left_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.left2 = this.setup("/player/boy_left_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	public void loadPlayerAttackImages() {

		if (EntityType.SWORD.equals(this.getCurrentWeapon().getType())) {
			this.attackUp1 = this.setup("/player/boy_attack_up_1.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackUp2 = this.setup("/player/boy_attack_up_2.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackDown1 = this.setup("/player/boy_attack_down_1.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackDown2 = this.setup("/player/boy_attack_down_2.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackLeft1 = this.setup("/player/boy_attack_left_1.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
			this.attackLeft2 = this.setup("/player/boy_attack_left_2.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
			this.attackRight1 = this.setup("/player/boy_attack_right_1.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
			this.attackRight2 = this.setup("/player/boy_attack_right_2.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
		}
		if (EntityType.AXE.equals(this.getCurrentWeapon().getType())) {
			this.attackUp1 = this.setup("/player/boy_axe_up_1.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackUp2 = this.setup("/player/boy_axe_up_2.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackDown1 = this.setup("/player/boy_axe_down_1.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackDown2 = this.setup("/player/boy_axe_down_2.png", this.getGp().getTileSize(),
					this.getGp().getTileSize() * 2);
			this.attackLeft1 = this.setup("/player/boy_axe_left_1.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
			this.attackLeft2 = this.setup("/player/boy_axe_left_2.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
			this.attackRight1 = this.setup("/player/boy_axe_right_1.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
			this.attackRight2 = this.setup("/player/boy_axe_right_2.png", this.getGp().getTileSize() * 2,
					this.getGp().getTileSize());
		}

	}

	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public boolean isAttackCancel() {
		return attackCancel;
	}

	public void setAttackCancel(boolean attackCancel) {
		this.attackCancel = attackCancel;
	}

	@Override
	public int getAttack() {
		this.attackArea = this.getCurrentWeapon().getAttackArea();
		return this.getStrength() * this.getCurrentWeapon().getAttackValue();
	}

	@Override
	public int getDefense() {
		return this.getDexterity() * this.getCurrentShield().getDefenseValue();
	}

	public void decreaseLife(int damage) {
		if ((this.getLife() - damage) < 0) {
			this.setLife(0);
		} else {
			this.setLife(this.getLife() - damage);
		}
	}

	public void resetUpLife(int life) {
		this.setLife(life);
	}

	public List<Entity> getInventory() {
		return inventory;
	}

	public int getMaxInventorySize() {
		return maxInventorySize;
	}

}
