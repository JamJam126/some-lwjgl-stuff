package entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import renderEngine.DisplayManager;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch; // rotation around the X axis (looking up / down)
	private float yaw;   // rotation around the Y axis (looking left / right)
	private float roll;  // rotation around the Z axis (tilting the head)

	private static final float SPEED = 0.02f;

	public Camera() {	}

	public void move() {
		long window = DisplayManager.getWindow();

		if (isDown(window, GLFW.GLFW_KEY_W)) {
			position.z -= SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_S)) {
			position.z += SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_D)) {
			position.x += SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_A)) {
			position.x -= SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_SPACE)) {
			position.y += SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_LEFT_SHIFT)) {
			position.y -= SPEED;
		}
	}

	private static boolean isDown(long window, int key) {
		return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
