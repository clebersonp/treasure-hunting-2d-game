package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_CoinBronze;
import object.OBJ_Heart;
import object.OBJ_Mana;
import object.OBJ_Rock;

public class MON_GreenSlime extends Entity {

	public MON_GreenSlime(GamePanel gp) {
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
	public void setAction() {

		if (this.isOnPath()) {
			// Check if it stops chasing
			this.checkStopChasingOrNot(this.getGp().getPlayer(), 10, 100);

			// Search the direction to go
			this.searchPath(this.getGoalRow(this.getGp().getPlayer()), this.getGoalCol(this.getGp().getPlayer()));

			// Check if it shoots a projectile
			this.checkShootOrNot(200, 40);

		} else {
			// Check if it starts chasing
			this.checkStartChasingOrNot(this.getGp().getPlayer(), 5, 100);

			// Get a random direction
			this.getRandomDirection();
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
