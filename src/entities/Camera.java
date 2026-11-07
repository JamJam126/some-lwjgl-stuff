package entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import renderEngine.DisplayManager;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 35.264f; // rotation around the X axis (looking up / down)
	private float yaw   = 45f;   	 // rotation around the Y axis (looking left / right)
	private float roll;  				 	 // rotation around the Z axis (tilting the head)

	private static final float SPEED = 2.02f;

	public Camera() {	}

	  //          screen up (W)
    //              ↑
    //  −Z (row−) ↖   ↗ +X (col+)
    //              ·
    //  −X (col−) ↙   ↘ +Z (row+)
    //              ↓
	public void move() {
		long window = DisplayManager.getWindow();
		float s = (float) Math.sin(Math.toRadians(yaw));
		float c = (float) Math.cos(Math.toRadians(yaw));

		if (isDown(window, GLFW.GLFW_KEY_W)) {
			position.x += s * SPEED; position.z -= c * SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_S)) {
			position.x -= s * SPEED; position.z += c * SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_D)) {
			position.x += c * SPEED; position.z += s * SPEED;
		}
		if (isDown(window, GLFW.GLFW_KEY_A)) {
			position.x -= c * SPEED; position.z -= s * SPEED;
		}
		// if (isDown(window, GLFW.GLFW_KEY_SPACE)) {
		// 	position.y += SPEED;
		// }
		// if (isDown(window, GLFW.GLFW_KEY_LEFT_SHIFT)) {
		// 	position.y -= SPEED;
		// }
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
