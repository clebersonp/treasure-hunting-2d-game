package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Mana extends Entity {

	public OBJ_Mana(GamePanel gp) {
		super(gp);
		this.setName("Mana Crystal");
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.image = this.setup("/objects/manacrystal_full.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
		this.image2 = this.setup("/objects/manacrystal_blank.png", this.getGp().getTileSize(),
				this.getGp().getTileSize());
	}

}
