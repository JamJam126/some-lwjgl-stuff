package main;

import java.util.List;

import org.joml.Vector3f;

import entities.Entity;
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

	public void setMachines(List<Entity> entities) {
		entities.add(place(assets.dropper, 6, 4, 180));

		for (int col = 7; col <= 10; col++) {
			entities.add(place(assets.conveyor, col, 4, 0));
		}

		float[][] orePreview = { { 7.25f, 0.1f, 20 }, { 8.0f, -0.12f, 75 }, { 8.7f, 0.08f, 140 }, { 9.6f, -0.05f, 250 } };
		for (float[] o : orePreview) {
			Vector3f position = new Vector3f(o[0], BELT_TOP, 4 + o[1]).mul(Settings.TILE_SIZE);
			entities.add(new Entity(assets.ore, position, 0, o[2], 0, Settings.TILE_SIZE));
		}
	}

	private static Entity place(TexturedModel model, int col, int row, float rotY) {
		Vector3f position = new Vector3f(col * Settings.TILE_SIZE, 0, row * Settings.TILE_SIZE);
		return new Entity(model, position, 0, rotY, 0, Settings.TILE_SIZE);
	}
}
