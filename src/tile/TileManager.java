package tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import entities.Camera;
import entities.Entity;
import main.Settings;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class TileManager {

	public static final int GRASS = 0;
	public static final int DIRT = 1;

	private final TexturedModel[] tileTypes;
	private int[][] mapTileNum; // [row][col]
	private Entity[][] tiles;   // [row][col]

	public TileManager(Loader loader, String mapName) {
		RawModel quad = createTileQuad(loader);
		tileTypes = new TexturedModel[] {
			new TexturedModel(quad, new ModelTexture(loader.loadTexture("grass"))),
			new TexturedModel(quad, new ModelTexture(loader.loadTexture("dirt"))),
		};

		loadMap(mapName);
		createTileEntities();
	}

	/** Draws only the tiles around the point the camera is looking at (the map is too big to draw all). */
	public void render(Renderer renderer, StaticShader shader, Camera camera) {
		Vector3f target = getLookAtPoint(camera);
		int radius = (int) Math.ceil(getViewRadius(camera) / Settings.TILE_SIZE) + 1;
		int centerCol = Math.round(target.x / Settings.TILE_SIZE);
		int centerRow = Math.round(target.z / Settings.TILE_SIZE);

		int minRow = Math.max(0, centerRow - radius), maxRow = Math.min(getRows() - 1, centerRow + radius);
		int minCol = Math.max(0, centerCol - radius), maxCol = Math.min(getCols() - 1, centerCol + radius);

		for (int row = minRow; row <= maxRow; row++) {
			for (int col = minCol; col <= maxCol; col++) {
				renderer.render(tiles[row][col], shader);
			}
		}
	}

	/** Where the camera's line of sight hits the ground (y = 0). */
	private static Vector3f getLookAtPoint(Camera camera) {
		double pitch = Math.toRadians(camera.getPitch());
		double yaw = Math.toRadians(camera.getYaw());
		Vector3f pos = camera.getPosition();

		float dirX = (float) (Math.sin(yaw) * Math.cos(pitch));
		float dirY = (float) -Math.sin(pitch);
		float dirZ = (float) (-Math.cos(yaw) * Math.cos(pitch));

		float t = pos.y / -dirY; // distance along the view direction until y = 0
		return new Vector3f(pos.x + dirX * t, 0, pos.z + dirZ * t);
	}

	/** How far from the look-at point the screen corners reach on the ground, in world units. */
	private static float getViewRadius(Camera camera) {
		float aspect = (float) DisplayManager.getWidth() / DisplayManager.getHeight();
		float halfHeight = Renderer.getOrthoHalfHeight();
		float halfWidth = halfHeight * aspect;
		// Looking down at an angle stretches the screen's height across more ground
		float halfDepth = halfHeight / (float) Math.sin(Math.toRadians(camera.getPitch()));
		return (float) Math.sqrt(halfWidth * halfWidth + halfDepth * halfDepth);
	}

	public int getTile(int col, int row) {
		return mapTileNum[row][col];
	}

	public int getRows() {
		return mapTileNum.length;
	}

	public int getCols() {
		return mapTileNum[0].length;
	}

	private RawModel createTileQuad(Loader loader) {
		float size = Settings.TILE_SIZE / 2;

		float[] vertices = {
			-size, 0, -size,
			-size, 0,  size,
			 size, 0,  size,
			 size, 0, -size,
		};

		float[] textureCoords = {
			0, 0,
			0, 1,
			1, 1,
			1, 0,
		};

		float[] normals = {
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
		};

		int[] indices = { 0, 1, 3, 3, 1, 2 };

		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	private void loadMap(String mapName) {
		String path = "res/maps/" + mapName + ".txt";
		List<int[]> rows = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) continue;

				String[] numbers = line.split("\\s+");
				int[] row = new int[numbers.length];
				for (int col = 0; col < numbers.length; col++) {
					row[col] = Integer.parseInt(numbers[col]);
				}
				rows.add(row);
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load map: " + path, e);
		}

		mapTileNum = rows.toArray(new int[0][]);
	}

	private void createTileEntities() {
		tiles = new Entity[getRows()][getCols()];
		for (int row = 0; row < getRows(); row++) {
			for (int col = 0; col < getCols(); col++) {
				TexturedModel model = tileTypes[mapTileNum[row][col]];
				Vector3f position = new Vector3f(col * Settings.TILE_SIZE, 0, row * Settings.TILE_SIZE);
				tiles[row][col] = new Entity(model, position, 0, 0, 0, 1);
			}
		}
	}
}
