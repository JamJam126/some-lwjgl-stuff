package tester;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.w3c.dom.Text;

import entities.Camera;
import entities.Entity;
import entities.Light;

import models.RawModel;	
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		int[] map = {
			0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0,
			0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
			1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0,
			1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0,
			0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1,
			0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
		};

		int MAP_WIDTH = 16;
		float TILE_SIZE = 16;
		float SIZE = TILE_SIZE / 2;
		float TILING = 1;

		float[] vertices = {
			-SIZE, 0, -SIZE,
			-SIZE, 0,  SIZE,
			 SIZE, 0,  SIZE,
			 SIZE, 0, -SIZE,
		};

		float[] textureCoords = {
			0, 			0,
			0, 			TILING,
			TILING, TILING,
			TILING, 0,
		};

		float[] normals = {
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
		};

		int[] indices = { 0, 1, 3, 3, 1, 2 };

		RawModel model = loader.loadToVAO(vertices, textureCoords, normals, indices);

		TexturedModel grassModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("grass")));
		TexturedModel dirtModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("dirt")));

		RawModel stallModel = OBJLoader.loadObjModel("stall/stall-red", loader);
		TexturedModel stallTextured = new TexturedModel(stallModel, new ModelTexture(loader.loadTexture("stall/colormap")));
		Entity stall = new Entity(stallTextured, new Vector3f(4 * TILE_SIZE, 0, 4 * TILE_SIZE), 0, 0, 0, 16);

		RawModel dropperModel = OBJLoader.loadObjModel("dropper/dropper", loader);
		TexturedModel dropperTextured = new TexturedModel(dropperModel, new ModelTexture(loader.loadTexture("dropper/colormap")));
		Entity dropper = new Entity(dropperTextured, new Vector3f(6 * TILE_SIZE, 0, 4 * TILE_SIZE), 0, 0, 0, TILE_SIZE);

		List<Entity> tiles = new ArrayList<>();
		for (int z = 0; z < MAP_WIDTH; z++) {
			for (int x = 0; x < MAP_WIDTH; x++) {
				TexturedModel m = map[z * MAP_WIDTH + x] == 1 ? dirtModel : grassModel;
				tiles.add(new Entity(m, new Vector3f(x * TILE_SIZE, 0, z * TILE_SIZE), 0, 0, 0, 1));
			}
		}

		Camera camera = new Camera();
		float d = 200;
		camera.getPosition().set(4 * TILE_SIZE - d, d, 4 * TILE_SIZE + d); 

		Light light = new Light(new Vector3f(2000, 5000, 2000), new Vector3f(1, 1, 1));

		while (!DisplayManager.shouldClose()) {

			camera.move();

			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(stall, shader);
			renderer.render(dropper, shader);
			for (Entity tile : tiles) renderer.render(tile, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
