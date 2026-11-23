package items;

public record ConveyorDefinition(
	String id, 
	String name, 
	int price, 
	float speed
) implements ItemDefinition {}
