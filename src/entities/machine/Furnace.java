package entities.machine;

import org.joml.Vector3f;

import items.FurnaceDefinition;
import models.TexturedModel;

public class Furnace extends Machine {
	
	public Furnace(
		TexturedModel model, 
		Vector3f position, 
		float rotY,
		FurnaceDefinition definition
	) {
		super(model, position, rotY, definition);
	}
}
