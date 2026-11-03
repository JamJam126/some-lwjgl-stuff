package main;

import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class Assets {

	public final TexturedModel stall;
	public final TexturedModel dropper;
	public final TexturedModel conveyor;
	public final TexturedModel ore;

	public Assets(Loader loader) {
		stall = load(loader, "stall/stall-red", new ModelTexture(loader.loadTexture("stall/colormap")));

		ModelTexture factoryTexture = new ModelTexture(loader.loadTexture("dropper/colormap"));
		dropper = load(loader, "dropper/dropper", factoryTexture);
		conveyor = load(loader, "conveyor/conveyor", factoryTexture);
		ore = load(loader, "ore/ore", factoryTexture);
	}

	private static TexturedModel load(Loader loader, String objFile, ModelTexture texture) {
		return new TexturedModel(OBJLoader.loadObjModel(objFile, loader), texture);
	}
}
