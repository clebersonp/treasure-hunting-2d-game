package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity {

	public OBJ_Door(GamePanel gp) {
		super(gp);
		this.setName("Door");
		this.setType(EntityType.OBSTACLE);
		this.setCollision(true);
		this.loadImages();

		// Definir area de colisao do obj door
		this.solidArea.x = 0;
		this.solidArea.y = 24;
		this.solidAreaDefaultX = this.solidArea.x;
		this.solidAreaDefaultY = this.solidArea.y;
		this.solidArea.width = 48;
		this.solidArea.height = 16;
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/door.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}
	
	// Interage com este obj quando o player pressionar o enter key
	@Override
	public void interact() {
		this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
		this.getGp().getUi().setCurrentDialogue("You need a key to open this.");
	}

}
