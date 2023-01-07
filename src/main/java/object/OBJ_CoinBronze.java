package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_CoinBronze extends Entity {

	public OBJ_CoinBronze(GamePanel gp) {
		super(gp);
		this.setName("Bronze Coin");
		this.setType(EntityType.PICKUP_ONLY);
		this.setValue(1);
		this.loadImages();
	}

	@Override
	protected void loadImages() {
		this.down1 = this.setup("/objects/coin_bronze.png", this.getGp().getTileSize(), this.getGp().getTileSize());
	}

	@Override
	public boolean use(Entity entity) {

		new Sound(Sound.COIN, false).play();
		this.getGp().getUi().addMessage("Coin +" + this.getValue());
		this.getGp().getPlayer().setCoin(this.getGp().getPlayer().getCoin() + 1);

		return true;
	}

}
