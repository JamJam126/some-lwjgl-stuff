package main;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import debug.DebugRenderer;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.machine.Conveyor;
import entities.machine.Dropper;
import entities.machine.Furnace;
import entities.ore.Ore;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import tile.MachineGrid;
import tile.TileManager;
import ui.UIRenderer;

public class Game {
	private static final int UPS = 60;
	private static final double UPDATE_INTERVAL = 1_000_000_000.0 / UPS;
	
	private static final Vector4f PANEL_COLOUR = new Vector4f(0, 0, 0, 0.5f);
	private static final Vector4f MONEY_COLOUR = new Vector4f(1, 0.85f, 0.3f, 1);

	private int money = 0;
	private boolean debug = false;
	private boolean debugKeyWasDown = false;

	private final Loader loader;
	private final StaticShader shader;
	private final Renderer renderer;
	private final UIRenderer uiRenderer;

	private final Camera camera = new Camera();
	private final Light light = new Light(new Vector3f(2000, 5000, 2000), new Vector3f(1, 1, 1));

	private final TileManager tileManager;
	private final List<Entity> entities = new ArrayList<>();
	private final List<Dropper> droppers = new ArrayList<>();
	private final List<Conveyor> conveyors = new ArrayList<>();
	private final List<Ore> ores = new ArrayList<>();
	private final List<Furnace> furnaces = new ArrayList<>();
	private final MachineGrid machineGrid;
	private final DebugRenderer debugRenderer;

	public Game() {
		DisplayManager.createDisplay();
		loader = new Loader();
		shader = new StaticShader();
		renderer = new Renderer(shader);
		uiRenderer = new UIRenderer(loader);

		tileManager = new TileManager(loader, "map01");
		machineGrid = new MachineGrid(tileManager.getCols(), tileManager.getRows());
		debugRenderer = new DebugRenderer(loader, tileManager.getCols(), tileManager.getRows());

		Assets assets = new Assets(loader);
		AssetSetter assetSetter = new AssetSetter(assets);
		assetSetter.setObjects(entities);
		assetSetter.setMachines(droppers, conveyors, furnaces, machineGrid);

		// Isometric camera: up and back from the start tile along -x/+z
		float d = Settings.CAMERA_DISTANCE;
		float targetX = Settings.CAMERA_START_COL * Settings.TILE_SIZE;
		float targetZ = Settings.CAMERA_START_ROW * Settings.TILE_SIZE;
		camera.getPosition().set(targetX - d, d, targetZ + d);
	}

	public void run() {
		double delta = 0;
		long lastTime = System.nanoTime();

		while (!DisplayManager.shouldClose()) {
			long now = System.nanoTime();
			delta += (now - lastTime) / UPDATE_INTERVAL;
			lastTime = now; 
		
			while (delta >= 1) {
				update();
				delta--;
			}

			render();
			DisplayManager.updateDisplay();
		}

		cleanUp();
	}

	private void update() {
		toggleDebugOnF3();
		camera.move();
		for (int i = ores.size() - 1; i >= 0; i--) {
			Ore ore = ores.get(i);
			ore.update(machineGrid);

			if (isOnFurnace(ore)) {
				money += ore.getValue();
				ores.remove(i);
				System.out.println("Sold ore for $" + ore.getValue() + ", money: $" + money);
			}
		}
		for (Dropper dropper : droppers) {
			dropper.update(ores);
		}
	}

	private void toggleDebugOnF3() {
		boolean down = GLFW.glfwGetKey(DisplayManager.getWindow(), GLFW.GLFW_KEY_F3) == GLFW.GLFW_PRESS;
		if (down && !debugKeyWasDown) debug = !debug; // only on the frame the key goes down
		debugKeyWasDown = down;
	}

	private boolean isOnFurnace(Ore ore) {
		return machineGrid.get(ore.getCol(), ore.getRow()) instanceof Furnace;
	}

	private void render() {
		if (DisplayManager.wasResized()) renderer.updateProjectionMatrix(shader);

		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);

		for (Entity entity : entities) renderer.render(entity, shader);
		for (Dropper dropper : droppers) renderer.render(dropper, shader);
		for (Ore ore : ores) renderer.render(ore, shader);
		for (Conveyor conveyor: conveyors) renderer.render(conveyor, shader);
		for (Furnace furnace: furnaces) renderer.render(furnace, shader);

		tileManager.render(renderer, shader, camera);

		shader.stop();

		if (debug) debugRenderer.renderGrid(renderer.getProjectionMatrix(), camera);

		renderUI();
	}

	private void renderUI() {
		uiRenderer.begin();
		if (debug) debugRenderer.renderLabels(uiRenderer, renderer.getProjectionMatrix(), camera, tileManager);

		String moneyText = "$" + money;
		uiRenderer.drawRect(12, 12, uiRenderer.getTextWidth(moneyText, 32) + 24, 48, PANEL_COLOUR);
		uiRenderer.drawText(moneyText, 24, 20, 32, MONEY_COLOUR);
		uiRenderer.end();
	}

	private void cleanUp() {
		shader.cleanUp();
		uiRenderer.cleanUp();
		debugRenderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
