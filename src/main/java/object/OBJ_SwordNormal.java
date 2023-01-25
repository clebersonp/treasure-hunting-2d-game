package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_SwordNormal extends Entity {

	public OBJ_SwordNormal(GamePanel gp) {
		super(gp);
		this.setType(EntityType.SWORD);
		this.setName("Normal Sword");
		this.setAttackValue(1);
		this.setKnockBackPower(8);
		this.attackArea.width = 36;
		this.attackArea.height = 36;
		this.loadPlayerImages();
		this.setDescription("[" + this.getName() + "]\nAn old sword.");
		this.setPrice(45);
	}

	@Override
	protected void loadPlayerImages() {
		this.down1 = this.setup("/objects/sword_normal.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
