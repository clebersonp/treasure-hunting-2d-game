package object;

import java.util.Objects;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_Chest extends Entity {

	private Entity loot;
	private boolean opened = false;

	public OBJ_Chest(GamePanel gp, Entity loot) {
		super(gp);
		this.loot = loot;
		this.setName("Chest");
		this.setType(EntityType.OBSTACLE);
		this.setCollision(true);

		this.solidArea.x = 4;
		this.solidArea.y = 16;
		this.solidArea.width = 40;
		this.solidArea.height = 32;
		this.solidAreaDefaultX = this.solidArea.x;
		this.solidAreaDefaultY = this.solidArea.y;

		this.loadPlayerImages();
	}

	@Override
	protected void loadPlayerImages() {
		this.image = this.setup("/objects/chest.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.image2 = this.setup("/objects/chest_opened.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		this.down1 = image;
	}

	@Override
	public void interact() {

		if (!this.opened) {
			this.getGp().setGameState(GamePanel.DIALOGUE_STATE);
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("You open the %s!", this.getName()));
			new Sound(Sound.UNLOCK, false).play();
			
			this.down1 = this.image2;
			this.opened = true;

			if (Objects.isNull(this.loot)) {
				sb.append(String.format("\n... But the %s is empty!", this.getName()));
			} else if (this.getGp().getPlayer().canObtainItem(this.loot)) {
				sb.append(String.format("\nYou obtain the %s!", this.loot.getName()));
				this.loot = null;
			} else {
				sb.append("\n... But you cannot carry any more!");
			}
			this.getGp().getUi().setCurrentDialogue(sb.toString());
		} else {
			new Sound(Sound.UNLOCK, false).play();
			this.down1 = this.image;
			this.opened = false;
		}

	}

}
