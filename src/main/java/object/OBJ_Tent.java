package object;

import entity.Entity;
import environment.Lighting.DayState;
import main.GamePanel;
import main.Sound;

public class OBJ_Tent extends Entity {

	public OBJ_Tent(GamePanel gp) {
		super(gp);
		this.setType(EntityType.CONSUMABLE);
		this.setName("Tent");
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nYou can sleep at night\nuntil next morning.");
		this.setPrice(300);
		this.setStackable(true);
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/tent.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	@Override
	public boolean use(Entity entity) {
		if (DayState.DUSK.equals(this.getGp().geteManager().getLighting().getDayState())
				|| DayState.NIGHT.equals(this.getGp().geteManager().getLighting().getDayState())) {

			this.getGp().setGameState(GamePanel.SLEEP_STATE);
			this.getGp().getPlayer().loadSleepingImages(this.down1);
			new Sound(Sound.SLEEP_MUSIC, false).play();
			this.getGp().getPlayer().setLife(this.getGp().getPlayer().getMaxLife());
			this.getGp().getPlayer().setMana(this.getGp().getPlayer().getMaxMana());
			return true;

		}
		return false;
	}
}
