package environment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.Objects;

import main.GamePanel;

public class Lighting {

	private final GamePanel gp;
	private BufferedImage darknessFilter;
	private int dayCounter;
	private float filterAlpha = 0f;

	private DayState dayState = DayState.DAY;

	public Lighting(GamePanel gp) {
		this.gp = gp;
		this.setLightSource();
	}

	public void setLightSource() {

		// Create a buffered Image
		this.darknessFilter = new BufferedImage(this.gp.getScreenWidth(), this.gp.getScreenHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) this.darknessFilter.getGraphics();

		if (Objects.isNull(this.gp.getPlayer().getCurrentLight())) {
			g2.setColor(new Color(0, 0, 0.1f, 0.996F));

		} else {
			// Get the center x and y of the ligth circle
			float centerX = this.gp.getPlayer().getScreenX() + (this.gp.getTileSize() / 2);
			float centerY = this.gp.getPlayer().getScreenY() + (this.gp.getTileSize() / 2);

			Color[] colors = new Color[12];
			colors[0] = new Color(0, 0, 0.1f, 0.1F);
			colors[1] = new Color(0, 0, 0.1f, 0.42F);
			colors[2] = new Color(0, 0, 0.1f, 0.52F);
			colors[3] = new Color(0, 0, 0.1f, 0.61F);
			colors[4] = new Color(0, 0, 0.1f, 0.69F);
			colors[5] = new Color(0, 0, 0.1f, 0.76F);
			colors[6] = new Color(0, 0, 0.1f, 0.82F);
			colors[7] = new Color(0, 0, 0.1f, 0.87F);
			colors[8] = new Color(0, 0, 0.1f, 0.91F);
			colors[9] = new Color(0, 0, 0.1f, 0.94F);
			colors[10] = new Color(0, 0, 0.1f, 0.96F);
			colors[11] = new Color(0, 0, 0.1f, 0.996F);

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

			// Circle radius
			float circleRadius = this.gp.getPlayer().getCurrentLight().getLightRadius() / 2;
			// Create a gradation paint settings for the light circle
			RadialGradientPaint radialPaint = new RadialGradientPaint(centerX, centerY, circleRadius, factions, colors);

			// Set the gradient data on g2
			g2.setPaint(radialPaint);
		}

		g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

		g2.dispose();
	}

	public void update() {
		if (this.gp.getPlayer().isLightUpdated()) {
			this.setLightSource();
			this.gp.getPlayer().setLightUpdated(false);
		}

		// check the state of the day
		if (DayState.DAY.equals(this.dayState)) {
			this.dayCounter++;

			if (this.dayCounter > 36000) {
				this.dayState = DayState.DUSK;
				this.dayCounter = 0;
			}
		}
		if (DayState.DUSK.equals(this.dayState)) {
			this.filterAlpha += 0.0001f;

			if (this.filterAlpha > 1f) {
				this.filterAlpha = 1;
				this.dayState = DayState.NIGHT;
			}
		}
		if (DayState.NIGHT.equals(this.dayState)) {
			this.dayCounter++;
			if (this.dayCounter > 36000) {
				this.dayState = DayState.DAWN;
				this.dayCounter = 0;
			}
		}
		if (DayState.DAWN.equals(this.dayState)) {
			this.filterAlpha -= 0.0001f;
			if (this.filterAlpha < 0) {
				this.filterAlpha = 0;
				this.dayState = DayState.DAY;
			}
		}
	}

	public void draw(Graphics2D g2) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.filterAlpha));
		g2.drawImage(this.darknessFilter, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		// DEBUG
		boolean debugOn = true;
		if (debugOn) {
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(50f));
			int x = 760;
			int y = 600;
			g2.drawString(this.dayState.name(), x, y);
			g2.setFont(g2.getFont().deriveFont(20f));
			g2.drawString("DayCounter: " + this.dayCounter, x, y + 20);
			g2.drawString(String.format("Transition: %.3f", this.filterAlpha), x, y + 40);
		}
	}

	public GamePanel getGp() {
		return gp;
	}

	public int getDayCounter() {
		return dayCounter;
	}

	public void setDayCounter(int dayCounter) {
		this.dayCounter = dayCounter;
	}

	public float getFilterAlpha() {
		return filterAlpha;
	}

	public void setFilterAlpha(float filterAlpha) {
		this.filterAlpha = filterAlpha;
	}

	public DayState getDayState() {
		return dayState;
	}

	public void setDayState(DayState dayState) {
		this.dayState = dayState;
	}

	public static enum DayState {
		DAY, DUSK, NIGHT, DAWN;
	}

}
