package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

	private GamePanel gp;
	private Tile[] tiles;
	private int[][][] mapTileNum;

	public TileManager(final GamePanel gp) {
		this.gp = gp;
		this.tiles = new Tile[50];
		this.mapTileNum = new int[this.gp.getMaxMap()][this.gp.getMaxWorldRow()][this.gp.getMaxWorldCol()];

		this.getTileImage();

		this.loadMap("/maps/worldV3.txt", 0);
		this.loadMap("/maps/interior01.txt", 1);
	}

	public void getTileImage() {
		
		// PLACEHOLDER
		this.setup(0, "grass00", false);
		this.setup(1, "grass00", false);
		this.setup(2, "grass00", false);
		this.setup(3, "grass00", false);
		this.setup(4, "grass00", false);
		this.setup(5, "grass00", false);
		this.setup(6, "grass00", false);
		this.setup(7, "grass00", false);
		this.setup(8, "grass00", false);
		this.setup(9, "grass00", false);
		// PLACEHOLDER
		
		this.setup(10, "grass00", false);
		this.setup(11, "grass01", false);
		this.setup(12, "water00", true);
		this.setup(13, "water01", true);
		this.setup(14, "water02", true);
		this.setup(15, "water03", true);
		this.setup(16, "water04", true);
		this.setup(17, "water05", true);
		this.setup(18, "water06", true);
		this.setup(19, "water07", true);
		this.setup(20, "water08", true);
		this.setup(21, "water09", true);
		this.setup(22, "water10", true);
		this.setup(23, "water11", true);
		this.setup(24, "water12", true);
		this.setup(25, "water13", true);
		this.setup(26, "road00", false);
		this.setup(27, "road01", false);
		this.setup(28, "road02", false);
		this.setup(29, "road03", false);
		this.setup(30, "road04", false);
		this.setup(31, "road05", false);
		this.setup(32, "road06", false);
		this.setup(33, "road07", false);
		this.setup(34, "road08", false);
		this.setup(35, "road09", false);
		this.setup(36, "road10", false);
		this.setup(37, "road11", false);
		this.setup(38, "road12", false);
		this.setup(39, "earth", false);
		this.setup(40, "wall", true);
		this.setup(41, "tree", true);
		this.setup(42, "hut", false);
		this.setup(43, "floor01", false);
		this.setup(44, "table01", true);
	}

	private void setup(int index, String fileName, boolean collision) {
		try {
			BufferedImage scaleImage = UtilityTool.scaleImage(
					ImageIO.read(getClass().getResourceAsStream(String.format("/tiles/%s.png", fileName))),
					this.gp.getTileSize(), this.gp.getTileSize());
			this.tiles[index] = new Tile(scaleImage, collision);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadMap(String mapPath, int map) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(mapPath)))) {
			final List<String> lines = br.lines().collect(Collectors.toList());
			for (int row = 0; row < lines.size() && row < this.gp.getMaxWorldRow(); row++) {
				int[] numbers = Arrays.asList(lines.get(row).split("\s")).stream().mapToInt(Integer::parseInt)
						.toArray();
				for (int col = 0; col < this.gp.getMaxWorldCol(); col++) {
					this.mapTileNum[map][row][col] = numbers[col];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(final Graphics2D g2) {
//		int count = 0;

		for (int worldRow = 0; worldRow < this.mapTileNum[this.gp.getCurrentMap()].length && worldRow < this.gp.getMaxWorldRow(); worldRow++) {
			for (int worldCol = 0; worldCol < this.mapTileNum[this.gp.getCurrentMap()][worldRow].length
					&& worldCol < this.gp.getMaxWorldCol(); worldCol++) {
				int tileNum = this.mapTileNum[this.gp.getCurrentMap()][worldRow][worldCol];

				int worldX = worldCol * this.gp.getTileSize();
				int worldY = worldRow * this.gp.getTileSize();

				// posicao do tile pelo seu tamanho - a posicao do player deve estar no world +
				// a posicao do player deve estar em relacao a screen(tela do jogo)
				int screenX = worldX - this.gp.getPlayer().getWorldX() + this.gp.getPlayer().getScreenX();
				int screenY = worldY - this.gp.getPlayer().getWorldY() + this.gp.getPlayer().getScreenY();

				// desenha somente o tamanho da screen(tela)
				if (worldX + this.gp.getTileSize() > this.gp.getPlayer().getWorldX() - this.gp.getPlayer().getScreenX()
						&& worldX - this.gp.getTileSize() < this.gp.getPlayer().getWorldX()
								+ this.gp.getPlayer().getScreenX()
						&& worldY + this.gp.getTileSize() > this.gp.getPlayer().getWorldY()
								- this.gp.getPlayer().getScreenY()
						&& worldY - this.gp.getTileSize() < this.gp.getPlayer().getWorldY()
								+ this.gp.getPlayer().getScreenY()) {
//					count++;
					g2.drawImage(this.tiles[tileNum].getImage(), screenX, screenY, null);
				}
			}
		}
//		System.out.println(String.format("Count tiles: %s", count));
	}

	public int[][][] getMapTileNum() {
		return mapTileNum;
	}

	public Tile[] getTiles() {
		return tiles;
	}

}
