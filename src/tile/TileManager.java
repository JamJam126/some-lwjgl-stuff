package tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import entities.Entity;
import main.Settings;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class TileManager {

	public static final int GRASS = 0;
	public static final int DIRT = 1;

	private final TexturedModel[] tileTypes;
	private int[][] mapTileNum; // [row][col]
	private final List<Entity> tiles = new ArrayList<>();

	public TileManager(Loader loader, String mapName) {
		RawModel quad = createTileQuad(loader);
		tileTypes = new TexturedModel[] {
			new TexturedModel(quad, new ModelTexture(loader.loadTexture("grass"))),
			new TexturedModel(quad, new ModelTexture(loader.loadTexture("dirt"))),
		};

		loadMap(mapName);
		createTileEntities();
	}

	public void render(Renderer renderer, StaticShader shader) {
		for (Entity tile : tiles) renderer.render(tile, shader);
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
		for (int row = 0; row < getRows(); row++) {
			for (int col = 0; col < getCols(); col++) {
				TexturedModel model = tileTypes[mapTileNum[row][col]];
				Vector3f position = new Vector3f(col * Settings.TILE_SIZE, 0, row * Settings.TILE_SIZE);
				tiles.add(new Entity(model, position, 0, 0, 0, 1));
			}
		}
	}
}
