package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ShieldBlue extends Entity {

	public OBJ_ShieldBlue(GamePanel gp) {
		super(gp);
		this.setType(EntityType.SHIELD);
		this.setName("Blue Shield");
		this.setDefenseValue(2);
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nA shiny blue shield.");
		this.setPrice(150);
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/shield_blue.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
