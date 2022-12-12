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
		
//		this.gp.getNpcs()[1] = new NPC_OldMan(this.gp);
//		this.gp.getNpcs()[1].setWorldX(this.gp.getTileSize() * 9);
//		this.gp.getNpcs()[1].setWorldY(this.gp.getTileSize() * 7);
	}

	public void setMonsters() {
		int i = 0;
		this.gp.getMonsters()[i] = new Slime(this.gp);
		this.gp.getMonsters()[i].setWorldX(this.gp.getTileSize() * 21);
		this.gp.getMonsters()[i].setWorldY(this.gp.getTileSize() * 41);
		i++;
		
		this.gp.getMonsters()[i] = new Slime(this.gp);
		this.gp.getMonsters()[i].setWorldX(this.gp.getTileSize() * 23);
		this.gp.getMonsters()[i].setWorldY(this.gp.getTileSize() * 40);
		i++;
		
		this.gp.getMonsters()[i] = new Slime(this.gp);
		this.gp.getMonsters()[i].setWorldX(this.gp.getTileSize() * 31);
		this.gp.getMonsters()[i].setWorldY(this.gp.getTileSize() * 40);
		i++;
		
		this.gp.getMonsters()[i] = new Slime(this.gp);
		this.gp.getMonsters()[i].setWorldX(this.gp.getTileSize() * 35);
		this.gp.getMonsters()[i].setWorldY(this.gp.getTileSize() * 41);
		i++;
	}

}
