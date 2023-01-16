package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Lighting {

	private GamePanel gp;
	private BufferedImage darknessFilter;

	public Lighting(GamePanel gp, int circleDiameter) {
		this.gp = gp;

		// Circle radius
		float circleRadius = circleDiameter / 2;

		// Create a buffered Image
		this.darknessFilter = new BufferedImage(this.gp.getScreenWidth(), this.gp.getScreenHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) this.darknessFilter.getGraphics();

		// Get the center x and y of the ligth circle
		float centerX = this.gp.getPlayer().getScreenX() + (this.gp.getTileSize() / 2);
		float centerY = this.gp.getPlayer().getScreenY() + (this.gp.getTileSize() / 2);

		Color[] colors = new Color[12];
		colors[0] = new Color(0, 0, 0, 0.1F);
		colors[1] = new Color(0, 0, 0, 0.42F);
		colors[2] = new Color(0, 0, 0, 0.52F);
		colors[3] = new Color(0, 0, 0, 0.61F);
		colors[4] = new Color(0, 0, 0, 0.69F);
		colors[5] = new Color(0, 0, 0, 0.76F);
		colors[6] = new Color(0, 0, 0, 0.82F);
		colors[7] = new Color(0, 0, 0, 0.87F);
		colors[8] = new Color(0, 0, 0, 0.91F);
		colors[9] = new Color(0, 0, 0, 0.94F);
		colors[10] = new Color(0, 0, 0, 0.96F);
		colors[11] = new Color(0, 0, 0, 0.996F);

		float[] factions = new float[12];
		factions[0] = 0;
		factions[1] = 0.4F;
		factions[2] = 0.5F;
		factions[3] = 0.6F;
		factions[4] = 0.65F;
		factions[5] = 0.7F;
		factions[6] = 0.75F;
		factions[7] = 0.8F;
		factions[8] = 0.85F;
		factions[9] = 0.9F;
		factions[10] = 0.95F;
		factions[11] = 1F;

		// Create a gradation paint settings for the light circle
		RadialGradientPaint radialPaint = new RadialGradientPaint(centerX, centerY, circleRadius, factions, colors);

		// Set the gradient data on g2
		g2.setPaint(radialPaint);

		g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

		g2.dispose();
	}

	public void draw(Graphics2D g2) {
		g2.drawImage(this.darknessFilter, 0, 0, null);
	}

}
