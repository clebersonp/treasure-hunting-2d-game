package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;

import config.Config;
import entity.Entity;
import entity.NPC_OldMan;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	// SCREEN SETTINGS
	final private int originalTileSize = 16; // 16x16 tile
	final private int scale = 3;

	final private int tileSize = originalTileSize * scale; // 48x48 tile
	final private int maxScreenCol = 20;
	final private int maxScreenRow = 14;
	final private int screenWidth = tileSize * maxScreenCol; // 960 pixels
	final private int screenHeight = tileSize * maxScreenRow; // 576 pixels

	// WORLD SETTINGS
	private final int maxWorldCol = 50;
	private final int maxWorldRow = 50;

	// MAPS
	private final int maxMap = 10;
	private int currentMap = 0;

	// FOR FULL SCREEN
	private int screenWidth2 = this.screenWidth;
	private int screenHeigth2 = this.screenHeight;
	private BufferedImage tempScreen;
	private Graphics2D g2;
	private boolean fullScreenOn = false;

	// FPS
	final private int FPS = 60;

	// SYSTEM
	private TileManager tileManager = new TileManager(this);
	private KeyHandler keyHandler = new KeyHandler(this);
	private Sound music = new Sound(Sound.BLUE_BOY_ADVENTURE, true);

	private CollisionChecker collisionChecker = new CollisionChecker(this);
	private AssetSetter assetSetter = new AssetSetter(this);
	private UI ui = new UI(this);
	private EventHandler eventHandler = new EventHandler(this);
	private Thread gameThread;

	// ENTITY, OBJECTS, MONSTERS, TILES, NPCS
	private Player player = new Player(this, keyHandler);
	private Entity[][] objects = new Entity[this.maxMap][20];
	private Entity[][] npcs = new NPC_OldMan[this.maxMap][10];
	private Entity[][] monsters = new Entity[this.maxMap][20];
	private InteractiveTile[][] interactiveTiles = new InteractiveTile[this.maxMap][50];
	private List<Entity> entities = new ArrayList<>();
	private List<Entity> projectiles = new ArrayList<>();
	private List<Entity> particles = new ArrayList<>();

	// GAME STATE
	private int gameState;
	public static final int TITLE_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int PAUSE_STATE = 2;
	public static final int DIALOGUE_STATE = 3;
	public static final int CHARACTER_STATE = 4;
	public static final int OPTIONS_STATE = 5;
	public static final int GAME_OVER_STATE = 6;
	public static final int TRANSITION_STATE = 7;

	// CONFIG
	private Config config = new Config(this);

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
		this.assetSetter.setMonsters();
		this.assetSetter.setInteractiveTiles();
