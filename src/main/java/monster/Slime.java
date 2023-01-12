package monster;

import java.util.Objects;
import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_CoinBronze;
import object.OBJ_Heart;
import object.OBJ_Mana;
import object.OBJ_Rock;

public class Slime extends Entity {

	public Slime(GamePanel gp) {
		super(gp);

		this.setType(EntityType.MONSTER);
		this.setName("Green Slime");
		this.setDefaultSpeed(1);
		this.setSpeed(this.getDefaultSpeed());
		this.setMaxLife(4);
		this.setLife(this.getMaxLife());
		this.setAttack(2);
		this.setDefense(0);
		this.setExp(2);

		this.solidArea.x = 3;
		this.solidArea.y = 18;
		this.solidArea.width = 42;
		this.solidArea.height = 30;
		this.solidAreaDefaultX = this.solidArea.x;
		this.solidAreaDefaultY = this.solidArea.y;

		this.setProjectile(new OBJ_Rock(gp));

		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.up1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.up2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.down1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.down2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.left1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.left2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.right1 = this.setup("/monsters/greenslime_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.right2 = this.setup("/monsters/greenslime_down_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
	}

	@Override
	public void update() {
		super.update();

		int xDistance = Math.abs(this.worldX - this.getGp().getPlayer().getWorldX());
		int yDistance = Math.abs(this.worldY - this.getGp().getPlayer().getWorldY());
		int tileDistance = (xDistance + yDistance) / this.getGp().getTileSize();

		if (!this.isOnPath() && tileDistance < 5) {

			int random = new Random().nextInt(100) + 1;
			if (random > 50) {
				this.setOnPath(true);
			}
		}

		if (this.isOnPath() && tileDistance > 10) {
//			int random = new Random().nextInt(100) + 1;
//			if (random > 90) {
				this.setOnPath(false);
//			}
		}

	}

	@Override
	public void setAction() {

		if (this.isOnPath()) {

			int goalRow = (this.getGp().getPlayer().getWorldY() + this.getGp().getPlayer().getSolidArea().y)
					/ this.getGp().getTileSize();
			int goalCol = (this.getGp().getPlayer().getWorldX() + this.getGp().getPlayer().getSolidArea().x)
					/ this.getGp().getTileSize();

			this.searchPath(goalRow, goalCol);

			int i = new Random().nextInt(200) + 1;
			if (i > 197 && !this.getProjectile().isAlive() && this.getShotAvailableCounter() == 40) {
				this.getProjectile().set(this.worldX, this.worldY, this.direction, Boolean.TRUE, this);
				
				for (int y = 0; y < this.getGp().getProjectiles()[this.getGp().getCurrentMap()].length; y++) {
					if (Objects.isNull(this.getGp().getProjectiles()[this.getGp().getCurrentMap()][y])) {
						this.getGp().getProjectiles()[this.getGp().getCurrentMap()][y] = this.getProjectile();
						this.setShotAvailableCounter(0);
						break;
					}
				}
			}

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

	@Override
	public void damageReaction() {
		this.setActionLockCounter(0);

		this.setOnPath(true);

		// o monster ira p o mesmo sentido do player, fugindo dele
//		this.direction = this.getGp().getPlayer().getDirection();
	}

	@Override
	public void checkDrop() {
		int i = new Random().nextInt(1_000) + 1;

		// SET THE MONSTER DROP ITEM
		if (i >= 450 && i < 720) {
			this.dropItem(new OBJ_CoinBronze(this.getGp()));
		} else if (i >= 800 && i < 850) {
			this.dropItem(new OBJ_Mana(this.getGp()));
		} else if (i >= 999) {
			this.dropItem(new OBJ_Heart(this.getGp()));
		}
	}

}
