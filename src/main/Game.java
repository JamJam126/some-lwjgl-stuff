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
import input.MouseHandler;
import items.Inventory;
import items.ItemDefinition;
import items.Items;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import renderEngine.TileHighlight;
import shaders.StaticShader;
import tile.MachineGrid;
import tile.TileManager;
import tile.TilePosition;
import toolbox.MousePicker;
import ui.BuildMenu;
import ui.UIRenderer;

public class Game {
	private static final int UPS = 60;
	private static final double UPDATE_INTERVAL = 1_000_000_000.0 / UPS;
	
	private static final Vector4f PANEL_COLOUR = new Vector4f(0, 0, 0, 0.5f);
	private static final Vector4f MONEY_COLOUR = new Vector4f(1, 0.85f, 0.3f, 1);
	private static final Vector4f SELECTED_COLOUR = new Vector4f(1, 1, 0, 0.4f);
	private static final Vector4f CAN_PLACE = new Vector4f(0, 1, 0, 0.4f);
	private static final Vector4f CANT_PLACE = new Vector4f(1, 0, 0, 0.4f);
	private static final Vector3f NO_TINT = new Vector3f(1, 1, 1);

	private int money = 0;
	private boolean debug = false;
	private final boolean[] keyWasDown = new boolean[GLFW.GLFW_KEY_LAST + 1];
	private ItemDefinition selectedType = null;
	private int currentRotation = 0;
	private TilePosition hoveredTile = null;
	private TilePosition selectedTile = null;

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
	private final List<Furnace> furnaces = new ArrayList<>();
	private final List<Ore> ores = new ArrayList<>();
	private final MachineGrid machineGrid;
	private final Inventory inventory;
	private final MachinePlacer machinePlacer;
	private final DebugRenderer debugRenderer;
	private final MousePicker mousePicker;
	private final TileHighlight tileHighlight;
	private final BuildMenu buildMenu = new BuildMenu();

