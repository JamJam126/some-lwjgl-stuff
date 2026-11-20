package input;

import org.lwjgl.glfw.GLFW;

import renderEngine.DisplayManager;

public class MouseHandler {

	private static boolean leftClicked = false;

	public static void init(long window) {
		GLFW.glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) leftClicked = true;
		});
	}

	public static boolean wasLeftClicked() {
		boolean clicked = leftClicked;
		leftClicked = false;
		return clicked;
	}

	public static float getX() { return getCursor()[0]; }
	public static float getY() { return getCursor()[1]; }

	public static float[] getCursor() {
		double[] x = new double[1];
		double[] y = new double[1];

		GLFW.glfwGetCursorPos(DisplayManager.getWindow(), x, y);

		return new float[] { (float) x[0], (float) y[0] };
	}
}