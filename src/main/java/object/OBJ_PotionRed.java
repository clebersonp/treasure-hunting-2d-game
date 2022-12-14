package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_PotionRed extends Entity {

	private int value = 5;

	public OBJ_PotionRed(GamePanel gp) {
		super(gp);
		this.setType(EntityType.CONSUMABLE);
		this.setName("Red Potion");
		this.setDefenseValue(2);
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nHeals your life by " + this.value + ".");
	}

	@Override
	public boolean use(Entity entity) {

		if (entity.getLife() < entity.getMaxLife()) {

			int newLife = entity.getLife() + this.value;

			this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
			this.getGp().getUi().setCurrentDialogue(
					"You drink the " + this.getName() + "!\n" + "Your life has been recovered by " + this.value + ".");

			if (newLife <= entity.getMaxLife()) {
				entity.setLife(newLife);
			} else {
				entity.setLife(entity.getMaxLife());
			}
			new Sound(Sound.POWER_UP);
			return true;
		}
		return false;
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/potion_red.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
