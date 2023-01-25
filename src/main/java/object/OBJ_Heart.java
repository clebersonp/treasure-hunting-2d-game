package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_Heart extends Entity {

	public OBJ_Heart(GamePanel gp) {
		super(gp);
		this.setName("Heart");
		this.setType(EntityType.PICKUP_ONLY);
		this.setValue(2);
		this.loadPlayerImages();
	}

	@Override
	protected void loadPlayerImages() {
		this.down1 = this.setup("/objects/heart_full.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.image = this.setup("/objects/heart_full.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.image2 = this.setup("/objects/heart_half.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.image3 = this.setup("/objects/heart_blank.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	@Override
	public boolean use(Entity entity) {
		if (entity.getLife() < entity.getMaxLife()) {
			this.getGp().getUi().addMessage("Life +" + this.getValue());

			int newLife = entity.getLife() + this.getValue();
			if (newLife <= entity.getMaxLife()) {
				entity.setLife(newLife);
			} else {
				entity.setLife(entity.getMaxLife());
			}
			new Sound(Sound.POWER_UP, false).play();
			return true;
		}
		return false;
	}

}
