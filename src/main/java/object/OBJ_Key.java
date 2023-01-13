package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_Key extends Entity {

	public OBJ_Key(GamePanel gp) {
		super(gp);
		this.setName("Key");
		this.setType(EntityType.CONSUMABLE);
		this.loadImages();
		this.setDescription("[" + this.getName() + "]\nIt opens a door.");
		this.setPrice(15);
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/key.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	@Override
	public boolean use(Entity entity) {
		this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
		
		// Verificar se existe um OBJ_Door proximo a entity ao utilizar este OBJ_KEY
		int objIndex = this.getDetected(entity, this.getGp().getObjects(), "Door");
		
		if (objIndex >= 0) {
			this.getGp().getUi().setCurrentDialogue("You use the " + this.getName() + " and open the door.");
			new Sound(Sound.UNLOCK, false).play();
			this.getGp().getObjects()[this.getGp().getCurrentMap()][objIndex] = null;
			return true;
		} else {
			this.getGp().getUi().setCurrentDialogue("What are you doing?");
			return false;
		}
	}

	
}
