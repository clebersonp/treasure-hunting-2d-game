package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class Slime extends Entity {

	public Slime(GamePanel gp) {
		super(gp);

		this.type = TYPE.MONSTER;
		this.name = "Green Slime";
		this.speed = 1;
		this.setMaxLife(4);
		this.setLife(this.getMaxLife());

		this.solidArea.x = 3;
		this.solidArea.y = 18;
		this.solidArea.width = 42;
		this.solidArea.height = 30;
		this.solidAreaDefaultX = this.solidArea.x;
		this.solidAreaDefaultY = this.solidArea.y;

		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.up1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.up2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.left1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.left2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.right1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.right2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	@Override
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

}
