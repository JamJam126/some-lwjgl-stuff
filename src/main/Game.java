package main;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import tile.TileManager;

public class Game {

	private final Loader loader;
	private final StaticShader shader;
	private final Renderer renderer;

	private final Camera camera = new Camera();
	private final Light light = new Light(new Vector3f(2000, 5000, 2000), new Vector3f(1, 1, 1));

	private final TileManager tileManager;
	private final List<Entity> entities = new ArrayList<>();

	public Game() {
		DisplayManager.createDisplay();
		loader = new Loader();
		shader = new StaticShader();
		renderer = new Renderer(shader);

		tileManager = new TileManager(loader, "map01");

		Assets assets = new Assets(loader);
		AssetSetter assetSetter = new AssetSetter(assets);
		assetSetter.setObjects(entities);
		assetSetter.setMachines(entities);

		// Isometric camera: up and back from the start tile along -x/+z
		float d = Settings.CAMERA_DISTANCE;
		float targetX = Settings.CAMERA_START_COL * Settings.TILE_SIZE;
		float targetZ = Settings.CAMERA_START_ROW * Settings.TILE_SIZE;
		camera.getPosition().set(targetX - d, d, targetZ + d);
	}

	public void run() {
		while (!DisplayManager.shouldClose()) {
			update();
			render();
			DisplayManager.updateDisplay();
		}
		cleanUp();
	}

	private void update() {
		camera.move();
	}

	private void render() {
		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);

		for (Entity entity : entities) renderer.render(entity, shader);
		tileManager.render(renderer, shader);

		shader.stop();
	}

	private void cleanUp() {
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
