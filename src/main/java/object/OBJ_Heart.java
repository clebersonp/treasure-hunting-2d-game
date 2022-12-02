package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class OBJ_Heart extends SuperObject {

	private GamePanel gp;

	public OBJ_Heart(GamePanel gp) {
		this.gp = gp;
		super.name = "Boots";
		try {
			super.image = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/objects/heart_full.png")),
					this.gp.getTileSize(), this.gp.getTileSize());
			super.image2 = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/objects/heart_half.png")),
					this.gp.getTileSize(), this.gp.getTileSize());
			super.image3 = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/objects/heart_blank.png")),
					this.gp.getTileSize(), this.gp.getTileSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
