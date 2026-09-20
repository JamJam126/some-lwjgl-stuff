package renderEngine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


public class DisplayManager {
	
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static long window;
	
	public static void createDisplay() {
		
		// Initialize 
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to Initailize");
		}

		// OpenGL context setup (3.2 core profile)
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3); 
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE); 
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE); // for macOS
		
		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "Test Window", 0, 0);
		
		if (window == 0) {
			throw new RuntimeException("Failed to create window");
		}
	
		GLFW.glfwMakeContextCurrent(window);
		
		// Enable V-Sync
		GLFW.glfwSwapInterval(1);
		
		GL.createCapabilities();
		
		// Set rendering area
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void updateDisplay() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	public static boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	/** The GLFW window handle. Needed for polling input with glfwGetKey. */
	public static long getWindow() {
		return window;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}
	
	public static void closeDisplay() {
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
}
