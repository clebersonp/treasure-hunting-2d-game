package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import entity.Entity.EntityType;
import entity.Player;
import environment.Lighting.DayState;
import object.OBJ_CoinBronze;
import object.OBJ_Heart;
import object.OBJ_Mana;

public class UI {

	private Font purisaB, underdog, pressStart;
	private GamePanel gp;
	private String currentDialogue = "";
	private int commandNum = 0;
	private BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
	private boolean merchantMusicOn = false;

	List<String> messages = new ArrayList<>();
	List<Integer> messagesCounter = new ArrayList<>();
	private int playerSlotInventoryCol = 0;
	private int playerSlotInventoryRow = 0;
	private int npcSlotInventoryCol = 0;
	private int npcSlotInventoryRow = 0;
	private int subState = SubState.LEVEL_0;

	// COUNTER
	int counter = 0;
	private boolean isTransitionFadeOut = true;

	private Entity npc;

	public UI(GamePanel gp) {
		this.gp = gp;

		try (InputStream isPurisa = getClass().getResourceAsStream("/fonts/purisa_bold.ttf");
				InputStream isUnderdog = getClass().getResourceAsStream("/fonts/underdog_regular.ttf");
				InputStream isPressStart = getClass().getResourceAsStream("/fonts/pressStart2P_Regular.ttf")) {

			this.purisaB = Font.createFont(Font.TRUETYPE_FONT, isPurisa);
			this.underdog = Font.createFont(Font.TRUETYPE_FONT, isUnderdog);
			this.pressStart = Font.createFont(Font.TRUETYPE_FONT, isPressStart);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// CREATE HUD OBJECT
		Entity obj_Heart = new OBJ_Heart(this.gp);
		this.heart_full = obj_Heart.getImage();
		this.heart_half = obj_Heart.getImage2();
		this.heart_blank = obj_Heart.getImage3();

		// CREATE MANA OBJECT
		Entity obj_Mana = new OBJ_Mana(this.gp);
		this.crystal_full = obj_Mana.getImage();
		this.crystal_blank = obj_Mana.getImage2();

		// CREATE COIN OBJECT
		Entity obj_coin = new OBJ_CoinBronze(this.gp);
		this.coin = obj_coin.getDown1();

	}

	public void draw(Graphics2D g2) {

		g2.setFont(this.underdog);
//		g2.setFont(this.purisaB);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);

		switch (this.gp.getGameState()) {
		case GamePanel.TITLE_STATE -> this.drawTitleScreen(g2);
		case GamePanel.PLAY_STATE -> {
			this.drawPlayerLife(g2);
			this.drawPlayScreen(g2);
			this.drawMessages(g2);
		}
		case GamePanel.PAUSE_STATE -> {
			this.drawPlayerLife(g2);
			this.drawPauseScreen(g2);
		}
		case GamePanel.DIALOGUE_STATE -> {
			this.drawDialogScreen(g2);
		}
		case GamePanel.CHARACTER_STATE -> {
			this.drawCharacterScreen(g2);
			this.drawInventory(g2, this.gp.getPlayer(), true);
		}
		case GamePanel.OPTIONS_STATE -> {
			this.drawOptionsScreen(g2);
		}
		case GamePanel.GAME_OVER_STATE -> {
			this.drawGameOverScreen(g2);
		}
		case GamePanel.TRANSITION_STATE -> {
			this.drawTransition(g2);
		}
		case GamePanel.TRADE_STATE -> {

			this.changeMusicToMerchant();
			this.drawTradeScreen(g2);
		}
		case GamePanel.SLEEP_STATE -> {
			this.drawSleepScreen(g2);
		}
		}

	}

	private void changeMusicToMerchant() {
		if (!this.merchantMusicOn) {
			this.gp.getMusic().stop();
			this.gp.setMusic(new Sound(Sound.MERCHANT_MUSIC, true));
			this.gp.playMusic(this.gp.getMusic());
			this.merchantMusicOn = true;
		}
	}

	private void changeMusicToBlueBoy() {
		if (this.merchantMusicOn) {
			this.gp.getMusic().stop();
			this.gp.setMusic(new Sound(Sound.BLUE_BOY_ADVENTURE, true));
			this.gp.playMusic(this.gp.getMusic());
			this.merchantMusicOn = false;
		}
	}

	private void drawTradeScreen(Graphics2D g2) {
		switch (this.subState) {
		case 0 -> this.tradeSelect(g2);
		case 1 -> this.tradeBuy(g2);
		case 2 -> this.tradeSell(g2);
		}

		// RESET ENTER PRESSED
		this.gp.getKeyHandler().setEnterPressed(false);
	}

	private void tradeSelect(Graphics2D g2) {

		this.drawDialogScreen(g2);

		// DRAW SUB WINDOW
		int x = this.gp.getTileSize() * 14;
		int y = (int) (this.gp.getTileSize() * 4.5);
		int width = this.gp.getTileSize() * 3;
		int height = (int) (this.gp.getTileSize() * 3.5);
		this.drawSubWindow(g2, x, y, width, height);

		// DRAW MENU
		g2.setColor(Color.WHITE);
		String text = "Buy";
		x += this.gp.getTileSize();
		y += this.gp.getTileSize();
		g2.drawString(text, x, y);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", x - 26, y - 2);
			g2.drawString(text, x - 2, y - 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - 24, y);
			g2.drawString(text, x, y);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = 1;
			}
		}

