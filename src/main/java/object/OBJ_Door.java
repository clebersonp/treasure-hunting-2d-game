package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class OBJ_Door extends SuperObject {

	private GamePanel gp;

	public OBJ_Door(GamePanel gp) {
		this.gp = gp;
		super.name = "Door";
		try {
			super.image = UtilityTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/objects/door.png")),
					this.gp.getTileSize(), this.gp.getTileSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.collision = true;
	}

}
