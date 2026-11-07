package renderEngine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


public class DisplayManager {
	
	
	private static final int WINDOWED_WIDTH = 1280;
	private static final int WINDOWED_HEIGHT = 720;

	// Current size of the drawing area in pixels; updated whenever the window is resized
	private static int width = WINDOWED_WIDTH;
	private static int height = WINDOWED_HEIGHT;
	private static boolean resized = false;
	private static boolean fullscreen = false;
	
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
		
		window = GLFW.glfwCreateWindow(WINDOWED_WIDTH, WINDOWED_HEIGHT, "Test Window", 0, 0);
		
		if (window == 0) {
			throw new RuntimeException("Failed to create window");
		}
	
		GLFW.glfwMakeContextCurrent(window);
		
		// Enable V-Sync
		GLFW.glfwSwapInterval(1);
		
		GL.createCapabilities();
		
		int[] fbWidth = new int[1], fbHeight = new int[1];
		GLFW.glfwGetFramebufferSize(window, fbWidth, fbHeight);
		width = fbWidth[0];
		height = fbHeight[0];
		GL11.glViewport(0, 0, width, height);

		GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
			if (w == 0 || h == 0) return; // minimized
			width = w;
			height = h;
			GL11.glViewport(0, 0, w, h);
			resized = true;
		});

		GLFW.glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS) toggleFullscreen();
		});
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
		return width;
	}

	public static int getHeight() {
		return height;
	}

	/** True once after the window changed size, so the renderer can rebuild its projection. */
	public static boolean wasResized() {
		boolean r = resized;
		resized = false;
		return r;
	}
	
	public static void closeDisplay() {
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public static void toggleFullscreen() {
		long monitor = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);

		if (!fullscreen) {
			GLFW.glfwSetWindowMonitor(window, monitor, 0, 0, mode.width(), mode.height(), mode.refreshRate());
    } else {
			GLFW.glfwSetWindowMonitor(window, 0, 100, 100, WINDOWED_WIDTH, WINDOWED_HEIGHT, GLFW.GLFW_DONT_CARE);
    }

    fullscreen = !fullscreen;
	}
}
