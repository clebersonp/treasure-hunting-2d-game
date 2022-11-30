package main;

import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

	private GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
		
		gp.getObjects()[0] = new OBJ_Key(this.gp);
		gp.getObjects()[0].setWorldX(23 * this.gp.getTileSize());
		gp.getObjects()[0].setWorldY(7 * this.gp.getTileSize());
		
		gp.getObjects()[1] = new OBJ_Key(this.gp);
		gp.getObjects()[1].setWorldX(23 * this.gp.getTileSize());
		gp.getObjects()[1].setWorldY(40 * this.gp.getTileSize());
		
		gp.getObjects()[2] = new OBJ_Key(this.gp);
		gp.getObjects()[2].setWorldX(38 * this.gp.getTileSize());
		gp.getObjects()[2].setWorldY(9 * this.gp.getTileSize());
		
		gp.getObjects()[3] = new OBJ_Door(this.gp);
		gp.getObjects()[3].setWorldX(12 * this.gp.getTileSize());
		gp.getObjects()[3].setWorldY(23 * this.gp.getTileSize());
		
		gp.getObjects()[4] = new OBJ_Door(this.gp);
		gp.getObjects()[4].setWorldX(8 * this.gp.getTileSize());
		gp.getObjects()[4].setWorldY(28 * this.gp.getTileSize());
		
		gp.getObjects()[5] = new OBJ_Door(this.gp);
		gp.getObjects()[5].setWorldX(10 * this.gp.getTileSize());
		gp.getObjects()[5].setWorldY(12 * this.gp.getTileSize());
		
		gp.getObjects()[6] = new OBJ_Chest(this.gp);
		gp.getObjects()[6].setWorldX(10 * this.gp.getTileSize());
		gp.getObjects()[6].setWorldY(8 * this.gp.getTileSize());
		
		gp.getObjects()[7] = new OBJ_Boots(this.gp);
		gp.getObjects()[7].setWorldX(37 * this.gp.getTileSize());
		gp.getObjects()[7].setWorldY(42 * this.gp.getTileSize());
	}
	
}
