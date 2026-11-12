package main;

import java.util.List;

import org.joml.Vector3f;

import entities.Entity;
import entities.machine.Conveyor;
import entities.machine.Dropper;
import entities.machine.Furnace;
import entities.ore.Ore;
import models.TexturedModel;

public class AssetSetter {

	private final Assets assets;

	public AssetSetter(Assets assets) {
		this.assets = assets;
	}

	public void setObjects(List<Entity> entities) {
		entities.add(place(assets.stall, 4, 4, 0));
	}

	public void setMachines(
		List<Dropper> droppers,
		List<Conveyor> conveyors, 
		List<Furnace> furnaces
	) {
		droppers.add(placeDropper(6, 4, 0));

		for (int col = 7; col <= 9; col++) conveyors.add(placeConveyor(col, 4, 0));   // +x
		
		furnaces.add(placeFurnace(10, 4, 0));
	}

	private Dropper placeDropper(int col, int row, float rotY) {
		Vector3f position = new Vector3f((Settings.START_COL + col) * Settings.TILE_SIZE, 0, (Settings.START_ROW + row) * Settings.TILE_SIZE);
		return new Dropper(assets.dropper, assets.ore, position, rotY);
	}

	private Conveyor placeConveyor(int col, int row, float rotY) {
		Vector3f position = new Vector3f((Settings.START_COL + col) * Settings.TILE_SIZE, 0, (Settings.START_ROW + row) * Settings.TILE_SIZE);
		return new Conveyor(assets.conveyor, position, rotY);
	}

	private Furnace placeFurnace(int col, int row, float rotY) {
		Vector3f position = new Vector3f((Settings.START_COL + col) * Settings.TILE_SIZE, 0, (Settings.START_ROW + row) * Settings.TILE_SIZE);
		return new Furnace(assets.furnace, position, rotY);
	}

	private static Entity place(TexturedModel model, int col, int row, float rotY) {
		Vector3f position = new Vector3f(
			(Settings.START_COL + col) * Settings.TILE_SIZE, 0, (Settings.START_ROW + row) * Settings.TILE_SIZE);
		return new Entity(model, position, 0, rotY, 0, Settings.TILE_SIZE);
	}
}
