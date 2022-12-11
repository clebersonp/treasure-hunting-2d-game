package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

	public OBJ_Chest(GamePanel gp) {
		super(gp);
		this.name = "Chest";
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/chest.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
