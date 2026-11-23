package entities.machine;

import org.joml.Vector3f;

import entities.Entity;
import items.ItemDefinition;
import main.Settings;
import models.TexturedModel;

public abstract class Machine extends Entity {

	private final ItemDefinition itemDefinition; 
	protected Machine(TexturedModel model, Vector3f position, float rotY, ItemDefinition itemDefinition) {
		super(model, position, 0, rotY, 0, Settings.TILE_SIZE);

		this.itemDefinition = itemDefinition;
	}

	public ItemDefinition getDefinition() { return itemDefinition; }
}
