package items;

import java.util.List;

// Every item in the game
public final class Items {

	public static final DropperDefinition BASIC_DROPPER = new DropperDefinition(
		"basic_dropper", 
		"Dropper", 
		50, 
		120,
		Ores.IRON
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
		100,
		1.0f
	);

	public static final DropperDefinition FAST_DROPPER = new DropperDefinition(
		"fast_dropper",
		"Fast Dropper",
		150,
		60,
		Ores.IRON
	);

	public static final DropperDefinition GOLD_DROPPER = new DropperDefinition(
		"gold_dropper",
		"Gold Dropper",
		300,
		120,
		Ores.GOLD
	);

	public static final DropperDefinition DIAMOND_DROPPER = new DropperDefinition(
		"diamond_dropper",
		"Diamond Dropper",
		1000,
		180,
		Ores.DIAMOND
	);

	public static final FurnaceDefinition HOT_FURNACE = new FurnaceDefinition(
		"hot_furnace",
		"Hot Furnace",
		400,
		1.5f
	);

	public static final FurnaceDefinition BLAST_FURNACE = new FurnaceDefinition(
		"blast_furnace",
		"Blast Furnace",
		1200,
		2.0f
	);

	public static final List<ItemDefinition> ALL = List.of(
		BASIC_DROPPER,
		CONVEYOR,
		FURNACE,
		FAST_DROPPER,
		GOLD_DROPPER,
		DIAMOND_DROPPER,
		HOT_FURNACE,
		BLAST_FURNACE
	);

	private Items() {}
}
