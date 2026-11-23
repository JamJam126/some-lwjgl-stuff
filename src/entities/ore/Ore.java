package entities.ore;

import org.joml.Vector3f;

import entities.Entity;
import entities.machine.Conveyor;
import models.TexturedModel;
import tile.MachineGrid;

public class Ore extends Entity {
	private final int value;

	public Ore(
		TexturedModel model, 
		Vector3f position, 
		float rotX, 
		float rotY, 
		float rotZ, 
		float scale,
		int value
	) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.value = value;
	}
	
	public void update(MachineGrid grid) {
		if (!(grid.get(getCol(), getRow()) instanceof Conveyor below)) return;
		this.increasePosition(below.getDirX() * below.getSpeed(), 0, below.getDirZ() * below.getSpeed());
	}

	public int getValue() { return value; }
}
