package main;

import entity.NPC_OldMan;
import monster.Slime;

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
		
		this.gp.getNpcs()[1] = new NPC_OldMan(this.gp);
		this.gp.getNpcs()[1].setWorldX(this.gp.getTileSize() * 9);
		this.gp.getNpcs()[1].setWorldY(this.gp.getTileSize() * 7);
	}

	public void setMonsters() {
		this.gp.getMonsters()[0] = new Slime(this.gp);
		this.gp.getMonsters()[0].setWorldX(this.gp.getTileSize() * 9);
		this.gp.getMonsters()[0].setWorldY(this.gp.getTileSize() * 9);
		
		this.gp.getMonsters()[1] = new Slime(this.gp);
		this.gp.getMonsters()[1].setWorldX(this.gp.getTileSize() * 11);
		this.gp.getMonsters()[1].setWorldY(this.gp.getTileSize() * 7);
	}

}
