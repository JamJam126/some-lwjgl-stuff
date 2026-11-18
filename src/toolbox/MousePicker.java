package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.Camera;
import main.Settings;
import renderEngine.DisplayManager;
import renderEngine.Renderer;
import tile.TilePosition;

// Turns a mouse pixel into the map tile under it
public class MousePicker {

	private final Camera camera;
	private final Renderer renderer;

	private final Matrix4f projectionView = new Matrix4f();
	private final Vector3f origin = new Vector3f();
	private final Vector3f direction = new Vector3f();
	private final int[] viewport = new int[4];

	public MousePicker(Camera camera, Renderer renderer) {
		this.camera = camera;
		this.renderer = renderer;
	}

	// The tile can be off the map, check it with MachineGrid.isInside
	public TilePosition getTile(float mouseX, float mouseY) {
		int width = DisplayManager.getWidth(), height = DisplayManager.getHeight();
		viewport[2] = width;
		viewport[3] = height;

		projectionView.set(renderer.getProjectionMatrix()).mul(Maths.createViewMatrix(camera));
		projectionView.unprojectRay(mouseX, height - mouseY, viewport, origin, direction); // GLFW y goes down, OpenGL y goes up

		float t = -origin.y / direction.y; // distance along the ray to the ground (y = 0)
		float x = origin.x + direction.x * t;
		float z = origin.z + direction.z * t;

		return new TilePosition(Math.round(x / Settings.TILE_SIZE), Math.round(z / Settings.TILE_SIZE));
	}
}
