package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class OBJ_Chest extends SuperObject {

	private GamePanel gp;

	public OBJ_Chest(GamePanel gp) {
		this.gp = gp;
		super.name = "Chest";
		try {
			super.image = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/objects/chest.png")),
					this.gp.getTileSize(), this.gp.getTileSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
