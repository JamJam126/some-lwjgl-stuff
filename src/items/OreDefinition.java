package items;

import org.joml.Vector3f;

public record OreDefinition(
	String id,
	String name,
	int value,
	Vector3f colour
) {}
