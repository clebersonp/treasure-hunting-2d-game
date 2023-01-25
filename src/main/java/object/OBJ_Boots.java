package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

	public OBJ_Boots(GamePanel gp) {
		super(gp);
		this.setName("Boots");
		this.loadPlayerImages();
	}

	@Override
	protected void loadPlayerImages() {
		this.down1 = this.setup("/objects/boots.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
