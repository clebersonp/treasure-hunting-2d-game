package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_SwordNormal extends Entity {

	public OBJ_SwordNormal(GamePanel gp) {
		super(gp);
		this.setType(EntityType.SWORD);
		this.setName("Normal Sword");
		this.setAttackValue(1);
		this.attackArea.width = 36;
		this.attackArea.height = 36;
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nAn old sword.");
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/sword_normal.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
