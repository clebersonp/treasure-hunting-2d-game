package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Entity;
import entity.NPC_OldMan;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	// SCREEN SETTINGS
	final private int originalTileSize = 16; // 16x16 tile
	final private int scale = 3;

	final private int tileSize = originalTileSize * scale; // 48x48 tile
	final private int maxScreenCol = 16;
	final private int maxScreenRow = 12;
	final private int screenWidth = tileSize * maxScreenCol; // 768 pixels
	final private int screenHeight = tileSize * maxScreenRow; // 576 pixels

	// WORLD SETTINGS
	private final int maxWorldCol = 50;
	private final int maxWorldRow = 50;

	// FPS
	final private int FPS = 60;

	// SYSTEM
	private TileManager tileManager = new TileManager(this);
	private KeyHandler keyHandler = new KeyHandler(this);
	private Sound music = new Sound();
	private Sound soundEffects = new Sound();
	private CollisionChecker collisionChecker = new CollisionChecker(this);
	private AssetSetter assetSetter = new AssetSetter(this);
	private UI ui = new UI(this);
	private EventHandler eventHandler = new EventHandler(this);
	private Thread gameThread;

	// ENTITY AND OBJECTS
	private Player player = new Player(this, keyHandler);
	private SuperObject[] objects = new SuperObject[10];

	// NPC
	private Entity[] npcs = new NPC_OldMan[10];

	// GAME STATE
	private int gameState;
	public static final int TITLE_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int PAUSE_STATE = 2;
	public static final int DIALOGUE_STATE = 3;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.DARK_GRAY);
		this.setDoubleBuffered(Boolean.TRUE);

		this.addKeyListener(this.keyHandler);
		this.setFocusable(Boolean.TRUE);

	}

	public void setupGame() {
		this.assetSetter.setObject();
		this.assetSetter.setNPC();
//		this.playMusic(0);
		this.gameState = TITLE_STATE;
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

//	// 2D Game Loop strategy 1
//	@Override
//	public void run() {
//		
//		double drawInterval = 1_000_000_000 / FPS; // 0.01666 seconds
//		double nextDrawTime = drawInterval + System.nanoTime();
//		
//		while (gameThread != null && gameThread.isAlive()) {
//			
//			// 1. UPDATE: update information such as charactere positions
//			update();
//
//			// 2 DRAW: draw the screen with the update information
//			repaint();
//			
//			
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				
//				remainingTime = remainingTime / 1_000_000;  // convert nano to millis seconds
//				
//				if (remainingTime < 0) {
//					remainingTime = 0;
//				}
//				
//				Thread.sleep((long) remainingTime); // value in millis
//				
//				nextDrawTime += drawInterval;
//				
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

	// 2D Game Loop strategy 2 Delta/Acumulator
	@Override
	public void run() {

		double drawInterval = 1_000_000_000 / FPS; // 0.01666 seconds or 1 frame
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime = 0;
//		long timer = 0;
//		int drawCount = 0;

		while (gameThread != null && gameThread.isAlive()) {

			currentTime = System.nanoTime();

//			System.out.println(String.format(
//					"Current Time: %s, Last Time: %s, Timer: %s, CT - LT: %s, ( CT - LT ) / DI: %s, Delta: %s",
//					currentTime, lastTime, timer, (currentTime - lastTime), (currentTime - lastTime) / drawInterval,
//					delta));

			delta += (currentTime - lastTime) / drawInterval;
//			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				// 1. UPDATE: update information such as charactere positions
				update();
				// 2 DRAW: draw the screen with the update information
				repaint();
//				drawCount++;
//				System.out.println(String.format("DrawCount: %s, Timer: %s, Delta: %s", drawCount, timer, delta));
				delta--;
			}

//			if (timer >= 1_000_000_000) {
//				System.out.println(String.format("DrawCount FPS: %s, Timer: %s", drawCount, timer));
//				drawCount = 0;
//				timer = 0;
//			}

		}

	}

	public void update() {
		if (this.gameState == PLAY_STATE) {
			// PLAYER
			this.player.update();

			// NPCS
			for (int i = 0; i < this.npcs.length; i++) {
				if (this.npcs[i] != null) {
					this.npcs[i].update();
				}
			}

		} else if (this.gameState == PAUSE_STATE) {
			// do nothing
		} else if (this.gameState == DIALOGUE_STATE) {

		}
	}

	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// DEBUG
		long drawStart = System.nanoTime();

		// TITLE SCREEN
		if (this.gameState == TITLE_STATE) {

			// UI
			this.ui.draw(g2);

		} else {
			// A ordem de desenhar importa, pois o ultimo sobrepoe o anterior
			// TILES
			this.tileManager.draw(g2);

			// OBJECTS
			this.drawObjects(g2);

			// NPCS
			for (int i = 0; i < this.npcs.length; i++) {
				if (this.npcs[i] != null) {
					this.npcs[i].draw(g2);
				}
			}

			// PLAYER
			this.player.draw(g2);

			// UI
			this.ui.draw(g2);
		}

		// DEBUG
		if (this.keyHandler.isCheckDrawTime()) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.WHITE);
			String drawPassedMsg = String.format("Draw TIme: %s", passed);
			g2.drawString(drawPassedMsg, 10, 400);
			System.out.println(drawPassedMsg);
		}

		g2.dispose();
	}

	private void drawObjects(Graphics2D g2) {
		for (int i = 0; i < this.objects.length; i++) {
			if (this.objects[i] != null) {
				this.objects[i].draw(g2, this);
			}
		}
	}

	public void playMusic(int index) {
		this.music.setFile(index);
		this.music.play();
		this.music.loop();
	}

	public void stopMusic() {
		this.music.stop();
	}

	public void playSoundEffects(int index) {
		this.soundEffects.setFile(index);
		this.soundEffects.play();
	}

	public void finishedGame() {
		this.gameThread = null;
	}

	public int getTileSize() {
		return this.tileSize;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getMaxWorldCol() {
		return maxWorldCol;
	}

	public int getMaxWorldRow() {
		return maxWorldRow;
	}

	public Player getPlayer() {
		return player;
	}

	public TileManager getTileManager() {
		return tileManager;
	}

	public CollisionChecker getCollisionChecker() {
		return collisionChecker;
	}

	public SuperObject[] getObjects() {
		return objects;
	}

	public UI getUi() {
		return ui;
	}

	public int getFPS() {
		return FPS;
	}

	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	public int getPLAY_STATE() {
		return PLAY_STATE;
	}

	public int getPAUSE_STATE() {
		return PAUSE_STATE;
	}

	public Entity[] getNpcs() {
		return npcs;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

}
