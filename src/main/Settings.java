package main;

public final class Settings {

	// WORLD SETTINGS
	public static final float TILE_SIZE = 16; // world units per tile (models are built 1x1, so scale = TILE_SIZE)

	// STARTING AREA: top-left tile of the 16x16 starting layout in map01 (centred on the 160x160 map).
	// AssetSetter places machines relative to this tile.
	public static final int START_COL = 72;
	public static final int START_ROW = 72;

	// CAMERA SETTINGS
	public static final float CAMERA_DISTANCE = 200; // how far the isometric camera sits from its target
	public static final int CAMERA_START_COL = START_COL + 4;
	public static final int CAMERA_START_ROW = START_ROW + 4;

	private Settings() {}
}
