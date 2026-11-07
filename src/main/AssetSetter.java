package main;

import java.util.List;

import org.joml.Vector3f;

import entities.Entity;
import entities.machine.Conveyor;
import entities.ore.Ore;
import models.TexturedModel;

public class AssetSetter {

	private static final float BELT_TOP = 0.148f;

	private final Assets assets;

	public AssetSetter(Assets assets) {
		this.assets = assets;
	}

	public void setObjects(List<Entity> entities) {
		entities.add(place(assets.stall, 4, 4, 0));
	}

	public void setMachines(List<Entity> entities, List<Conveyor> conveyors, List<Ore> ores) {
		entities.add(place(assets.dropper, 6, 4, 180));

		for (int col = 7; col <= 9; col++) conveyors.add(placeConveyor(col, 4, 0));   // +x
		for (int row = 4; row <= 7; row++) conveyors.add(placeConveyor(10, row, 270)); // corner, then +z
		// for (int col = 7; col <= 10; col++) {
		// 	conveyors.add(placeConveyor(col, 4, 0));
		// }

		// conveyors.add(placeConveyor(7, 6, 270));

		float[][] orePreview = { 
			{ 7.25f, 0.1f, 20 }, 
			// { 8.0f, -0.12f, 75 }, 
			// { 8.7f, 0.08f, 140 }, 
			// { 9.6f, -0.05f, 250 } 
		};
		for (float[] o : orePreview) {
			Vector3f position = new Vector3f(Settings.START_COL + o[0], BELT_TOP, Settings.START_ROW + 4 + o[1]).mul(Settings.TILE_SIZE);
			ores.add(new Ore(assets.ore, position, 0, o[2], 0, Settings.TILE_SIZE));
		}
	}

	private Conveyor placeConveyor(int col, int row, float rotY) {
		Vector3f position = new Vector3f((Settings.START_COL + col) * Settings.TILE_SIZE, 0, (Settings.START_ROW + row) * Settings.TILE_SIZE);
		return new Conveyor(assets.conveyor, position, rotY);
	}

	private static Entity place(TexturedModel model, int col, int row, float rotY) {
		Vector3f position = new Vector3f(
			(Settings.START_COL + col) * Settings.TILE_SIZE, 0, (Settings.START_ROW + row) * Settings.TILE_SIZE);
		return new Entity(model, position, 0, rotY, 0, Settings.TILE_SIZE);
	}
}
