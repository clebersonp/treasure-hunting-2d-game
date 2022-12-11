package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {

	public OBJ_Heart(GamePanel gp) {
		super(gp);
		this.name = "Boots";
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.image = this.setup("/objects/heart_full.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.image2 = this.setup("/objects/heart_half.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.image3 = this.setup("/objects/heart_blank.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
