package entity;

import main.GamePanel;

public abstract class Projectile extends Entity {

	private Entity user;

	public Projectile(GamePanel gp) {
		super(gp);
	}

	protected abstract void loadImages();

	public void set(int worldX, int worldY, Entity.Direction direction, boolean alive, Entity user) {

		// Config the values to the projectile from the parameters
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.setAlive(alive);
		this.user = user;
		this.setLife(this.getMaxLife()); // Reset the life to the max value every time you shoot it

	}

	@Override
	public void update() {

		// check the collision do projectile em um monstro
		if (EntityType.PLAYER.equals(this.user.getType())) {
			int monsterIndex = this.getGp().getCollisionChecker().checkEntity(this, this.getGp().getMonsters());
			if (monsterIndex != -1) {
				this.getGp().getPlayer().damageMonster(monsterIndex, this.getAttack());
				this.setAlive(Boolean.FALSE); // destroi o projectile
			}
		} else if (EntityType.MONSTER.equals(this.user.getType())) {
			boolean contactPlayer = this.getGp().getCollisionChecker().checkPlayer(this);
			if (!this.getGp().getPlayer().isInvincible() && contactPlayer) {
				// GIVE DAMAGE FOR THE PLAYER
				this.damagePlayer(this.getAttack());
				this.setAlive(Boolean.FALSE); // destroi o projectile apos o dano no player
			}
		}

		// controla a direcao e velocidade o projetil
		switch (direction) {
		case UP -> this.worldY -= this.getSpeed();
		case DOWN -> this.worldY += this.getSpeed();
		case LEFT -> this.worldX -= this.getSpeed();
		case RIGHT -> this.worldX += this.getSpeed();
		}

		// no caso do projetil ele tem um tempo de vida a partir do disparo
		// quando acabar seu tempo de vida sera removido
		this.setLife(this.getLife() - 1);
		if (this.getLife() <= 0) {
			this.setAlive(false);
		}

		// controla a animacao, trocando as sprits a partir do counter
		this.spriteCounter++;
		if (this.spriteCounter > 12) {
			if (this.sprintNum == 1) {
				this.sprintNum = 2;
			} else {
				this.sprintNum = 1;
			}
			this.spriteCounter = 0;
		}
	}

	public boolean haveResource(Entity user) {
		return user.getMana() >= this.getUseCost();
	}

	public void subtractResource(Entity user) {
		int newMana = user.getMana() - this.getUseCost();
		user.setMana(newMana);
	}
}
