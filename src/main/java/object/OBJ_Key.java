package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

	public OBJ_Key(GamePanel gp) {
		super(gp);
		this.setName("Key");
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/key.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
