package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_PotionRed extends Entity {

	public OBJ_PotionRed(GamePanel gp) {
		super(gp);
		this.setType(EntityType.CONSUMABLE);
		this.setName("Red Potion");
		this.setValue(5);
		this.setDefenseValue(2);
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nHeals your life by " + this.getValue() + ".");
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/potion_red.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	@Override
	public boolean use(Entity entity) {

		if (entity.getLife() < entity.getMaxLife()) {

			int newLife = entity.getLife() + this.getValue();

			this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
			this.getGp().getUi().setCurrentDialogue("You drink the " + this.getName() + "!\n"
					+ "Your life has been recovered by " + this.getValue() + ".");

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
