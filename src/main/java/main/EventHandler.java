package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Objects;

import entity.Entity;

public class EventHandler {

	private GamePanel gp;
	private EventRect[][][] eventRect;
	private int previousEventX, previousEventY;
	private boolean canTouchEvent;

	private int tempMap, tempCol, tempRow;

	public EventHandler(GamePanel gp) {
		super();
		this.gp = gp;

		this.previousEventX = 0;
		this.previousEventY = 0;
		this.canTouchEvent = true;

		this.eventRect = new EventRect[this.gp.getMaxMap()][this.gp.getMaxWorldRow()][this.gp.getMaxWorldCol()];

		for (int map = 0; map < this.gp.getMaxMap(); map++) {
			for (int row = 0; row < this.gp.getMaxWorldRow(); row++) {
				for (int col = 0; col < this.gp.getMaxWorldCol(); col++) {
					// Adicionando 1 rectangle p collision p cada tile do mapa
					this.eventRect[map][row][col] = new EventRect();
					this.eventRect[map][row][col].x = 23; // 23 pixels, centro do tileSize(48 pixels).
					// 48 / 2 = 24, 24 - 1 = 23
					this.eventRect[map][row][col].y = 23;
					this.eventRect[map][row][col].width = 2;
					this.eventRect[map][row][col].height = 2;
					this.eventRect[map][row][col].setEventRectDefaultX(this.eventRect[map][row][col].x);
					this.eventRect[map][row][col].setEventRectDefaultY(this.eventRect[map][row][col].y);
				}
			}
		}
	}

