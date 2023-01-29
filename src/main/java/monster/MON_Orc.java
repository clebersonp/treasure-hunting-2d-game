package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_CoinBronze;
import object.OBJ_Heart;
import object.OBJ_Mana;

public class MON_Orc extends Entity {

	public MON_Orc(GamePanel gp) {
		super(gp);

		this.setType(EntityType.MONSTER);
		this.setName("Green Orc");
		this.setDefaultSpeed(1);
		this.setSpeed(this.getDefaultSpeed());
		this.setMaxLife(10);
		this.setLife(this.getMaxLife());
		this.setAttack(2);
		this.setDefense(2);
		this.setExp(10);
		this.setMotion1_duration(40);
		this.setMotion2_duration(85);

		this.solidArea.x = 4;
		this.solidArea.y = 4;
		this.solidArea.width = 40;
		this.solidArea.height = 44;
		this.solidAreaDefaultX = this.solidArea.x;
		this.solidAreaDefaultY = this.solidArea.y;
		this.attackArea.width = 48;
		this.attackArea.height = 48;

		this.loadImages();
		this.loadAttackImages();
	}

	@Override
	protected void loadImages() {
		this.up1 = this.setup("/monsters/orc_up_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.up2 = this.setup("/monsters/orc_up_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down1 = this.setup("/monsters/orc_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down2 = this.setup("/monsters/orc_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.left1 = this.setup("/monsters/orc_left_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.left2 = this.setup("/monsters/orc_left_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.right1 = this.setup("/monsters/orc_right_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.right2 = this.setup("/monsters/orc_right_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	public void loadAttackImages() {
		this.attackUp1 = this.setup("/monsters/orc_attack_up_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize() * 2);
		this.attackUp2 = this.setup("/monsters/orc_attack_up_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize() * 2);
		this.attackDown1 = this.setup("/monsters/orc_attack_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize() * 2);
		this.attackDown2 = this.setup("/monsters/orc_attack_down_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize() * 2);
		this.attackLeft1 = this.setup("/monsters/orc_attack_left_1.png", this.getGp().getTileSize() * 2,
				this.getGp().getTileSize());
		this.attackLeft2 = this.setup("/monsters/orc_attack_left_2.png", this.getGp().getTileSize() * 2,
				this.getGp().getTileSize());
		this.attackRight1 = this.setup("/monsters/orc_attack_right_1.png", this.getGp().getTileSize() * 2,
				this.getGp().getTileSize());
		this.attackRight2 = this.setup("/monsters/orc_attack_right_2.png", this.getGp().getTileSize() * 2,
				this.getGp().getTileSize());
	}

	@Override
	public void setAction() {

		if (this.isOnPath()) {
			// Check if it stops chasing
			this.checkStopChasingOrNot(this.getGp().getPlayer(), 10, 100);

			// Search the direction to go
			this.searchPath(this.getGoalRow(this.getGp().getPlayer()), this.getGoalCol(this.getGp().getPlayer()));

		} else {
			// Check if it starts chasing
			this.checkStartChasingOrNot(this.getGp().getPlayer(), 5, 100);

			// Get a random direction
			this.getRandomDirection();
		}
		
		// Check if it attacks
		if (!this.isAttacking()) {
			this.checkAttackOrNot(30, this.getGp().getTileSize() * 4, this.getGp().getTileSize());
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
