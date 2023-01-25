package object;

import java.awt.Color;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Rock extends Projectile {

	private static final Color BROWN_COLOR = new Color(40, 50, 0);
	private static final int PARTICLE_SIZE = 5;
	private static final int PARTICLE_SPEED = 1;
	private static final int PARTICLE_MAX_LIFE = 20;

	public OBJ_Rock(GamePanel gp) {
		super(gp);
		this.setName("Rock");
		this.setDefaultSpeed(4);
		this.setSpeed(this.getDefaultSpeed());
		this.setMaxLife(80);
		this.setLife(this.getMaxLife());
		this.setAttack(2);
		this.setUseCost(1);
		this.setAlive(Boolean.FALSE);
		this.loadPlayerImages();
	}

	@Override
	protected void loadPlayerImages() {
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

	@Override
	public Color getParticleColor() {
		return BROWN_COLOR;
	}

	@Override
	public int getParticleSize() {
		return PARTICLE_SIZE; // pixels
	}

	@Override
	public int getParticleSpeed() {
		return PARTICLE_SPEED;
	}

	@Override
	public int getParticleMaxLife() {
		return PARTICLE_MAX_LIFE;
	}

}
