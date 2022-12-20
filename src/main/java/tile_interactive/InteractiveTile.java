package tile_interactive;

import entity.Entity;
import main.GamePanel;

public abstract class InteractiveTile extends Entity {

	private boolean destructible = false;

	public InteractiveTile(GamePanel gp, int col, int row) {
		super(gp);
	}

	public void update() {
		
	}
	
	public boolean isCorrectItem(Entity entity) {
		return false;
	}
	
	public void playSE() {
		
	}
	
	public InteractiveTile getDestroyedForm() {
		return null;
	}
	
	public boolean isDestructible() {
		return destructible;
	}

	public void setDestructible(boolean destructible) {
		this.destructible = destructible;
	}

}
