package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity {

	public NPC_OldMan(GamePanel gp) {
		super(gp);
		this.setType(EntityType.NPC);
		super.solidArea.x = 8;
		super.solidArea.y = 16;
		super.solidAreaDefaultX = super.solidArea.x;
		this.solidAreaDefaultY = super.solidArea.y;
		super.solidArea.width = 32;
		super.solidArea.height = 32;

		this.direction = Entity.Direction.DOWN;
		this.setSpeed(1);
		this.loadImages();
		this.setDialogue();
	}

	public void loadImages() {
		super.up1 = this.setup("/npc/oldman_up_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.up2 = this.setup("/npc/oldman_up_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.right1 = this.setup("/npc/oldman_right_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.right2 = this.setup("/npc/oldman_right_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.down1 = this.setup("/npc/oldman_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.down2 = this.setup("/npc/oldman_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.left1 = this.setup("/npc/oldman_left_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.left2 = this.setup("/npc/oldman_left_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}
	
	@Override
	public void update() {
		super.update();
		
		int xDistance = Math.abs(this.worldX - this.getGp().getPlayer().getWorldX());
		int yDistance = Math.abs(this.worldY - this.getGp().getPlayer().getWorldY());
		int tileDistance = (xDistance + yDistance) / this.getGp().getTileSize();
		
		if (this.isOnPath() && tileDistance > 15) {
			this.setOnPath(false);
		}
	}

	public void setAction() {

		if (this.isOnPath()) {
			
			int goalRow = 9;
			int goalCol = 12;
			
			goalRow = (this.getGp().getPlayer().getWorldY() + this.getGp().getPlayer().getSolidArea().y)
					/ this.getGp().getTileSize();
			goalCol = (this.getGp().getPlayer().getWorldX() + this.getGp().getPlayer().getSolidArea().x)
					/ this.getGp().getTileSize();

			this.searchPath(goalRow, goalCol);

		} else {
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

	}

	public void setDialogue() {
		this.getDialogues()[0] = "Hello, lad.";
		this.getDialogues()[1] = "So you've come to this island to find \nthe treasures?";
		this.getDialogues()[2] = "I used to be a great wizard but \nnow... I'm a bit too old for taking \nan adventure.";
		this.getDialogues()[3] = "Well, good luck on you.";
	}

	public void speak() {
		super.speak();
		switch (this.getGp().getPlayer().getDirection()) {
		case UP -> this.direction = Direction.DOWN;
		case DOWN -> this.direction = Direction.UP;
		case LEFT -> this.direction = Direction.RIGHT;
		case RIGHT -> this.direction = Direction.LEFT;
		}
		if (this.isOnPath()) {
			this.setOnPath(false);
		} else {
			this.setOnPath(true);
		}
	}
}
