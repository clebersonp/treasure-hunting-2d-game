package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Lantern extends Entity {

	public OBJ_Lantern(GamePanel gp) {
		super(gp);
		this.setName("Lanter");
		this.setType(EntityType.LIGHT);
		this.loadPlayerImages();
		this.setDescription("[" + this.getName() + "]\nIlluminates your \nsurroundings.");
		this.setPrice(200);
		this.setLightRadius(450);
	}

	@Override
	protected void loadPlayerImages() {
		this.down1 = this.setup("/objects/lantern.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
