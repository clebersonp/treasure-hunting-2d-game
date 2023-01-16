package environment;

import java.awt.Graphics2D;

import main.GamePanel;

public class EnvironmentManager {

	private GamePanel gp;
	private Lighting lighting;

	public EnvironmentManager(GamePanel gp) {
		this.gp = gp;
	}

	public void setup() {
		// Lighting
		this.lighting = new Lighting(this.gp);
	}

	public void update() {
		this.lighting.update();
	}
	
	public void draw(Graphics2D g2) {
		this.lighting.draw(g2);
	}

}
