package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

	public OBJ_Key(GamePanel gp) {
		super(gp);
		this.name = "Key";
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/key.png");
	}

}
