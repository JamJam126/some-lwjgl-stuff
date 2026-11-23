package items;

public record FurnaceDefinition(
	String id,
	String name,
	int price,
	float multiplier
) implements ItemDefinition {}