		text = "Sell";
		y += this.gp.getTileSize();
		g2.drawString(text, x, y);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", x - 26, y - 2);
			g2.drawString(text, x - 2, y - 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - 24, y);
			g2.drawString(text, x, y);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = 2;
			}
		}

		text = "Leave";
		y += this.gp.getTileSize();
		g2.drawString(text, x, y);
		if (this.commandNum == 2) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", x - 26, y - 2);
			g2.drawString(text, x - 2, y - 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - 24, y);
			g2.drawString(text, x, y);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = 0;
				this.commandNum = 0;
				this.gp.setGameState(GamePanel.DIALOGUE_STATE);

				this.changeMusicToBlueBoy();

				this.currentDialogue = "Come again, hehe!";
			}
		}

	}

	private void tradeBuy(Graphics2D g2) {
		// DRAW PLAYER INVENTORY
		this.drawInventory(g2, this.gp.getPlayer(), false);

		// DRAW NPC INVENTORY
		this.drawInventory(g2, this.npc, true);

		// DRAW HINT WINDOW
		int x = this.gp.getTileSize() * 2;
		int y = this.gp.getTileSize() * 9;
		int width = this.gp.getTileSize() * 6;
		int height = this.gp.getTileSize() * 2;
		this.drawSubWindow(g2, x, y, width, height);
		g2.drawString("[ESC] Back", x + 24, y + 60);

		// DRAW PLAYER COIN WINDOW
		x = this.gp.getTileSize() * 12;
		y = this.gp.getTileSize() * 9;
		width = this.gp.getTileSize() * 6;
		height = this.gp.getTileSize() * 2;
		this.drawSubWindow(g2, x, y, width, height);
		g2.drawString(String.format("Your Coin: %s", this.gp.getPlayer().getCoin()), x + 24, y + 60);

		// DRAW PRICE INVENTORY WINDOW
		int itemIndex = this.getInventoryItemIndexOnSlot(this.npcSlotInventoryCol, this.npcSlotInventoryRow);
		if (itemIndex < this.npc.getInventory().size()) {

			x = (int) (this.gp.getTileSize() * 5.5);
			y = (int) (this.gp.getTileSize() * 5.5);
			width = (int) (this.gp.getTileSize() * 2.5);
			height = this.gp.getTileSize();
			this.drawSubWindow(g2, x, y, width, height);

			// DRAW COIN IMAGE
			g2.drawImage(this.coin, x + 10, y + 8, 32, 32, null);

			// DRAW PRICE
			int price = this.npc.getInventory().get(itemIndex).getPrice();
			String text = "" + price;
			x = this.getXTextPositionAlignToRight(g2, text, x + width - 12);
			g2.drawString(text, x, y + 32);

			// BUY AN ITEM
			if (this.gp.getKeyHandler().isEnterPressed()) {
				if (this.npc.getInventory().get(itemIndex).getPrice() > this.gp.getPlayer().getCoin()) {
					this.subState = 0;
					this.commandNum = 0;
					this.gp.setGameState(GamePanel.DIALOGUE_STATE);

					this.changeMusicToBlueBoy();

					this.currentDialogue = "You need more coin to buy that!";
					this.drawDialogScreen(g2);
				} else if (this.gp.getPlayer().canObtainItem(this.npc.getInventory().get(itemIndex))) {
					this.gp.getPlayer()
							.setCoin(this.gp.getPlayer().getCoin() - this.npc.getInventory().get(itemIndex).getPrice());
				} else {
					this.subState = 0;
					this.commandNum = 0;
					this.gp.setGameState(GamePanel.DIALOGUE_STATE);

					this.changeMusicToBlueBoy();

					this.currentDialogue = "You cannot carry any item more!";
					this.drawDialogScreen(g2);
				}
			}

		}
	}

	private void tradeSell(Graphics2D g2) {
		// DRAW PLAYER INVENTORY
		this.drawInventory(g2, this.gp.getPlayer(), true);

		// DRAW NPC INVENTORY
		this.drawInventory(g2, this.npc, false);

		// DRAW HINT WINDOW
		int x = this.gp.getTileSize() * 2;
		int y = this.gp.getTileSize() * 9;
		int width = this.gp.getTileSize() * 6;
		int height = this.gp.getTileSize() * 2;
		this.drawSubWindow(g2, x, y, width, height);
		g2.drawString("[ESC] Back", x + 24, y + 60);

		// DRAW PLAYER COIN WINDOW
		x = this.gp.getTileSize() * 12;
		y = this.gp.getTileSize() * 9;
		width = this.gp.getTileSize() * 6;
		height = this.gp.getTileSize() * 2;
		this.drawSubWindow(g2, x, y, width, height);
		g2.drawString(String.format("Your Coin: %s", this.gp.getPlayer().getCoin()), x + 24, y + 60);

		// SELL INVENTORY WINDOW
		int itemIndex = this.getInventoryItemIndexOnSlot(this.playerSlotInventoryCol, this.playerSlotInventoryRow);

		if (itemIndex < this.gp.getPlayer().getInventory().size()) {
			x = (int) (this.gp.getTileSize() * 15.5);
			y = (int) (this.gp.getTileSize() * 5.5);
			width = (int) (this.gp.getTileSize() * 2.5);
			height = this.gp.getTileSize();
			this.drawSubWindow(g2, x, y, width, height);

			// DRAW COIN IMAGE
			g2.drawImage(this.coin, x + 10, y + 8, 32, 32, null);

			// DRAW PRICE
			int price = this.gp.getPlayer().getInventory().get(itemIndex).getPrice() / 2;
			String text = "" + price;
			x = this.getXTextPositionAlignToRight(g2, text, x + width - 12);
			g2.drawString(text, x, y + 32);

			// SELL AN ITEM
			if (this.gp.getKeyHandler().isEnterPressed()) {
				if (this.gp.getPlayer().getInventory().get(itemIndex).equals(this.gp.getPlayer().getCurrentWeapon())
						|| this.gp.getPlayer().getInventory().get(itemIndex)
								.equals(this.gp.getPlayer().getCurrentShield())
						|| this.gp.getPlayer().getInventory().get(itemIndex)
								.equals(this.gp.getPlayer().getCurrentLight())) {
					this.subState = 0;
					this.commandNum = 0;
					this.gp.setGameState(GamePanel.DIALOGUE_STATE);

					this.changeMusicToBlueBoy();

					this.currentDialogue = "You cannot sell an equipped item!";
				} else {
					this.gp.getPlayer().setCoin(this.gp.getPlayer().getCoin() + price);
					if (this.gp.getPlayer().getInventory().get(itemIndex).isStackable()
							&& this.gp.getPlayer().getInventory().get(itemIndex).getAmount() > 1) {
						this.gp.getPlayer().getInventory().get(itemIndex)
								.setAmount(this.gp.getPlayer().getInventory().get(itemIndex).getAmount() - 1);
					} else {
						this.gp.getPlayer().getInventory().remove(itemIndex);
					}
				}
			}
		}
	}

	private void drawTransition(Graphics2D g2) {
		// Fade In
		if (this.isTransitionFadeOut) {
			this.counter++;
			g2.setColor(new Color(0, 0, 0, this.counter * 10));
			g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

			if (this.counter == 25) {
				// Update players position
				this.gp.setCurrentMap(this.gp.getEventHandler().getTempMap());
				Player player = this.gp.getPlayer();
				player.setWorldX(this.gp.getTileSize() * this.gp.getEventHandler().getTempCol());
				player.setWorldY(this.gp.getTileSize() * this.gp.getEventHandler().getTempRow());
				this.gp.getEventHandler().setPreviousEventX(player.getWorldX());
				this.gp.getEventHandler().setPreviousEventY(player.getWorldY());

				// CHANGE TO FADE IN
				this.isTransitionFadeOut = false;
			}
		} else {
			this.counter--;
			g2.setColor(new Color(0, 0, 0, this.counter * 10));
			g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());
			if (counter == 0) {
				this.gp.setGameState(GamePanel.PLAY_STATE);
				this.isTransitionFadeOut = true;
			}
		}
	}

	private void drawGameOverScreen(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));

		text = "Game Over";
		// Shadow
		g2.setColor(Color.BLACK);
		x = this.getXTextPositionCenter(g2, text);
		y = this.gp.getTileSize() * 4;
		g2.drawString(text, x, y);
		// Main
		g2.setColor(Color.WHITE);
		g2.drawString(text, x - 4, y - 4);

		// Retry
		g2.setFont(g2.getFont().deriveFont(50F));
		text = "Retry";
		x = this.getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize() * 4;
		g2.drawString(text, x, y);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.BLACK);
			g2.drawString(">", x - (this.gp.getTileSize() - 4), y + 4);
			g2.drawString(text, x + 4, y + 4);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);
		}

		// Back to the title screen
		text = "Quit";
		x = this.getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize();
		g2.drawString(text, x, y);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.BLACK);
			g2.drawString(">", x - (this.gp.getTileSize() - 4), y + 4);
			g2.drawString(text, x + 4, y + 4);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);
		}

	}

	public void drawOptionsScreen(Graphics2D g2) {

		g2.setColor(Color.WHITE);
		g2.setFont(g2.getFont().deriveFont(32F));

		// SUB WINDOW
		final int frameX = this.gp.getTileSize() * 6;
		final int frameY = this.gp.getTileSize();
		final int frameWidth = this.gp.getTileSize() * 8;
		final int frameHeight = this.gp.getTileSize() * 11;
		this.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

		switch (this.subState) {
		case SubState.LEVEL_0 -> this.optionsTop(g2, frameX, frameY);
		case SubState.LEVEL_1 -> this.optionsFullScreenNotification(g2, frameX, frameY);
		case SubState.LEVEL_2 -> this.optionsControl(g2, frameX, frameY);
		case SubState.LEVEL_3 -> this.optionsEndGameCofirmation(g2, frameX, frameY);
		}

		// RESET ENTER KEY
		this.gp.getKeyHandler().setEnterPressed(false);

	}

	private void optionsTop(Graphics2D g2, int frameX, int frameY) {
		int textX;
		int textY;

		// TITLE
		String text = "Options";
		textX = this.getXTextPositionCenter(g2, text);
		textY = frameY + this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		g2.setFont(g2.getFont().deriveFont(25F));

		// FULL SCREEN ON/OFF
		text = "Full Screen";
		textX = frameX + this.gp.getTileSize();
		textY += this.gp.getTileSize() * 2;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.gp.setFullScreenOn(!this.gp.isFullScreenOn());
				this.subState = SubState.LEVEL_1;
			}
		}

		// MUSIC VOLUME
		text = "Music";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
		}

		// SE VOLUME
		text = "SE";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 2) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
		}

		// CONTROL
		text = "Control";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 3) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_2;
				this.commandNum = 0;
			}
		}

		// END GAME
		text = "End Game";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 4) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_3;
				this.commandNum = 0;
			}

		}

		// BACK
		text = "Back";
		textY += this.gp.getTileSize() * 2;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 5) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);

			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.commandNum = 0;
				this.gp.setGameState(GamePanel.PLAY_STATE);
				// CONFIG
				this.gp.getConfig().save();
			}
		}

		g2.setStroke(new BasicStroke(3F));

		// FULL SCREEN CHECK BOX
		textX = frameX + (int) (this.gp.getTileSize() * 4.5);
		textY = frameY + this.gp.getTileSize() * 2 + 24;
		g2.drawRect(textX, textY, 24, 24);
		if (this.gp.isFullScreenOn()) {
			g2.fillRect(textX, textY, 24, 24);
		}

		// MUSIC VOLUME
		textY += this.gp.getTileSize();
		g2.drawRect(textX, textY, 120, 24);
		g2.fillRect(textX, textY, 24 * this.gp.getMusic().getMusicVolumeScale(), 24);

		// SE VOLUME
		textY += this.gp.getTileSize();
		g2.drawRect(textX, textY, 120, 24);
		g2.fillRect(textX, textY, 24 * Sound.getSeVolumeScale(), 24);

	}

	private void optionsFullScreenNotification(Graphics2D g2, int frameX, int frameY) {
		int textX = frameX + this.gp.getTileSize();
		int textY = frameY + this.gp.getTileSize() * 3;

		this.currentDialogue = "The change will take \neffect after restarting \nthe game.";
		for (String line : this.currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// BACK
		String text = "Back";
		textY += this.gp.getTileSize() * 5;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
			}
		}
	}

	private void optionsControl(Graphics2D g2, int frameX, int frameY) {

		int textX;
		int textY;

		// TITLE
		String text = "Control";
		textX = this.getXTextPositionCenter(g2, text);
		textY = frameY + this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		textX = frameX + this.gp.getTileSize();
		textY += this.gp.getTileSize();

		g2.setFont(g2.getFont().deriveFont(20F));

		text = "Move";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

		text = "Confirm/Attack";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

		text = "Shoot/Cast";
		g2.drawString(text, textX, textY);

		text = "Character Screen";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		text = "Pause";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);

		text = "Options";
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize() * 2;

		text = "Back";
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
				this.commandNum = 3;
			}
		}

		textX = frameX + this.gp.getTileSize() * 6;
		textY = frameY + this.gp.getTileSize() * 2;

		text = "AWSD";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

		text = "ENTER";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "F";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "C";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "P";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();
		text = "ESC";
		g2.drawString(text, textX, textY);
		textY += this.gp.getTileSize();

	}

	private void optionsEndGameCofirmation(Graphics2D g2, int frameX, int frameY) {
		int textX = frameX + this.gp.getTileSize();
		int textY = frameY + this.gp.getTileSize() * 3;

		this.currentDialogue = "Quit the game and \nreturn to the title \nscreen?";

		for (String line : this.currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// YES
		String text = "Yes";
		textX = this.getXTextPositionCenter(g2, text);
		textY += this.gp.getTileSize() * 2;
		g2.drawString(text, textX, textY);
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
				this.gp.setGameState(GamePanel.TITLE_STATE);
				// CONFIG
				this.gp.getConfig().save();
				this.gp.getMusic().stop();
			}
		}

		// NO
		text = "No";
		textX = this.getXTextPositionCenter(g2, text);
		textY += this.gp.getTileSize();
		g2.drawString(text, textX, textY);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (textX - this.gp.getTileSize() / 2), textY + 2);
			g2.drawString(text, textX + 3, textY + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", textX - this.gp.getTileSize() / 2, textY);
			g2.drawString(text, textX, textY);
			if (this.gp.getKeyHandler().isEnterPressed()) {
				this.subState = SubState.LEVEL_0;
				this.commandNum = 4;
			}
		}
	}

	private void drawInventory(Graphics2D g2, Entity entity, boolean cursor) {

		int frameX = 0;
		int frameY = 0;
		int frameWidth = 0;
		int frameHeight = 0;
		int slotCol = 0;
		int slotRow = 0;

		if (entity == this.gp.getPlayer()) {
			frameX = (this.gp.getTileSize() * 12);
			frameY = this.gp.getTileSize();
			frameWidth = this.gp.getTileSize() * 6;
			frameHeight = this.gp.getTileSize() * 5;
			slotCol = this.playerSlotInventoryCol;
			slotRow = this.playerSlotInventoryRow;
		} else {
			frameX = this.gp.getTileSize() * 2;
			frameY = this.gp.getTileSize();
			frameWidth = this.gp.getTileSize() * 6;
			frameHeight = this.gp.getTileSize() * 5;
			slotCol = this.npcSlotInventoryCol;
			slotRow = this.npcSlotInventoryRow;
		}

		// FRAME
		this.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

		// SLOT
		final int slotXStart = frameX + 18;
		final int slotYStart = frameY + 18;
		int slotX = slotXStart;
		int slotY = slotYStart;
		int slotSize = this.gp.getTileSize() + 3;

		// DRAW ENTITY'S INVENTORY ITEMS
		for (int i = 0; i < entity.getInventory().size(); i++) {

			// EQUIP CURSOR
			if (entity.getInventory().get(i) == entity.getCurrentWeapon()
					|| entity.getInventory().get(i) == entity.getCurrentShield()
					|| entity.getInventory().get(i) == entity.getCurrentLight()) {

				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, this.gp.getTileSize(), this.gp.getTileSize(), 10, 10);

			}

			g2.drawImage(entity.getInventory().get(i).getDown1(), slotX, slotY, null);
			if (entity.getInventory().get(i).isStackable() && entity.getInventory().get(i).getAmount() > 1
					&& entity.getType() == EntityType.PLAYER) {
				g2.setColor(Color.white);
				g2.setFont(g2.getFont().deriveFont(20F));
				String text = Integer.toString(entity.getInventory().get(i).getAmount());
				int x = this.getXTextPositionAlignToRight(g2, text, slotX + this.gp.getTileSize());
				int y = slotY + this.gp.getTileSize() - 2;
				g2.drawString(text, x, y);
			}

			slotX += slotSize;
			if (i == 4 || i == 9 || i == 14) {
				slotX = slotXStart;
				slotY += slotSize;
			}
		}

		if (cursor) {
			// CURSOR
			int cursorX = slotXStart + (slotSize * slotCol);
			int cursorY = slotYStart + (slotSize * slotRow);
			int cursorWidth = this.gp.getTileSize();
			int cursorHeight = this.gp.getTileSize();
			// DRAW CURSOR
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(3F));
			g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
			// DESCRIPTION FRAME
			int dFrameX = frameX;
			int dFrameY = frameY + frameHeight;
			int dFrameWidth = frameWidth;
			int dFrameHeight = this.gp.getTileSize() * 3;

			// DRAW DESCRIPTION TEXT
			int textX = dFrameX + 20;
			int textY = dFrameY + this.gp.getTileSize();
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22));

			int itemIndex = this.getInventoryItemIndexOnSlot(slotCol, slotRow);
			if (itemIndex < entity.getInventory().size()) {

				this.drawSubWindow(g2, dFrameX, dFrameY, dFrameWidth, dFrameHeight);

				final Entity item = entity.getInventory().get(itemIndex);
				String description = item.getDescription();
				for (String line : description.split("\n")) {
					g2.drawString(line, textX, textY);
					textY += 32;
				}
			}
		}
	}

	public int getInventoryItemIndexOnSlot(int slotCol, int slotRow) {
		return slotCol + (slotRow * 5);
	}

	private void drawMessages(Graphics2D g2) {

		int messageX = this.gp.getTileSize();
		int messageY = this.gp.getTileSize() * 4;

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

		for (int i = 0; i < this.messages.size(); i++) {

			if (this.messages.get(i) != null) {

				// SHADOW
				g2.setColor(Color.BLACK);
				g2.drawString(this.messages.get(i), messageX + 2, messageY + 2);

				g2.setColor(Color.WHITE);
				g2.drawString(this.messages.get(i), messageX, messageY);

				int counter = this.messagesCounter.get(i) + 1;
				this.messagesCounter.set(i, counter);

				messageY += 50;

				if (this.messagesCounter.get(i) > 180) { // 3 seconds
					this.messages.remove(i);
					this.messagesCounter.remove(i);
				}

			}
		}
	}

	private void drawPlayerLife(Graphics2D g2) {
		int x = this.gp.getTileSize() / 2;
		int y = this.gp.getTileSize() / 2;
		int i = 0;

		// DRAW MAX LIFE
		while (i < this.gp.getPlayer().getMaxLife() / 2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += this.gp.getTileSize();
		}

		// RESET
		x = this.gp.getTileSize() / 2;
		y = this.gp.getTileSize() / 2;
		i = 0;

		// DRAW CURRENT LIFE
		while (i < this.gp.getPlayer().getLife()) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if (i < this.gp.getPlayer().getLife()) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += this.gp.getTileSize();
		}

		// DRAW MAX MANA
		x = this.gp.getTileSize() / 2;
		y = this.gp.getTileSize() + 25;
		i = 0;
		while (i < this.gp.getPlayer().getMaxMana()) {
			g2.drawImage(this.crystal_blank, x, y, null);
			i++;
			x += 33;
		}

		// DRAW MANA
		x = this.gp.getTileSize() / 2;
		y = this.gp.getTileSize() + 25;
		i = 0;
		while (i < this.gp.getPlayer().getMana()) {
			g2.drawImage(crystal_full, x, y, null);
			i++;
			x += 33;
		}
	}

	private void drawTitleScreen(Graphics2D g2) {
		// BACKGROUND COLOR
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

		// TITLE NAME
		g2.setFont(this.pressStart.deriveFont(Font.BOLD, 35F));
		String text = "Blue Boy Adventure";
		int x = this.getXTextPositionCenter(g2, text);
		int y = this.gp.getTileSize() * 3;

		// SHADOW COLOR
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(text, x + 7, y + 5);
		// MAIN COLOR
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);

		// Blue Boy image
		x = this.gp.getScreenWidth() / 2 - (this.gp.getTileSize() * 2) / 2;
		y += this.gp.getTileSize();
		g2.drawImage(this.gp.getPlayer().getDown1(), x, y, this.gp.getTileSize() * 2, this.gp.getTileSize() * 2, null);

		// MENU
		g2.setFont(this.pressStart.deriveFont(Font.PLAIN, 25F));
		text = "NEW GAME";
		x = getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize() * 4;
		if (this.commandNum == 0) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (x - this.gp.getTileSize()) + 3, y + 2);
			g2.drawString(text, x + 3, y + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);
		} else {
			g2.setColor(Color.WHITE);
			g2.drawString(text, x, y);
		}

		text = "LOAD GAME";
		x = getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize();
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);
		if (this.commandNum == 1) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (x - this.gp.getTileSize()) + 3, y + 2);
			g2.drawString(text, x + 3, y + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);

		} else {
			g2.setColor(Color.WHITE);
			g2.drawString(text, x, y);
		}

		text = "QUIT";
		x = getXTextPositionCenter(g2, text);
		y += this.gp.getTileSize();
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);
		if (this.commandNum == 2) {
			// SHADOW
			g2.setColor(Color.DARK_GRAY);
			g2.drawString(">", (x - this.gp.getTileSize()) + 3, y + 2);
			g2.drawString(text, x + 3, y + 2);

			g2.setColor(Color.WHITE);
			g2.drawString(">", x - this.gp.getTileSize(), y);
			g2.drawString(text, x, y);

		} else {
			g2.setColor(Color.WHITE);
			g2.drawString(text, x, y);
		}
	}

	private void drawDialogScreen(Graphics2D g2) {

		// WINDOW
		int x = this.gp.getTileSize() * 3;
		int y = this.gp.getTileSize() / 2;
		int width = this.gp.getScreenWidth() - (this.gp.getTileSize() * 6);
		int height = this.gp.getTileSize() * 4;

		this.drawSubWindow(g2, x, y, width, height);

		x += this.gp.getTileSize();
		y += this.gp.getTileSize();
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		g2.setColor(Color.WHITE);
		for (String line : this.currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
	}

	private void drawCharacterScreen(Graphics2D g2) {
		// CREATE A FRAME
		final int frameX = this.gp.getTileSize() / 2;
		final int frameY = this.gp.getTileSize() / 2;
		final int frameWidth = this.gp.getTileSize() * 5;
		final int frameHeight = (this.gp.getTileSize() * 10) + 10;

		this.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

		// TEXT
		g2.setColor(Color.WHITE);
		g2.setFont(g2.getFont().deriveFont(28F));

		int textX = frameX + 20;
		int textY = frameY + this.gp.getTileSize();
		final int lineHeight = 38;

		// NAMES
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += this.gp.getPlayer().getCurrentWeapon().getDown1().getHeight() - 10;
		g2.drawString("Weapon", textX, textY);
		textY += this.gp.getPlayer().getCurrentShield().getDown1().getHeight() - 10;
		g2.drawString("Shield", textX, textY);

		// VALUES
		int tailX = (frameX + frameWidth) - 30;
		// RESET textY
		textY = frameY + this.gp.getTileSize();
		String value;

		value = String.valueOf(this.gp.getPlayer().getLevel());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getLife() + "/" + this.gp.getPlayer().getMaxLife());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getMana() + "/" + this.gp.getPlayer().getMaxMana());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getStrength());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getDexterity());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getAttack());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getDefense());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getExp());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getNextLevelExp());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(this.gp.getPlayer().getCoin());
		textX = this.getXTextPositionAlignToRight(g2, value, tailX);
		g2.drawString(value, textX, textY);
		textY += 10;

		textX = tailX - this.gp.getPlayer().getCurrentWeapon().getDown1().getWidth() + 10;
		g2.drawImage(this.gp.getPlayer().getCurrentWeapon().getDown1(), textX, textY, this.gp.getTileSize() - 10,
				this.gp.getTileSize() - 10, null);
		textY += this.gp.getPlayer().getCurrentWeapon().getDown1().getHeight() - 10;

		textX = tailX - this.gp.getPlayer().getCurrentShield().getDown1().getWidth() + 10;
		g2.drawImage(this.gp.getPlayer().getCurrentShield().getDown1(), textX, textY, this.gp.getTileSize() - 10,
				this.gp.getTileSize() - 10, null);

	}

	private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {

		Color black = new Color(0, 0, 0, 210);
		g2.setColor(black);
		g2.fillRoundRect(x, y, width, height, 25, 25);

		Color white = new Color(255, 255, 255, 180);
		g2.setStroke(new BasicStroke(5F));
		g2.setColor(white);
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 15, 15);
	}

	private void drawPlayScreen(Graphics2D g2) {
		// TODO Auto-generated method stub
	}

	public void drawPauseScreen(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

		String text = "PAUSED";
		int x = this.getXTextPositionCenter(g2, text);
		int y = this.gp.getScreenHeight() / 2;

		g2.drawString(text, x, y);
	}

	public void drawSleepScreen(Graphics2D g2) {
		this.counter++;
		if (this.counter < 120) {
			this.gp.geteManager().getLighting()
					.setFilterAlpha(this.gp.geteManager().getLighting().getFilterAlpha() + 0.01f);
			if (this.gp.geteManager().getLighting().getFilterAlpha() >= 1f) {
				this.gp.geteManager().getLighting().setFilterAlpha(1f);
			}
		}
		if (this.counter >= 120) {
			this.gp.geteManager().getLighting()
					.setFilterAlpha(this.gp.geteManager().getLighting().getFilterAlpha() - 0.01f);
			if (this.gp.geteManager().getLighting().getFilterAlpha() <= 0f) {
				this.counter = 0;
				this.gp.geteManager().getLighting().setFilterAlpha(0f);
				this.gp.geteManager().getLighting().setDayState(DayState.DAY);
				this.gp.geteManager().getLighting().setDayCounter(0);
				this.gp.setGameState(GamePanel.PLAY_STATE);
				this.gp.getPlayer().loadPlayerImages();
			}
		}
	}

	private int getXTextPositionCenter(Graphics2D g2, String text) {
		int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return this.gp.getScreenWidth() / 2 - textLength / 2;
	}

	private int getXTextPositionAlignToRight(Graphics2D g2, String text, int tailX) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return tailX - length;
	}

	public void addMessage(String text) {
		this.messages.add(text);
		this.messagesCounter.add(0);
	}

	public String getCurrentDialogue() {
		return currentDialogue;
	}

	public void setCurrentDialogue(String currentDialogue) {
		this.currentDialogue = currentDialogue;
	}

	public int getCommandNum() {
		return commandNum;
	}

	public void setCommandNum(int commandNum) {
		this.commandNum = commandNum;
	}

	public int getSubState() {
		return subState;
	}

	public void setSubState(int subState) {
		this.subState = subState;
	}

	public Entity getNpc() {
		return npc;
	}

	public void setNpc(Entity npc) {
		this.npc = npc;
	}

	public int getPlayerSlotInventoryCol() {
		return playerSlotInventoryCol;
	}

	public void setPlayerSlotInventoryCol(int playerSlotInventoryCol) {
		this.playerSlotInventoryCol = playerSlotInventoryCol;
	}

	public int getPlayerSlotInventoryRow() {
		return playerSlotInventoryRow;
	}

	public void setPlayerSlotInventoryRow(int playerSlotInventoryRow) {
		this.playerSlotInventoryRow = playerSlotInventoryRow;
	}

	public int getNpcSlotInventoryCol() {
		return npcSlotInventoryCol;
	}

	public void setNpcSlotInventoryCol(int npcSlotInventoryCol) {
		this.npcSlotInventoryCol = npcSlotInventoryCol;
	}

	public int getNpcSlotInventoryRow() {
		return npcSlotInventoryRow;
	}

	public void setNpcSlotInventoryRow(int npcSlotInventoryRow) {
		this.npcSlotInventoryRow = npcSlotInventoryRow;
	}

	public boolean isMerchantMusicOn() {
		return merchantMusicOn;
	}

	public void setMerchantMusicOn(boolean merchantMusicOn) {
		this.merchantMusicOn = merchantMusicOn;
	}

	public static interface SubState {
		public static final int LEVEL_0 = 0;
		public static final int LEVEL_1 = 1;
		public static final int LEVEL_2 = 2;
		public static final int LEVEL_3 = 3;
	}

}
