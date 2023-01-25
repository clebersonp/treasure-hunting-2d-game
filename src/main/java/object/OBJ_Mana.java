package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_Mana extends Entity {

	public OBJ_Mana(GamePanel gp) {
		super(gp);
		this.setName("Mana Crystal");
		this.setType(EntityType.PICKUP_ONLY);
		this.setValue(1);
		this.loadPlayerImages();
	}

	@Override
	protected void loadPlayerImages() {
		this.down1 = this.setup("/objects/manacrystal_full.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.image = this.setup("/objects/manacrystal_full.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.image2 = this.setup("/objects/manacrystal_blank.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
	}

	@Override
	public boolean use(Entity entity) {
		if (entity.getMana() < entity.getMaxMana()) {
			this.getGp().getUi().addMessage("Mana +" + this.getValue());

			int newMana = entity.getMana() + this.getValue();
			if (newMana <= entity.getMaxMana()) {
				entity.setMana(newMana);
			} else {
				entity.setMana(entity.getMaxMana());
			}
			new Sound(Sound.POWER_UP, false).play();;
			return true;
		}
		return false;
	}

}
