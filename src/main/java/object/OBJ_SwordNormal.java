package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_SwordNormal extends Entity {

	public OBJ_SwordNormal(GamePanel gp) {
		super(gp);
		this.setName("Normal Sword");
		this.setAttackValue(4);
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/sword_normal.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
