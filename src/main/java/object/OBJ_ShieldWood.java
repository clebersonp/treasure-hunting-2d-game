package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ShieldWood extends Entity {

	public OBJ_ShieldWood(GamePanel gp) {
		super(gp);
		this.setName("Shield Wood");
		this.setDefenseValue(1);
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/shield_wood.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
