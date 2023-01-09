package main;

import entity.Entity;

public class CollisionChecker {

	private GamePanel gp;

	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}

	public void checkTile(Entity entity) {

		int entityLeftWorldX = entity.getWorldX() + entity.getSolidArea().x;
		int entityRightWorldX = entity.getWorldX() + entity.getSolidArea().x + entity.getSolidArea().width;
		int entityTopWorldY = entity.getWorldY() + entity.getSolidArea().y;
		int entityBottomWorldY = entity.getWorldY() + entity.getSolidArea().y + entity.getSolidArea().height;

		int entityLeftCol = entityLeftWorldX / this.gp.getTileSize();
		int entityRightCol = entityRightWorldX / this.gp.getTileSize();
		int entityTopRow = entityTopWorldY / this.gp.getTileSize();
		int entityBottomRow = entityBottomWorldY / this.gp.getTileSize();

		int tileNum2 = 0;
		int tileNum1 = tileNum2;

		switch (entity.getDirection()) {
		case UP:
			entityTopRow = (entityTopWorldY - entity.getSpeed()) / this.gp.getTileSize();
			tileNum1 = this.gp.getTileManager().getMapTileNum()[this.gp.getCurrentMap()][entityTopRow][entityLeftCol];
			tileNum2 = this.gp.getTileManager().getMapTileNum()[this.gp.getCurrentMap()][entityTopRow][entityRightCol];
			break;
		case DOWN:
			entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / this.gp.getTileSize();
			tileNum1 = this.gp.getTileManager().getMapTileNum()[this.gp
					.getCurrentMap()][entityBottomRow][entityLeftCol];
			tileNum2 = this.gp.getTileManager().getMapTileNum()[this.gp
					.getCurrentMap()][entityBottomRow][entityRightCol];
			break;
		case LEFT:
			entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / this.gp.getTileSize();
			tileNum1 = this.gp.getTileManager().getMapTileNum()[this.gp.getCurrentMap()][entityTopRow][entityLeftCol];
			tileNum2 = this.gp.getTileManager().getMapTileNum()[this.gp
					.getCurrentMap()][entityBottomRow][entityLeftCol];
			break;
		case RIGHT:
			entityRightCol = (entityRightWorldX + entity.getSpeed()) / this.gp.getTileSize();
			tileNum1 = this.gp.getTileManager().getMapTileNum()[this.gp.getCurrentMap()][entityTopRow][entityRightCol];
			tileNum2 = this.gp.getTileManager().getMapTileNum()[this.gp
					.getCurrentMap()][entityBottomRow][entityRightCol];
			break;
		}

		if (this.gp.getTileManager().getTiles()[tileNum1].isCollision()
				|| this.gp.getTileManager().getTiles()[tileNum2].isCollision()) {
			entity.setCollisionOn(Boolean.TRUE);
		}
	}

	public int checkObject(Entity entity, boolean player) {
		int objIndex = -1;
		for (int i = 0; i < this.gp.getObjects()[this.gp.getCurrentMap()].length; i++) {

			if (this.gp.getObjects()[this.gp.getCurrentMap()][i] != null) {
				// Get entity's solid area position
				entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
				entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;

				// Get the object's solid area position
				gp.getObjects()[this.gp.getCurrentMap()][i]
						.getSolidArea().x = gp.getObjects()[this.gp.getCurrentMap()][i].getWorldX()
								+ gp.getObjects()[this.gp.getCurrentMap()][i].getSolidArea().x;
				gp.getObjects()[this.gp.getCurrentMap()][i]
						.getSolidArea().y = gp.getObjects()[this.gp.getCurrentMap()][i].getWorldY()
								+ gp.getObjects()[this.gp.getCurrentMap()][i].getSolidArea().y;

				switch (entity.getDirection()) {
				case UP:
					entity.getSolidArea().y -= entity.getSpeed();
					break;
				case DOWN:
					entity.getSolidArea().y += entity.getSpeed();
					break;
				case LEFT:
					entity.getSolidArea().x -= entity.getSpeed();
					break;
				case RIGHT:
					entity.getSolidArea().x += entity.getSpeed();
					break;
				}

				if (entity.getSolidArea().intersects(this.gp.getObjects()[this.gp.getCurrentMap()][i].getSolidArea())) {
					if (this.gp.getObjects()[this.gp.getCurrentMap()][i].isCollision()) {
						entity.setCollisionOn(Boolean.TRUE);
					}
					if (player) {
						objIndex = i;
					}
				}

				// Reset entity's and object's solid area position to default
				entity.getSolidArea().x = entity.getSolidAreaDefaultX();
				entity.getSolidArea().y = entity.getSolidAreaDefaultY();
				this.gp.getObjects()[this.gp.getCurrentMap()][i]
						.getSolidArea().x = this.gp.getObjects()[this.gp.getCurrentMap()][i].getSolidAreaDefaultX();
				this.gp.getObjects()[this.gp.getCurrentMap()][i]
						.getSolidArea().y = this.gp.getObjects()[this.gp.getCurrentMap()][i].getSolidAreaDefaultY();
			}
		}
		return objIndex;
	}

	// NPC OR MONSTER COLLISION
	public int checkEntity(Entity entity, Entity[][] targets) {
		int objIndex = -1;
		for (int i = 0; i < targets[this.gp.getCurrentMap()].length; i++) {

			if (targets[this.gp.getCurrentMap()][i] != null) {
				// Get entity's solid area position
				entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
				entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;

				// Get the object's solid area position
				targets[this.gp.getCurrentMap()][i].getSolidArea().x = targets[this.gp.getCurrentMap()][i].getWorldX()
						+ targets[this.gp.getCurrentMap()][i].getSolidArea().x;
				targets[this.gp.getCurrentMap()][i].getSolidArea().y = targets[this.gp.getCurrentMap()][i].getWorldY()
						+ targets[this.gp.getCurrentMap()][i].getSolidArea().y;

				switch (entity.getDirection()) {
				case UP:
					entity.getSolidArea().y -= entity.getSpeed();
					break;
				case DOWN:
					entity.getSolidArea().y += entity.getSpeed();
					break;
				case LEFT:
					entity.getSolidArea().x -= entity.getSpeed();
					break;
				case RIGHT:
					entity.getSolidArea().x += entity.getSpeed();
					break;
				}

				// Se houve colisao entre duas Entities porem n em si proprio
				if (entity.getSolidArea().intersects(targets[this.gp.getCurrentMap()][i].getSolidArea())
						&& entity != targets[this.gp.getCurrentMap()][i]) {
					entity.setCollisionOn(Boolean.TRUE);
					objIndex = i;
				}

				// Reset entity's and target's solid area position to default
				entity.getSolidArea().x = entity.getSolidAreaDefaultX();
				entity.getSolidArea().y = entity.getSolidAreaDefaultY();
				targets[this.gp.getCurrentMap()][i].getSolidArea().x = targets[this.gp.getCurrentMap()][i]
						.getSolidAreaDefaultX();
				targets[this.gp.getCurrentMap()][i].getSolidArea().y = targets[this.gp.getCurrentMap()][i]
						.getSolidAreaDefaultY();
			}
		}
		return objIndex;
	}

	public boolean checkPlayer(Entity entity) {

		boolean contactPlayer = false;

		// Get entity's solid area position
		entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
		entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;

		// Get the player's solid area position
		this.gp.getPlayer().getSolidArea().x = this.gp.getPlayer().getWorldX() + this.gp.getPlayer().getSolidArea().x;
		this.gp.getPlayer().getSolidArea().y = this.gp.getPlayer().getWorldY() + this.gp.getPlayer().getSolidArea().y;

		switch (entity.getDirection()) {
		case UP:
			entity.getSolidArea().y -= entity.getSpeed();
			break;
		case DOWN:
			entity.getSolidArea().y += entity.getSpeed();
			break;
		case LEFT:
			entity.getSolidArea().x -= entity.getSpeed();
			break;
		case RIGHT:
			entity.getSolidArea().x += entity.getSpeed();
			break;
		}

		if (entity.getSolidArea().intersects(this.gp.getPlayer().getSolidArea())) {
			entity.setCollisionOn(Boolean.TRUE);
			contactPlayer = true;
		}

		// Reset entity's and target's solid area position to default
		entity.getSolidArea().x = entity.getSolidAreaDefaultX();
		entity.getSolidArea().y = entity.getSolidAreaDefaultY();
		this.gp.getPlayer().getSolidArea().x = this.gp.getPlayer().getSolidAreaDefaultX();
		this.gp.getPlayer().getSolidArea().y = this.gp.getPlayer().getSolidAreaDefaultY();

		return contactPlayer;
	}

}
