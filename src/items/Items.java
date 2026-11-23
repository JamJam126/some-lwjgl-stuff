package items;

import java.util.List;

// Every item in the game
public final class Items {

	public static final DropperDefinition BASIC_DROPPER = new DropperDefinition(
		"basic_dropper", 
		"Dropper", 
		50, 
		120, 
		10
	);
	
	public static final ConveyorDefinition CONVEYOR = new ConveyorDefinition(
		"conveyor", 
		"Conveyor", 
		10, 
		0.1f
	);
	
	public static final FurnaceDefinition FURNACE = new FurnaceDefinition(
		"furnace", 
		"Furnace", 
		100
	);

	public static final List<ItemDefinition> ALL = List.of(BASIC_DROPPER, CONVEYOR, FURNACE);

	private Items() {}
}
