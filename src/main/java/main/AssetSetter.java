package main;

import entity.NPC_OldMan;
import monster.Slime;
import object.OBJ_Axe;
import object.OBJ_Key;
import object.OBJ_PotionRed;
import object.OBJ_ShieldBlue;
import object.OBJ_ShieldWood;

public class AssetSetter {

	private GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}

	public void setObject() {
		int i = 0;
		this.gp.getObjects()[i] = new OBJ_Key(this.gp);
		this.gp.getObjects()[i].setWorldX(this.gp.getTileSize() * 25);
		this.gp.getObjects()[i].setWorldY(this.gp.getTileSize() * 23);
		i++;
		this.gp.getObjects()[i] = new OBJ_Key(this.gp);
		this.gp.getObjects()[i].setWorldX(this.gp.getTileSize() * 21);
		this.gp.getObjects()[i].setWorldY(this.gp.getTileSize() * 19);
		i++;
		this.gp.getObjects()[i] = new OBJ_Key(this.gp);
		this.gp.getObjects()[i].setWorldX(this.gp.getTileSize() * 26);
		this.gp.getObjects()[i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[i] = new OBJ_Axe(this.gp);
		this.gp.getObjects()[i].setWorldX(this.gp.getTileSize() * 23);
		this.gp.getObjects()[i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[i] = new OBJ_ShieldBlue(this.gp);
		this.gp.getObjects()[i].setWorldX(this.gp.getTileSize() * 19);
		this.gp.getObjects()[i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[i].setWorldX(this.gp.getTileSize() * 16);
		this.gp.getObjects()[i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[i].setWorldX(this.gp.getTileSize() * 26);
		this.gp.getObjects()[i].setWorldY(this.gp.getTileSize() * 6);
		i++;
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
