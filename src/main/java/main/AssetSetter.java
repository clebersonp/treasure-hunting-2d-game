package main;

import entity.NPC_Merchant;
import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import monster.MON_Orc;
import object.OBJ_Axe;
import object.OBJ_Chest;
import object.OBJ_CoinBronze;
import object.OBJ_Door;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_Lantern;
import object.OBJ_Mana;
import object.OBJ_PotionRed;
import object.OBJ_ShieldBlue;
import object.OBJ_Tent;
import tile_interactive.IT_DryTree;

public class AssetSetter {

	private GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}

	public void setObject() {
		int mapNum = 0;
		int i = 0;
		this.gp.getObjects()[mapNum][i] = new OBJ_Key(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 25);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 23);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_Key(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 20);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 21);
		i++;
//		this.gp.getObjects()[mapNum][i] = new OBJ_Door(this.gp);
//		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 14);
//		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 28);
//		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 26);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 23);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 19);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 16);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 26);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 6);
		i++;

		this.gp.getObjects()[mapNum][i] = new OBJ_Lantern(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 12);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 15);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_Tent(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 12);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 17);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 14);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 27);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 11);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 20);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 19);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 22);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_PotionRed(this.gp);
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 15);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 30);
		i++;
		this.gp.getObjects()[mapNum][i] = new OBJ_Chest(this.gp, new OBJ_PotionRed(this.gp));
		this.gp.getObjects()[mapNum][i].setWorldX(this.gp.getTileSize() * 12);
		this.gp.getObjects()[mapNum][i].setWorldY(this.gp.getTileSize() * 8);
		i++;
	}

	public void setNPC() {
		int mapNum = 0;
		int i = 0;
		this.gp.getNpcs()[mapNum][i] = new NPC_OldMan(this.gp);
		this.gp.getNpcs()[mapNum][i].setWorldX(this.gp.getTileSize() * 21);
		this.gp.getNpcs()[mapNum][i].setWorldY(this.gp.getTileSize() * 21);
		i++;
		
		i = 0;
		this.gp.getNpcs()[mapNum][i] = new NPC_Merchant(this.gp);
		this.gp.getNpcs()[mapNum][i].setWorldX(this.gp.getTileSize() * 6);
		this.gp.getNpcs()[mapNum][i].setWorldY(this.gp.getTileSize() * 29);
	}

	public void setMonsters() {
		int mapNum = 0;
		int i = 0;
		this.gp.getMonsters()[mapNum][i] = new MON_GreenSlime(this.gp);
		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 21);
		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 5);
		i++;

		this.gp.getMonsters()[mapNum][i] = new MON_GreenSlime(this.gp);
		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 23);
		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 8);
		i++;

		this.gp.getMonsters()[mapNum][i] = new MON_Orc(this.gp);
		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 25);
		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 10);
		i++;
		
		this.gp.getMonsters()[mapNum][i] = new MON_Orc(this.gp);
		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 24);
		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 9);
		i++;
//
//		this.gp.getMonsters()[mapNum][i] = new Slime(this.gp);
//		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 31);
//		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 40);
//		i++;
//
//		this.gp.getMonsters()[mapNum][i] = new Slime(this.gp);
//		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 35);
//		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 41);
//		i++;
//
//		this.gp.getMonsters()[mapNum][i] = new Slime(this.gp);
//		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 22);
//		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 42);
//		i++;
//
//		this.gp.getMonsters()[mapNum][i] = new Slime(this.gp);
//		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 24);
//		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 40);
//		i++;
//
//		this.gp.getMonsters()[mapNum][i] = new Slime(this.gp);
//		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 32);
//		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 40);
//		i++;
//
//		this.gp.getMonsters()[mapNum][i] = new Slime(this.gp);
//		this.gp.getMonsters()[mapNum][i].setWorldX(this.gp.getTileSize() * 36);
//		this.gp.getMonsters()[mapNum][i].setWorldY(this.gp.getTileSize() * 41);
		i++;
	}

	public void setInteractiveTiles() {
		int mapNum = 0;
		int i = 0;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 27, 12);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 28, 12);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 29, 12);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 30, 12);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 31, 12);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 32, 12);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 33, 12);
		i++;
		
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 30, 21);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 20, 20);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 20, 21);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 20, 22);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 22, 24);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 23, 24);
		i++;
		this.gp.getInteractiveTiles()[mapNum][i] = new IT_DryTree(gp, 24, 24);
		i++;
	}

}
