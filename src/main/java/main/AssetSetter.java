package main;

import entity.NPC_OldMan;
import object.OBJ_Door;

public class AssetSetter {

	private GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}

	public void setObject() {
		this.gp.getObjects()[0] = new OBJ_Door(this.gp);
		this.gp.getObjects()[0].setWorldX(this.gp.getTileSize() * 15);
		this.gp.getObjects()[0].setWorldY(this.gp.getTileSize() * 21);
		
		this.gp.getObjects()[1] = new OBJ_Door(this.gp);
		this.gp.getObjects()[1].setWorldX(this.gp.getTileSize() * 38);
		this.gp.getObjects()[1].setWorldY(this.gp.getTileSize() * 7);
		
		this.gp.getObjects()[2] = new OBJ_Door(this.gp);
		this.gp.getObjects()[2].setWorldX(this.gp.getTileSize() * 20);
		this.gp.getObjects()[2].setWorldY(this.gp.getTileSize() * 40);
	}

	public void setNPC() {
		this.gp.getNpcs()[0] = new NPC_OldMan(this.gp);
		this.gp.getNpcs()[0].setWorldX(this.gp.getTileSize() * 21);
		this.gp.getNpcs()[0].setWorldY(this.gp.getTileSize() * 21);
		
		this.gp.getNpcs()[1] = new NPC_OldMan(this.gp);
		this.gp.getNpcs()[1].setWorldX(this.gp.getTileSize() * 38);
		this.gp.getNpcs()[1].setWorldY(this.gp.getTileSize() * 6);
		
		this.gp.getNpcs()[2] = new NPC_OldMan(this.gp);
		this.gp.getNpcs()[2].setWorldX(this.gp.getTileSize() * 21);
		this.gp.getNpcs()[2].setWorldY(this.gp.getTileSize() * 40);
	}

}
