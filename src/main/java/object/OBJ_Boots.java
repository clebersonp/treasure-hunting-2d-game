package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class OBJ_Boots extends SuperObject {

	private GamePanel gp;

	public OBJ_Boots(GamePanel gp) {
		this.gp = gp;
		super.name = "Boots";
		try {
			super.image = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/objects/boots.png")),
					this.gp.getTileSize(), this.gp.getTileSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
