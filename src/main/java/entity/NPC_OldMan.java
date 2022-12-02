package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity {

	private int actionLockCounter = 0;

	public NPC_OldMan(GamePanel gp) {
		super(gp);

		this.direction = Entity.Direction.DOWN;
		this.speed = 1;
		this.getImage();
		this.setDialogue();
	}

	public void getImage() {
		super.up1 = this.setup("/npc/oldman_up_1.png");
		super.up2 = this.setup("/npc/oldman_up_2.png");
		super.right1 = this.setup("/npc/oldman_right_1.png");
		super.right2 = this.setup("/npc/oldman_right_2.png");
		super.down1 = this.setup("/npc/oldman_down_1.png");
		super.down2 = this.setup("/npc/oldman_down_2.png");
		super.left1 = this.setup("/npc/oldman_left_1.png");
		super.left2 = this.setup("/npc/oldman_left_2.png");
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

			BufferedImage image = null;

			image = switch (super.direction) {
			case UP -> super.sprintNum == 1 ? super.up1 : super.up2;
			case DOWN -> super.sprintNum == 1 ? super.down1 : super.down2;
			case LEFT -> super.sprintNum == 1 ? super.left1 : super.left2;
			case RIGHT -> super.sprintNum == 1 ? super.right1 : super.right2;
			default -> throw new IllegalArgumentException("Unexpected value for player direction: " + super.direction);
			};

			g2.drawImage(image, screenX, screenY, null);

		}

	}

	public void setAction() {

		this.actionLockCounter++;

		if (this.actionLockCounter == 120) {

			Random random = new Random();
			int i = random.nextInt(100) + 1;

			if (i <= 25) {
				super.direction = Entity.Direction.UP;
			}
			if (i > 25 && i <= 50) {
				super.direction = Entity.Direction.DOWN;
			}
			if (i > 50 && i <= 75) {
				super.direction = Entity.Direction.LEFT;
			}
			if (i > 75 && i <= 100) {
				super.direction = Entity.Direction.RIGHT;
			}

			this.actionLockCounter = 0;
		}
	}

	public void setDialogue() {
		this.getDialogues()[0] = "Hello, lad.";
		this.getDialogues()[1] = "So you've come to this island to find \nthe treasures?";
		this.getDialogues()[2] = "I used to be a great wizard but \nnow... I'm a bit too old for taking \nan adventure.";
		this.getDialogues()[3] = "Well, good luck on you.";
	}

	public void speak() {
		super.speak();
	}
}
