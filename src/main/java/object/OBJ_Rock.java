package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Rock extends Projectile {

	public OBJ_Rock(GamePanel gp) {
		super(gp);
		this.setName("Rock");
		this.setSpeed(8);
		this.setMaxLife(80);
		this.setLife(this.getMaxLife());
		this.setAttack(2);
		this.setUseCost(1);
		this.setAlive(Boolean.FALSE);
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.up1 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.up2 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down1 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down2 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.left1 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.left2 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.right1 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.right2 = this.setup("/projectiles/rock_down_1.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
	}

	@Override
	public boolean haveResource(Entity user) {
		return user.getAmmo() >= this.getUseCost();
	}

	@Override
	public void subtractResource(Entity user) {
		int newAmmo = user.getAmmo() - this.getUseCost();
		user.setAmmo(newAmmo);
	}

}
