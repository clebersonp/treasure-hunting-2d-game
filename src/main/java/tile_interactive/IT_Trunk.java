package tile_interactive;

import main.GamePanel;

public class IT_Trunk extends InteractiveTile {

	public IT_Trunk(GamePanel gp, int col, int row) {
		super(gp, col, row);
		this.setWorldX(this.getGp().getTileSize() * col);
		this.setWorldY(this.getGp().getTileSize() * row);

		// RESET THE SOLID AREA TO PASS THROUGH IT
		this.solidArea.x = 0;
		this.solidArea.y = 0;
		this.solidArea.width = 0;
		this.solidArea.height = 0;
		this.solidAreaDefaultX = this.solidArea.x;
		this.solidAreaDefaultY = this.solidArea.y;

		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/tiles_interactive/trunk.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

}
