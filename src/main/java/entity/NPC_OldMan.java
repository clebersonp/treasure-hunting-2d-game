package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity {

	public NPC_OldMan(GamePanel gp) {
		super(gp);

		super.solidArea.x = 8;
		super.solidArea.y = 16;
		super.solidAreaDefaultX = super.solidArea.x;
		this.solidAreaDefaultY = super.solidArea.y;
		super.solidArea.width = 32;
		super.solidArea.height = 31;

		this.direction = Entity.Direction.DOWN;
		this.speed = 1;
		this.loadImages();
		this.setDialogue();
	}

	public void loadImages() {
		super.up1 = this.setup("/npc/oldman_up_1.png");
		super.up2 = this.setup("/npc/oldman_up_2.png");
		super.right1 = this.setup("/npc/oldman_right_1.png");
		super.right2 = this.setup("/npc/oldman_right_2.png");
		super.down1 = this.setup("/npc/oldman_down_1.png");
		super.down2 = this.setup("/npc/oldman_down_2.png");
		super.left1 = this.setup("/npc/oldman_left_1.png");
		super.left2 = this.setup("/npc/oldman_left_2.png");
	}

	public void setAction() {

		this.setActionLockCounter(this.getActionLockCounter() + 1);

		if (this.getActionLockCounter() == 120) {

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

			this.setActionLockCounter(0);
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
