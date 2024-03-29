package object;

import java.awt.Color;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

	private static final Color BROWN_COLOR = new Color(240, 50, 0);
	private static final int PARTICLE_SIZE = 6;
	private static final int PARTICLE_SPEED = 1;
	private static final int PARTICLE_MAX_LIFE = 20;

	public OBJ_Fireball(GamePanel gp) {
		super(gp);
		this.setName("Fireball");
		this.setDefaultSpeed(5);
		this.setSpeed(this.getDefaultSpeed());
		this.setMaxLife(80);
		this.setLife(this.getMaxLife());
		this.setAttack(1);
		this.setUseCost(1);
		this.setAlive(Boolean.FALSE);
		
		this.setKnockBackPower(5);
		
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