//		this.playMusic(0);
		this.gameState = TITLE_STATE;

		// BLANK BufferedImage
		this.tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		this.g2 = (Graphics2D) tempScreen.getGraphics();

		if (this.fullScreenOn) {
			this.setFullScreen();
		}
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
				this.drawToTempScreen(); // draw everything to the buffered image
				this.drawToScreen(); // draw the buffered image to the screen
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
			for (int i = 0; i < this.npcs[this.currentMap].length; i++) {
				if (this.npcs[this.currentMap][i] != null) {
					this.npcs[this.currentMap][i].update();
				}
			}

			// MONSTERS
			for (int i = 0; i < this.monsters[this.currentMap].length; i++) {
				if (this.monsters[this.currentMap][i] != null) {
					if (this.monsters[this.currentMap][i].isAlive() && !this.monsters[this.currentMap][i].isDying()) {
						this.monsters[this.currentMap][i].update();
					}
					if (!this.monsters[this.currentMap][i].isAlive()) {
						this.monsters[this.currentMap][i].checkDrop();
						this.monsters[this.currentMap][i] = null;
					}
				}
			}

			// PROJECTILES
			for (int i = 0; i < this.projectiles.size(); i++) {
				if (Objects.nonNull(this.projectiles.get(i))) {
					if (this.projectiles.get(i).isAlive()) {
						this.projectiles.get(i).update();
					} else {
						this.projectiles.remove(i);
					}
				}
			}

			// PARTICLES
			for (int i = 0; i < this.particles.size(); i++) {
				if (Objects.nonNull(this.particles.get(i))) {
					if (this.particles.get(i).isAlive()) {
						this.particles.get(i).update();
					} else {
						this.particles.remove(i);
					}
				}
			}

			// INTERACTIVE TILES
			for (int i = 0; i < this.interactiveTiles[this.currentMap].length; i++) {
				if (Objects.nonNull(this.interactiveTiles[this.currentMap][i])) {
					this.interactiveTiles[this.currentMap][i].update();
				}
			}

		} else if (this.gameState == PAUSE_STATE) {
			// do nothing
		} else if (this.gameState == DIALOGUE_STATE) {

		}
	}

	public void drawToTempScreen() {
		// DEBUG
		long drawStart = System.nanoTime();

		// TITLE SCREEN
		if (this.gameState == TITLE_STATE) {

			// UI
			this.ui.draw(g2);

		} else {
			// TILES
			this.tileManager.draw(g2);

			// INTERACTIVE TILES
			for (int i = 0; i < this.interactiveTiles[this.currentMap].length; i++) {
				if (Objects.nonNull(this.interactiveTiles[this.currentMap][i])) {
					this.interactiveTiles[this.currentMap][i].draw(g2);
				}
			}

			this.entities.add(player);
			for (Entity npc : this.npcs[this.currentMap]) {
				if (npc != null) {
					this.entities.add(npc);
				}
			}
			for (Entity obj : this.objects[this.currentMap]) {
				if (obj != null) {
					this.entities.add(obj);
				}
			}
			for (Entity monster : this.monsters[this.currentMap]) {
				if (monster != null) {
					this.entities.add(monster);
				}
			}
			for (Entity projectile : this.projectiles) {
				if (Objects.nonNull(projectile)) {
					this.entities.add(projectile);
				}
			}
			for (Entity particle : this.particles) {
				if (Objects.nonNull(particle)) {
					this.entities.add(particle);
				}
			}

			// A ordem de desenhar importa, pois o ultimo sobrepoe o anterior
			// Sort entities collection by worldY para sobreposicao de entity
			Collections.sort(this.entities, (e1, e2) -> Integer.compare(e1.getWorldY(), e2.getWorldY()));

			// Draw every Entity
			this.entities.forEach(e -> e.draw(g2));

			// clean the list
			this.entities.clear();

			// UI
			this.ui.draw(g2);
		}

		// DEBUG
		if (this.keyHandler.isCheckDrawTime()) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));
			g2.setColor(new Color(240, 250, 0));
			int yPostion = 480;
			String worldX = "WorldX > " + this.getPlayer().getWorldX();
			g2.drawString(worldX, 10, yPostion);
			String worldY = "WorldY > " + this.getPlayer().getWorldY();
			g2.drawString(worldY, 10, yPostion += 20);
			String row = "Row > " + this.getPlayer().getWorldY() / this.getTileSize();
			g2.drawString(row, 10, yPostion += 20);
			String col = "Col > " + this.getPlayer().getWorldX() / this.getTileSize();
			g2.drawString(col, 10, yPostion += 20);
			String drawPassedMsg = String.format("Draw TIme > %s", passed);
			g2.drawString(drawPassedMsg, 10, yPostion += 20);
//					System.out.println(drawPassedMsg);
		}
	}

	public void drawToScreen() {
		Graphics graphics = this.getGraphics();
		graphics.drawImage(this.tempScreen, 0, 0, this.screenWidth2, this.screenHeigth2, null);
		graphics.dispose();
	}

	public void retry() {
		this.player.setDefaultPositions();
		this.player.restoreLifeAndMana();
		this.assetSetter.setNPC();
		this.assetSetter.setMonsters();
		this.playMusic(this.music);
	}

	public void restart() {
		this.currentMap = 0;
		this.player.setDefaultValues();
		this.player.setInventoryItems();
		this.assetSetter.setObject();
		this.assetSetter.setNPC();
		this.assetSetter.setMonsters();
		this.assetSetter.setInteractiveTiles();
	}

	public void setFullScreen() {
		// GET LOCAL SCREEN DEVICE
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);

		// GET FULL SCREEN WIDTH AND HEIGHT
		this.screenWidth2 = Main.window.getWidth();
		this.screenHeigth2 = Main.window.getHeight();
	}

	public void playMusic(Sound sound) {
		sound.play();
		sound.loop();
	}

	public void stopMusic(Sound sound) {
		sound.stop();
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

	public Entity[][] getObjects() {
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

	public Entity[][] getNpcs() {
		return npcs;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	public Entity[][] getMonsters() {
		return monsters;
	}

	public InteractiveTile[][] getInteractiveTiles() {
		return interactiveTiles;
	}

	public void setInteractiveTiles(InteractiveTile[][] interactiveTiles) {
		this.interactiveTiles = interactiveTiles;
	}

	public Sound getMusic() {
		return music;
	}

	public AssetSetter getAssetSetter() {
		return assetSetter;
	}

	public void setAssetSetter(AssetSetter assetSetter) {
		this.assetSetter = assetSetter;
	}

	public List<Entity> getProjectiles() {
		return projectiles;
	}

	public List<Entity> getParticles() {
		return particles;
	}

	public void setParticles(List<Entity> particles) {
		this.particles = particles;
	}

	public boolean isFullScreenOn() {
		return fullScreenOn;
	}

	public void setFullScreenOn(boolean fullScreenOn) {
		this.fullScreenOn = fullScreenOn;
	}

	public Config getConfig() {
		return config;
	}

	public int getCurrentMap() {
		return currentMap;
	}

	public void setCurrentMap(int currentMap) {
		this.currentMap = currentMap;
	}

	public int getMaxMap() {
		return maxMap;
	}

}
