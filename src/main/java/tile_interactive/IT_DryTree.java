package tile_interactive;

import java.awt.Color;
import java.util.Objects;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class IT_DryTree extends InteractiveTile {

	private static final Color BROWN_COLOR = new Color(65, 50, 30);
	private static final int PARTICLE_SIZE = 6;
	private static final int PARTICLE_SPEED = 1;
	private static final int PARTICLE_MAX_LIFE = 20;

	public IT_DryTree(GamePanel gp, int col, int row) {
		super(gp, col, row);
		this.setWorldX(this.getGp().getTileSize() * col);
		this.setWorldY(this.getGp().getTileSize() * row);

		this.setMaxLife(3);
		this.setLife(this.getMaxLife());
		this.setDestructible(Boolean.TRUE);
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/tiles_interactive/drytree.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
	}

	@Override
	public void playSE() {
		new Sound(Sound.CUT_TREE).play();
	}

	@Override
	public InteractiveTile getDestroyedForm() {
		return new IT_Trunk(this.getGp(), this.getWorldX() / this.getGp().getTileSize(),
				this.worldY / this.getGp().getTileSize());
	}

	@Override
	public boolean isCorrectItem(Entity entity) {
		return Objects.isNull(entity) ? false : EntityType.AXE.equals(entity.getCurrentWeapon().getType());
	}

	@Override
	public void update() {

		if (this.isInvincible()) {
			this.incrementInvincibleCounter();
			if (this.getInvincibleCounter() > 40) { // 40 segundos
				this.setInvincible(Boolean.FALSE);
				this.resetInvincibleCounter();
			}
		}

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
