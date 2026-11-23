package items;

public record FurnaceDefinition(
	String id, 
	String name, 
	int price
) implements ItemDefinition {}
