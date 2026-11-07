package renderEngine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import toolbox.Maths;

public class Renderer {

	private static final float NEAR_PLANE = 0.1f;  	// closest visible distance
	private static final float FAR_PLANE = 1000f;  	// farthest visible distance
	public static final float ORTHO_SIZE = 20f;  		// half the visible height in world units, at a 720px tall window
	private static final float REFERENCE_HEIGHT = 720f;

	/**
	 * Half the visible height in world units for the current window size.
	 * Grows with the window, so a bigger window shows more of the map at the same zoom.
	 */
	public static float getOrthoHalfHeight() {
		return ORTHO_SIZE * DisplayManager.getHeight() / REFERENCE_HEIGHT;
	}

	private Matrix4f projectionMatrix;

	public Renderer(StaticShader shader) {
		updateProjectionMatrix(shader);
	}

	/** Rebuilds the projection for the current window size. Call after the window is resized. */
	public void updateProjectionMatrix(StaticShader shader) {
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); // draw nearer pixels over farther ones
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}

	public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();

		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) DisplayManager.getWidth() / DisplayManager.getHeight();
		float halfHeight = getOrthoHalfHeight();
		// projectionMatrix = new Matrix4f().perspective(
		// 		(float) Math.toRadians(FOV), aspectRatio, NEAR_PLANE, FAR_PLANE);
		projectionMatrix = new Matrix4f().ortho(
			-halfHeight * aspectRatio,
			halfHeight * aspectRatio,
			-halfHeight,
			halfHeight,
			NEAR_PLANE,
			FAR_PLANE
		);
	}
}