	public Game() {
		DisplayManager.createDisplay();
		loader = new Loader();
		shader = new StaticShader();
		renderer = new Renderer(shader);
		mousePicker = new MousePicker(camera, renderer);
		uiRenderer = new UIRenderer(loader);
		tileHighlight = new TileHighlight(loader);

		tileManager = new TileManager(loader, "map01");
		machineGrid = new MachineGrid(tileManager.getCols(), tileManager.getRows());
		inventory = new Inventory();
		debugRenderer = new DebugRenderer(loader, tileManager.getCols(), tileManager.getRows());

		Assets assets = new Assets(loader);
		machinePlacer = new MachinePlacer(assets, machineGrid, inventory, droppers, conveyors, furnaces);
		AssetSetter assetSetter = new AssetSetter();
		assetSetter.setInventory(inventory);
		assetSetter.setMachines(machinePlacer);

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
		if (wasKeyPressed(GLFW.GLFW_KEY_F3)) debug = !debug;
		if (wasKeyPressed(GLFW.GLFW_KEY_R))  currentRotation = (currentRotation + 90) % 360;
		if (wasKeyPressed(GLFW.GLFW_KEY_X) && selectedTile != null) {
			machinePlacer.remove(selectedTile.col(), selectedTile.row());
			selectedTile = null;
		}
		
		handleHover();
		
		if (MouseHandler.wasLeftClicked()) {
			ItemDefinition button = buildMenu.getButtonAt(MouseHandler.getX(), MouseHandler.getY());

			if (button != null) {
				selectedType = button; // the menu gets the click, not the tile behind it
				selectedTile = null;

			} else if (hoveredTile == null) {
				selectedTile = null; // clicked off the map

			} else if (selectedType != null) {
				machinePlacer.place(selectedType, hoveredTile.col(), hoveredTile.row(), currentRotation);

			} else if (machineGrid.get(hoveredTile.col(), hoveredTile.row()) != null) {
				selectedTile = hoveredTile;
			
			} else {
				selectedTile = null; // clicked empty ground
			}
		}

		// Key 1 picks the first item in Items.ALL, key 2 the second, and so on (up to 9)
		for (int i = 0; i < Math.min(Items.ALL.size(), 9); i++) {
			if (GLFW.glfwGetKey(DisplayManager.getWindow(), GLFW.GLFW_KEY_1 + i) == GLFW.GLFW_PRESS) {
				selectedTile = null;
				selectedType = Items.ALL.get(i);
			}
		}
		if (GLFW.glfwGetKey(DisplayManager.getWindow(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
			selectedType = null;
			selectedTile = null;
		}

		camera.move();
		for (int i = ores.size() - 1; i >= 0; i--) {
			Ore ore = ores.get(i);
			ore.update(machineGrid);

			if (machineGrid.get(ore.getCol(), ore.getRow()) instanceof Furnace furnace) {
				money += Math.round(ore.getValue() * furnace.getMultiplier());
				ores.remove(i);
			}
		}
		for (Dropper dropper : droppers) {
			dropper.update(ores);
		}
	}

	private void handleHover() {
		if (buildMenu.getButtonAt(MouseHandler.getX(), MouseHandler.getY()) != null) {
			hoveredTile = null; // no tile highlight under the menu
			return;
		}

		TilePosition tile = mousePicker.getTile(MouseHandler.getX(), MouseHandler.getY());
		if (machineGrid.isInside(tile.col(), tile.row())) hoveredTile = tile;
		else hoveredTile = null;
	}

	private boolean wasKeyPressed(int key) {
		boolean down = GLFW.glfwGetKey(DisplayManager.getWindow(), key) == GLFW.GLFW_PRESS;
		boolean pressed = down && !keyWasDown[key];
		keyWasDown[key] = down;

		return pressed;
	}

	private void render() {
		if (DisplayManager.wasResized()) renderer.updateProjectionMatrix(shader);

		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		shader.loadTint(NO_TINT);

		for (Entity entity : entities) renderer.render(entity, shader);
		for (Dropper dropper : droppers) renderer.render(dropper, shader);
		for (Ore ore : ores) {
			shader.loadTint(ore.getDefinition().colour());
			renderer.render(ore, shader);
		}
		shader.loadTint(NO_TINT);
		for (Conveyor conveyor: conveyors) renderer.render(conveyor, shader);
		for (Furnace furnace: furnaces) renderer.render(furnace, shader);

		tileManager.render(renderer, shader, camera);

		shader.stop();

		if (debug) debugRenderer.renderGrid(renderer.getProjectionMatrix(), camera);
		renderHighLight();
		renderUI();
	}

	private void renderHighLight() {
		if (selectedTile != null) tileHighlight.render(renderer.getProjectionMatrix(), camera, selectedTile.col(), selectedTile.row(), SELECTED_COLOUR);
		if (selectedType == null || hoveredTile == null) return;
		Vector4f color = 
			machineGrid.isFree(hoveredTile.col(), hoveredTile.row()) && inventory.getCount(selectedType) > 0
				? CAN_PLACE 
				: CANT_PLACE;

		tileHighlight.render(renderer.getProjectionMatrix(), camera, hoveredTile.col(), hoveredTile.row(), color);
	}

	private void renderUI() {
		uiRenderer.begin();
		if (debug) debugRenderer.renderLabels(uiRenderer, renderer.getProjectionMatrix(), camera, tileManager);

		String moneyText = "$" + money;
		uiRenderer.drawRect(12, 12, uiRenderer.getTextWidth(moneyText, 32) + 24, 48, PANEL_COLOUR);
		uiRenderer.drawText(moneyText, 24, 20, 32, MONEY_COLOUR);

		buildMenu.render(uiRenderer, inventory, selectedType, MouseHandler.getX(), MouseHandler.getY());
		uiRenderer.end();
	}

	private void cleanUp() {
		shader.cleanUp();
		uiRenderer.cleanUp();
		debugRenderer.cleanUp();
		tileHighlight.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
