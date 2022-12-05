package entity;

import static entity.Entity.Direction.DOWN;
import static entity.Entity.Direction.LEFT;
import static entity.Entity.Direction.RIGHT;
import static entity.Entity.Direction.UP;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

	private KeyHandler keyHandler;

	private final int screenX;
	private final int screenY;

	public Player(final GamePanel gp, final KeyHandler keyHandler) {
		super(gp);

		this.keyHandler = keyHandler;

		// posicao do player na screen centralizado
		this.screenX = super.getGp().getScreenWidth() / 2 - (super.getGp().getTileSize() / 2); // 360
		this.screenY = super.getGp().getScreenHeight() / 2 - (super.getGp().getTileSize() / 2); // 264

		// Definir area de colisao do player
		super.solidArea = new Rectangle();
		super.solidArea.x = 8;
		super.solidArea.y = 16;
		super.solidAreaDefaultX = super.solidArea.x;
		this.solidAreaDefaultY = super.solidArea.y;
		super.solidArea.width = 32;
		super.solidArea.height = 31;

		this.setDefaultValues();
		this.getImage();
	}

	private void setDefaultValues() {

		// posicao do player no world fixa no centro p inicio do game
		super.worldX = super.getGp().getTileSize() * 23; // 1104
		super.worldY = super.getGp().getTileSize() * 21; // 1008

		super.speed = 4;
		super.direction = DOWN;
		
		
		// PLAYER LIFE
		int defaultLife = 6;
		super.setMaxLife(defaultLife);
		super.setLife(defaultLife);
	}

	public void setAction() {

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
			super.getGp().getCollisionChecker().checkTile(this);

			// CHECK OBJECT COLLISION
			int objectIndex = super.getGp().getCollisionChecker().checkObject(this, true);
			this.pickupObject(objectIndex);

			// CHECK NPCS COLLISION
			int collisionNpcIndex = super.getGp().getCollisionChecker().checkEntity(this, super.getGp().getNpcs());
			this.interactNPC(collisionNpcIndex);
			
			// CHECK EVENT
			this.getGp().getEventHandler().checkEvent();
			
			// Dps de checkar nos eventos a key, resetar
			this.keyHandler.setEnterPressed(Boolean.FALSE);

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

	private void interactNPC(int collisionNpcIndex) {
		if (collisionNpcIndex >= 0) {
			if (this.keyHandler.isEnterPressed()) {
				this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
				this.getGp().getNpcs()[collisionNpcIndex].speak();
			}
		}
	}

	public void pickupObject(int objectIndex) {
		if (objectIndex >= 0) {

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
//		g2.setColor(new Color(0, 0, 255, 100));
//		g2.fillRect(this.screenX + 8, this.screenY + 16, 32, 31);

	}

	public void getImage() {
		super.up1 = this.setup("/player/boy_up_1.png");
		super.up2 = this.setup("/player/boy_up_2.png");
		super.right1 = this.setup("/player/boy_right_1.png");
		super.right2 = this.setup("/player/boy_right_2.png");
		super.down1 = this.setup("/player/boy_down_1.png");
		super.down2 = this.setup("/player/boy_down_2.png");
		super.left1 = this.setup("/player/boy_left_1.png");
		super.left2 = this.setup("/player/boy_left_2.png");
	}

	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

}
