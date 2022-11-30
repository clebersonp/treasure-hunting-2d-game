package entity;

import static entity.Entity.Direction.DOWN;
import static entity.Entity.Direction.LEFT;
import static entity.Entity.Direction.RIGHT;
import static entity.Entity.Direction.UP;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity {

	private GamePanel gp;
	private KeyHandler keyHandler;

	private final int screenX;
	private final int screenY;
	private int hasKey = 0;

	public Player(final GamePanel gp, final KeyHandler keyHandler) {
		this.gp = gp;
		this.keyHandler = keyHandler;

		// posicao do player na screen centralizado
		this.screenX = this.gp.getScreenWidth() / 2 - (this.gp.getTileSize() / 2); // 360
		this.screenY = this.gp.getScreenHeight() / 2 - (this.gp.getTileSize() / 2); // 264

		// Definir area de colisao do player
		super.solidArea = new Rectangle();
		super.solidArea.x = 8;
		super.solidArea.y = 16;
		super.solidAreaDefaultX = super.solidArea.x;
		this.solidAreaDefaultY = super.solidArea.y;
		super.solidArea.width = 32;
		super.solidArea.height = 31;

		this.setDefaultValues();
		this.getPlayerImage();
	}

	private void setDefaultValues() {

		// posicao do player no world fixa no centro p inicio do game
		super.worldX = this.gp.getTileSize() * 23; // 1104
		super.worldY = this.gp.getTileSize() * 21; // 1008

		super.speed = 4;
		super.direction = DOWN;

	}

	public void update() {

		if (this.keyHandler.hasAnyDirectionKeyPressed()) {

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
			this.gp.getCollisionChecker().checkTile(this);

			// CHECK OBJECT COLLISION
			int objectIndex = this.gp.getCollisionChecker().checkObject(this, true);
			this.pickupObject(objectIndex);

			// IF COLLISION IS FALSE< PLAYER CAN MOVE
			if (!collisionOn) {
				switch (super.direction) {
				case UP:
					super.worldY -= super.speed;
					break;
				case DOWN:
					super.worldY += super.speed;
					break;
				case LEFT:
					super.worldX -= super.speed;
					break;
				case RIGHT:
					super.worldX += super.speed;
					break;
				}
			}

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
	}

	public void pickupObject(int objectIndex) {
		if (objectIndex >= 0) {
			String objectName = this.gp.getObjects()[objectIndex].getName();

			switch (objectName) {
			case "Key":
				this.gp.playSoundEffects(1); // coin sound effect
				hasKey++;
				this.gp.getObjects()[objectIndex] = null;
				this.gp.getUi().showMessage("You got a key!");
				break;
			case "Door":
				if (hasKey > 0) {
					this.gp.playSoundEffects(3); // door open sound effect
					this.gp.getObjects()[objectIndex] = null;
					hasKey--;
					this.gp.getUi().showMessage("You opened the door!");
				} else {
					this.gp.getUi().showMessage("You need a key!");
				}
				break;
			case "Boots":
				this.gp.playSoundEffects(2); // boots sound effect
				this.speed += 1;
				this.gp.getObjects()[objectIndex] = null;
				this.gp.getUi().showMessage("Speed up!");
				break;
			case "Chest":
				this.gp.stopMusic();
				this.gp.getUi().setGameFinished(Boolean.TRUE);
				this.gp.playSoundEffects(4); // Fanfare
				break;
			}
		}
	}

	public void draw(Graphics2D g2) {

		// TEST
		// g2.setColor(Color.YELLOW);
		// g2.fillRect(super.x, super.y, this.gp.getTileSize(), this.gp.getTileSize());

		BufferedImage image = null;

		image = switch (super.direction) {
		case UP -> super.sprintNum == 1 ? super.up1 : super.up2;
		case DOWN -> super.sprintNum == 1 ? super.down1 : super.down2;
		case LEFT -> super.sprintNum == 1 ? super.left1 : super.left2;
		case RIGHT -> super.sprintNum == 1 ? super.right1 : super.right2;
		default -> throw new IllegalArgumentException("Unexpected value for player direction: " + super.direction);
		};

		g2.drawImage(image, this.screenX, this.screenY, null);

		// Debug position player
//		System.out.println(String.format("PLAYER > SCREEN_X: %s, SCREEN_Y: %s, WORLD_X: %s, WORLD_Y: %s", this.screenX,
//				this.screenY, super.worldX, super.worldY));

		// debug collision area
//		g2.drawRect(this.screenX + 8, this.screenY + 16, 32, 31);

	}

	public void getPlayerImage() {
		super.up1 = this.setup("boy_up_1");
		super.up2 = this.setup("boy_up_2");
		super.right1 = this.setup("boy_right_1");
		super.right2 = this.setup("boy_right_2");
		super.down1 = this.setup("boy_down_1");
		super.down2 = this.setup("boy_down_2");
		super.left1 = this.setup("boy_left_1");
		super.left2 = this.setup("boy_left_2");
	}

	public BufferedImage setup(String fileName) {
		BufferedImage scaledImage = null;
		try {
			scaledImage = UtilityTool.scaleImage(
					ImageIO.read(getClass().getResourceAsStream(String.format("/player/%s.png", fileName))),
					this.gp.getTileSize(), this.gp.getTileSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scaledImage;
	}

	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public int getHasKey() {
		return hasKey;
	}

}
