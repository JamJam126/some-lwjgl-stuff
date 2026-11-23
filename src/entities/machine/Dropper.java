package entities.machine;

import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

import entities.ore.Ore;
import items.DropperDefinition;
import items.OreDefinition;
import main.Settings;
import models.TexturedModel;

public class Dropper extends Machine {

	private static final float CHUTE_END = 0.64f;

	private final TexturedModel oreModel;
	private final Random random = new Random();
	private final int spawnInterval;
	private final OreDefinition ore;
	private int spawnTimer = 0;

	public Dropper(
		TexturedModel model,
		TexturedModel oreModel,
		Vector3f position,
		float rotY,
		DropperDefinition definition
	) {
		super(model, position, rotY, definition);
		this.oreModel = oreModel;
		this.spawnInterval = definition.spawnInterval();
		this.ore = definition.ore();
	}

	public void update(List<Ore> ores) {
		spawnTimer++;
		if (spawnTimer >= spawnInterval) {
			spawnTimer = 0;
			ores.add(spawnOre());
		}
	}

	private Ore spawnOre() {
		double angle = Math.toRadians(getRotY());
		float offsetX = (float)   Math.cos(angle) * CHUTE_END;
		float offsetZ = (float) - Math.sin(angle) * CHUTE_END;

		Vector3f position = new Vector3f(
			getPosition().x + offsetX * Settings.TILE_SIZE,
			Settings.BELT_TOP * Settings.TILE_SIZE,
			getPosition().z + offsetZ * Settings.TILE_SIZE
		);

		float randomSpin = random.nextFloat() * 360;

		return new Ore(oreModel, position, 0, randomSpin, 0, Settings.TILE_SIZE, ore);
	}
}
