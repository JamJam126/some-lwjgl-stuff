package items;

public record DropperDefinition(
	String id,
	String name,
	int price,
	int spawnInterval,
	OreDefinition ore
) implements ItemDefinition {}
