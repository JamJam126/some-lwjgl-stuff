package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.Camera;

public class Maths {

	/**
	 * Builds a transformation ("model") matrix: moves, rotates and scales an
	 * object from its local space into world space.
	 *
	 * JOML note: the Matrix4f constructor already produces the identity matrix,
	 * and translate/rotate/scale each modify the matrix in place (post-multiply),
	 * so the calls below apply in the same order the tutorial's LWJGL 2 code does.
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
		matrix.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
		matrix.scale(scale);
		return matrix;
	}

	/**
	 * Builds the view matrix: it represents "move the whole world so that the
	 * camera ends up at the origin looking down -Z". That is why we rotate by the
	 * camera's pitch/yaw and then translate by the NEGATIVE of its position.
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		viewMatrix.translate(negativeCameraPos);
		return viewMatrix;
	}
}
