package shaders;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import entities.Camera;
import toolbox.Maths;

public class LineShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/lineVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/lineFragmentShader.txt";

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_colour;

	public LineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_colour = getUniformLocation("colour");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera) {
		loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
	}

	public void loadColour(Vector4f colour) {
		loadVector4(location_colour, colour.x, colour.y, colour.z, colour.w);
	}
}
