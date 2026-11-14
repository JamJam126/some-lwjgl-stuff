package shaders;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class UIShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/uiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/uiFragmentShader.txt";

	private int location_projectionMatrix;
	private int location_rect;
	private int location_uvRect;
	private int location_colour;

	public UIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_rect = getUniformLocation("rect");
		location_uvRect = getUniformLocation("uvRect");
		location_colour = getUniformLocation("colour");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

	public void loadRect(float x, float y, float width, float height) {
		loadVector4(location_rect, x, y, width, height);
	}

	public void loadUvRect(float u, float v, float uWidth, float vHeight) {
		loadVector4(location_uvRect, u, v, uWidth, vHeight);
	}

	public void loadColour(Vector4f colour) {
		loadVector4(location_colour, colour.x, colour.y, colour.z, colour.w);
	}
}
