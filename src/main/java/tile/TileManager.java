package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

	private GamePanel gp;
	private Tile[] tiles;
	private int[][][] mapTileNum;
	private boolean drawPath = true;
	private List<TileData> tilesData = new ArrayList<>();

	public TileManager(final GamePanel gp) {

		this.gp = gp;
		this.tiles = new Tile[50];
		this.getTileImage();

		this.loadMap("/maps/sample.txt", 0);
//		this.loadMap("/maps/worldV3.txt", 0);
//		this.loadMap("/maps/interior01.txt", 1);
	}

	public void getTileImage() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				this.getClass().getResourceAsStream("/maps/sample_data.txt"), StandardCharsets.UTF_8))) {

			br.lines().forEach(l -> {
				String[] split = l.split(";");
				if (split.length == 3) {
					int index = Integer.valueOf(split[0]);
					String name = split[1];
					boolean solid = Boolean.valueOf(split[2]);
					this.tilesData.add(new TileData(index, name, solid));
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!this.tilesData.isEmpty()) {
			this.tilesData.forEach(t -> {
				this.setup(t.getIndex(), t.getName(), t.isSolid());
			});
		}

		// PLACEHOLDER
//		this.setup(0, "grass00", false);
//		this.setup(1, "grass00", false);
//		this.setup(2, "grass00", false);
//		this.setup(3, "grass00", false);
//		this.setup(4, "grass00", false);
//		this.setup(5, "grass00", false);
//		this.setup(6, "grass00", false);
//		this.setup(7, "grass00", false);
//		this.setup(8, "grass00", false);
//		this.setup(9, "grass00", false);
		// PLACEHOLDER

//		this.setup(10, "grass00", false);
//		this.setup(11, "grass01", false);
//		this.setup(12, "water00", true);
//		this.setup(13, "water01", true);
//		this.setup(14, "water02", true);
//		this.setup(15, "water03", true);
//		this.setup(16, "water04", true);
//		this.setup(17, "water05", true);
//		this.setup(18, "water06", true);
//		this.setup(19, "water07", true);
//		this.setup(20, "water08", true);
//		this.setup(21, "water09", true);
//		this.setup(22, "water10", true);
//		this.setup(23, "water11", true);
//		this.setup(24, "water12", true);
//		this.setup(25, "water13", true);
//		this.setup(26, "road00", false);
//		this.setup(27, "road01", false);
//		this.setup(28, "road02", false);
//		this.setup(29, "road03", false);
//		this.setup(30, "road04", false);
//		this.setup(31, "road05", false);
//		this.setup(32, "road06", false);
//		this.setup(33, "road07", false);
//		this.setup(34, "road08", false);
//		this.setup(35, "road09", false);
//		this.setup(36, "road10", false);
//		this.setup(37, "road11", false);
//		this.setup(38, "road12", false);
//		this.setup(39, "earth", false);
//		this.setup(40, "wall", true);
//		this.setup(41, "tree", true);
//		this.setup(42, "hut", false);
//		this.setup(43, "floor01", false);
//		this.setup(44, "table01", true);
	}

	private void setup(int index, String fileName, boolean collision) {
		try {
			BufferedImage scaleImage = UtilityTool.scaleImage(
					ImageIO.read(getClass().getResourceAsStream(String.format("/tiles/%s", fileName))),
					this.gp.getTileSize(), this.gp.getTileSize());
			this.tiles[index] = new Tile(scaleImage, collision);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadMap(String mapPath, int map) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(mapPath)))) {

			final List<String> lines = br.lines().collect(Collectors.toList());

			int maxWorldRows = lines.size();
			int maxWorldCols = maxWorldRows;
			Optional<String> lineOptional = lines.stream().findAny();
			if (lineOptional.isPresent()) {
				maxWorldCols = lineOptional.get().split("\s").length;
			}

			this.gp.setMaxWorldRow(maxWorldRows);
			this.gp.setMaxWorldCol(maxWorldCols);

			this.mapTileNum = new int[this.gp.getMaxMap()][this.gp.getMaxWorldRow()][this.gp.getMaxWorldCol()];

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

		for (int worldRow = 0; worldRow < this.mapTileNum[this.gp.getCurrentMap()].length
				&& worldRow < this.gp.getMaxWorldRow(); worldRow++) {
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

		if (this.drawPath) {
			for (int i = 0; i < this.gp.getPathFinder().getPathList().size(); i++) {
				g2.setColor(new Color(255, 0, 0, 70));
				int worldX = this.gp.getPathFinder().getPathList().get(i).getCol() * this.gp.getTileSize();
				int worldY = this.gp.getPathFinder().getPathList().get(i).getRow() * this.gp.getTileSize();

				// posicao do tile pelo seu tamanho - a posicao do player deve estar no world +
				// a posicao do player deve estar em relacao a screen(tela do jogo)
				int screenX = worldX - this.gp.getPlayer().getWorldX() + this.gp.getPlayer().getScreenX();
				int screenY = worldY - this.gp.getPlayer().getWorldY() + this.gp.getPlayer().getScreenY();

				g2.fillRect(screenX, screenY, this.gp.getTileSize(), this.gp.getTileSize());
				g2.setColor(Color.black);
				g2.setFont(g2.getFont().deriveFont(12F));
				g2.drawString("" + (i + 1), screenX + 10, screenY + 12);
				g2.drawString("" + screenX, screenX + 10, screenY + 22);
				g2.drawString("" + screenY, screenX + 10, screenY + 32);
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

	public GamePanel getGp() {
		return gp;
	}

	public static final class TileData {
		private final int index;
		private final String name;
		private final boolean solid;

		public TileData(int index, String name, boolean solid) {
			super();
			this.index = index;
			this.name = name;
			this.solid = solid;
		}

		public int getIndex() {
			return index;
		}

		public String getName() {
			return name;
		}

		public boolean isSolid() {
			return solid;
		}

		@Override
		public String toString() {
			return "TileData [index=" + index + ", name=" + name + ", solid=" + solid + "]";
		}

	}

}
