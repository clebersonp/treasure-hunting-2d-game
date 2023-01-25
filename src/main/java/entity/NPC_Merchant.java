package entity;

import main.GamePanel;
import object.OBJ_Axe;
import object.OBJ_Key;
import object.OBJ_Lantern;
import object.OBJ_PotionRed;
import object.OBJ_ShieldBlue;
import object.OBJ_ShieldWood;
import object.OBJ_SwordNormal;
import object.OBJ_Tent;

public class NPC_Merchant extends Entity {

	public NPC_Merchant(GamePanel gp) {
		super(gp);
		this.setType(EntityType.NPC);
		super.solidArea.x = 8;
		super.solidArea.y = 16;
		super.solidAreaDefaultX = super.solidArea.x;
		this.solidAreaDefaultY = super.solidArea.y;
		super.solidArea.width = 32;
		super.solidArea.height = 31;

		this.direction = Entity.Direction.DOWN;
		this.setDefaultSpeed(1);
		this.setSpeed(this.getDefaultSpeed());
		this.loadPlayerImages();
		this.setDialogue();
		this.setInventoryItems();
	}

	public void loadPlayerImages() {
		super.up1 = this.setup("/npc/merchant_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.up2 = this.setup("/npc/merchant_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.right1 = this.setup("/npc/merchant_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.right2 = this.setup("/npc/merchant_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.down1 = this.setup("/npc/merchant_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.down2 = this.setup("/npc/merchant_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.left1 = this.setup("/npc/merchant_down_1.png", this.getGp().getTileSize(), this.getGp().getTileSize());
		super.left2 = this.setup("/npc/merchant_down_2.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	public void setDialogue() {
		this.getDialogues()[0] = "He he, so you found me.\nI have some good stuff.\nDo you want to trade?";
	}

	public void setInventoryItems() {
		this.getInventory().clear();
		this.getInventory().add(new OBJ_PotionRed(this.getGp()));
		this.getInventory().add(new OBJ_Key(this.getGp()));
		this.getInventory().add(new OBJ_SwordNormal(this.getGp()));
		this.getInventory().add(new OBJ_Axe(this.getGp()));
		this.getInventory().add(new OBJ_ShieldWood(this.getGp()));
		this.getInventory().add(new OBJ_ShieldBlue(this.getGp()));
		this.getInventory().add(new OBJ_Lantern(this.getGp()));
		this.getInventory().add(new OBJ_Tent(this.getGp()));
	}

	@Override
	public void speak() {
		super.speak();
		this.getGp().setGameState(GamePanel.TRADE_STATE);
		this.getGp().getUi().setNpc(this);
	}

}
