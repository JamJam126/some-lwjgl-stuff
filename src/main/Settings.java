package main;

public final class Settings {

	// WORLD SETTINGS
	public static final float TILE_SIZE = 16; // world units per tile (models are built 1x1, so scale = TILE_SIZE)

	// CAMERA SETTINGS
	public static final float CAMERA_DISTANCE = 200; // how far the isometric camera sits from its target
	public static final int CAMERA_START_COL = 4;
	public static final int CAMERA_START_ROW = 4;

	private Settings() {}
}
