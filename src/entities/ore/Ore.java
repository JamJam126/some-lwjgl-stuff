package entities.ore;

import org.joml.Vector3f;

import entities.Entity;
import entities.machine.Conveyor;
import items.OreDefinition;
import models.TexturedModel;
import tile.MachineGrid;

public class Ore extends Entity {
	private final OreDefinition definition;
	private int value; // starts at the definition's value, upgraders will raise it

	public Ore(
		TexturedModel model,
		Vector3f position,
		float rotX,
		float rotY,
		float rotZ,
		float scale,
		OreDefinition definition
	) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.definition = definition;
		this.value = definition.value();
	}

	public void update(MachineGrid grid) {
		if (!(grid.get(getCol(), getRow()) instanceof Conveyor below)) return;
		this.increasePosition(below.getDirX() * below.getSpeed(), 0, below.getDirZ() * below.getSpeed());
	}

	public OreDefinition getDefinition() { return definition; }
	public int getValue() { return value; }
}
