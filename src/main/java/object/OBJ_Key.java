package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class OBJ_Key extends SuperObject {

	private GamePanel gp;

	public OBJ_Key(GamePanel gp) {
		this.gp = gp;
		super.name = "Key";
		try {
			super.image = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/objects/key.png")),
					this.gp.getTileSize(), this.gp.getTileSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