	public void checkEvent() {

		// Check if the player character is more than 1 tile away from the last event
		int xDistance = Math.abs(this.gp.getPlayer().getWorldX() - this.previousEventX);
		int yDistance = Math.abs(this.gp.getPlayer().getWorldY() - this.previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if (distance > this.gp.getTileSize()) {
			this.canTouchEvent = true;
		}

		if (this.canTouchEvent) {
			if (this.hit(0, 27, 16, Entity.Direction.RIGHT)) {
				this.damagePit(GamePanel.DIALOGUE_STATE);
			} else if (this.hit(0, 23, 12, Entity.Direction.UP)) {
				this.healingPool(GamePanel.DIALOGUE_STATE);
			} else if (this.hit(0, 10, 39, Entity.Direction.UP)) {
				this.teleport(1, 12, 13);
			} else if (this.hit(1, 12, 13, Entity.Direction.DOWN)) {
				this.teleport(0, 10, 39);
			} else if (this.hit(1, 12, 9, Entity.Direction.UP)) {
				this.speak(this.gp.getNpcs()[1][0]); // VALOR FIXO
			}
		}

	}
	
	private void speak(Entity entity) {
		if (this.gp.getKeyHandler().isEnterPressed()) {
			this.gp.setGameState(GamePanel.DIALOGUE_STATE);
			this.gp.getPlayer().setAttackCancel(true);
			entity.speak();
		}
	}
	
	public void teleport(int mapNumber, int col, int row) {

		this.gp.setGameState(GamePanel.TRANSITION_STATE);
		this.tempMap = mapNumber;
		this.tempCol = col;
		this.tempRow = row;
		new Sound(Sound.STAIRS, false).play();
	}

	public boolean hit(int map, int col, int row, Entity.Direction reqDirection) {
		boolean hit = false;

		if (map == this.gp.getCurrentMap()) {
			boolean debugCollision = false;
			Graphics2D g2 = (Graphics2D) this.gp.getGraphics();

			if (debugCollision) {
				g2.setColor(new Color(0, 255, 0, 200));
				g2.fillRect(this.gp.getPlayer().getScreenX() + this.gp.getPlayer().getSolidArea().x,
						this.gp.getPlayer().getScreenY() + this.gp.getPlayer().getSolidArea().y,
						this.gp.getPlayer().getSolidArea().width, this.gp.getPlayer().getSolidArea().height);
			}

			// Calcula o solidArea p a posicao do player referente ao world e n mais ao
			// screenSize
			this.gp.getPlayer().getSolidArea().x = this.gp.getPlayer().getWorldX()
					+ this.gp.getPlayer().getSolidArea().x;
			this.gp.getPlayer().getSolidArea().y = this.gp.getPlayer().getWorldY()
					+ this.gp.getPlayer().getSolidArea().y;

			// Desloca o eventRect p o numero de cols/rows e soma a sua posicao x e y
			this.eventRect[map][row][col].x = col * this.gp.getTileSize() + this.eventRect[map][row][col].x;
			this.eventRect[map][row][col].y = row * this.gp.getTileSize() + this.eventRect[map][row][col].y;

			if (debugCollision) {
				System.out.println("Player Rect in the World: " + this.gp.getPlayer().getSolidArea());

				g2.setColor(new Color(255, 0, 0));
				g2.fillRect(
						this.eventRect[map][row][col].x - this.gp.getPlayer().getWorldX()
								+ this.gp.getPlayer().getScreenX(),
						this.eventRect[map][row][col].y - this.gp.getPlayer().getWorldY()
								+ this.gp.getPlayer().getScreenY(),
						this.eventRect[map][row][col].width, this.eventRect[map][row][col].height);
				System.out.println("Rec in cols/rows: " + this.eventRect);
				System.out.println("X/Y cols/rows: "
						+ (this.eventRect[map][row][col].x - this.gp.getPlayer().getWorldX()
								+ this.gp.getPlayer().getScreenX())
						+ ", " + (this.eventRect[map][row][col].y - this.gp.getPlayer().getWorldY()
								+ this.gp.getPlayer().getScreenY()));
			}

			if (this.gp.getPlayer().getSolidArea().intersects(this.eventRect[map][row][col])
					&& !this.eventRect[map][row][col].isEventDone()) {
				if (this.gp.getPlayer().getDirection().equals(reqDirection)
						|| Entity.Direction.ANY.equals(reqDirection)) {
					hit = true;

					this.previousEventX = this.gp.getPlayer().getWorldX();
					this.previousEventY = this.gp.getPlayer().getWorldY();

					if (debugCollision) {
						System.out.println("HIT: Player Rect Position: " + this.gp.getPlayer().getSolidArea()
								+ ", Hit Rect Position: " + this.eventRect);
					}
				}
			}

			// RESET
			this.gp.getPlayer().getSolidArea().x = this.gp.getPlayer().getSolidAreaDefaultX();
			this.gp.getPlayer().getSolidArea().y = this.gp.getPlayer().getSolidAreaDefaultY();
			this.eventRect[map][row][col].x = this.eventRect[map][row][col].getEventRectDefaultX();
			this.eventRect[map][row][col].y = this.eventRect[map][row][col].getEventRectDefaultY();

		}
		return hit;
	}

	public void damagePit(int gameState) {
		this.gp.setGameState(gameState);
		new Sound(Sound.RECEIVE_DAMAGE, false).play();
		this.gp.getUi().setCurrentDialogue("You fall into a pit!");
		this.gp.getPlayer().decreaseLife(1);
		this.canTouchEvent = false;
	}

	public void healingPool(int gameState) {
		if (this.gp.getKeyHandler().isEnterPressed()) {
			this.gp.setGameState(gameState);
			this.gp.getPlayer().setAttackCancel(Boolean.TRUE);
			if (this.gp.getPlayer().getLife() < this.gp.getPlayer().getMaxLife()) {
				new Sound(Sound.POWER_UP, false).play();
				this.gp.getUi().setCurrentDialogue("You drink the water. \nYour life and mana have been \nrecovered.");
				this.gp.getPlayer().resetUpLife(this.gp.getPlayer().getMaxLife());
				this.gp.getPlayer().setMana(this.gp.getPlayer().getMaxMana());

				// RESET Slimes to Teste
				final boolean allMonstersNulls = Arrays.asList(this.gp.getMonsters()).stream()
						.allMatch(m -> Objects.isNull(m));
				if (this.gp.getMonsters().length == 0 || allMonstersNulls) {
					this.gp.getAssetSetter().setMonsters();
				}

			} else {
				this.gp.getUi().setCurrentDialogue("Your life already has been recovered.");
			}
		}
	}

	public int getTempMap() {
		return tempMap;
	}

	public int getTempCol() {
		return tempCol;
	}

	public int getTempRow() {
		return tempRow;
	}

	public int getPreviousEventX() {
		return previousEventX;
	}

	public void setPreviousEventX(int previousEventX) {
		this.previousEventX = previousEventX;
	}

	public int getPreviousEventY() {
		return previousEventY;
	}

	public void setPreviousEventY(int previousEventY) {
		this.previousEventY = previousEventY;
	}

}
