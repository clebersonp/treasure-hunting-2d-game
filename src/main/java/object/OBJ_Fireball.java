package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

	public OBJ_Fireball(GamePanel gp) {
		super(gp);
		this.setName("Fireball");
		this.setSpeed(10);
		this.setMaxLife(80);
		this.setLife(this.getMaxLife());
		this.setAttack(1);
		this.setUseCost(1);
		this.setAlive(Boolean.FALSE);
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.up1 = this.setup("/projectiles/fireball_up_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.up2 = this.setup("/projectiles/fireball_up_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down1 = this.setup("/projectiles/fireball_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.down2 = this.setup("/projectiles/fireball_down_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.left1 = this.setup("/projectiles/fireball_left_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.left2 = this.setup("/projectiles/fireball_left_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.right1 = this.setup("/projectiles/fireball_right_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.right2 = this.setup("/projectiles/fireball_right_2.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
	}

}
