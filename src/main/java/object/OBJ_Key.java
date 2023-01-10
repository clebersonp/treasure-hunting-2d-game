package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

	public OBJ_Key(GamePanel gp) {
		super(gp);
		this.setName("Key");
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nIt opens a door.");
		this.setPrice(15);
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/key.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
