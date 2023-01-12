package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

	public OBJ_Axe(GamePanel gp) {
		super(gp);
		this.setType(EntityType.AXE);
		this.setName("Woodcutter's Axe");
		this.setAttackValue(2);
		this.setKnockBackPower(3);
		this.attackArea.width = 25;
		this.attackArea.height = 20;
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nA bit rusty but still cut\nsome trees.");
		this.setPrice(75);
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/axe.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
