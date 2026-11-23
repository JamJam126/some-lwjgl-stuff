package items;

import org.joml.Vector3f;

// Every ore type in the game
public final class Ores {

	public static final OreDefinition IRON = new OreDefinition(
		"iron",
		"Iron",
		10,
		new Vector3f(1, 1, 1)
	);

	public static final OreDefinition GOLD = new OreDefinition(
		"gold",
		"Gold",
		25,
		new Vector3f(1, 0.85f, 0.35f)
	);

	public static final OreDefinition DIAMOND = new OreDefinition(
		"diamond",
		"Diamond",
		60,
		new Vector3f(0.5f, 0.9f, 1)
	);

	private Ores() {}
}
