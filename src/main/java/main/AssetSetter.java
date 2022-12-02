package main;

import entity.NPC_OldMan;

public class AssetSetter {

	private GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}

	public void setObject() {

	}

	public void setNPC() {
		this.gp.getNpcs()[0] = new NPC_OldMan(this.gp);
		this.gp.getNpcs()[0].setWorldX(this.gp.getTileSize() * 21);
		this.gp.getNpcs()[0].setWorldY(this.gp.getTileSize() * 21);
	}

}
