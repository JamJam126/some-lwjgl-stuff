package renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import main.Settings;
import models.RawModel;
import shaders.LineShader;

// See-through square drawn on one tile
public class TileHighlight {

	private static final float HEIGHT = 0.05f;

	private final LineShader shader = new LineShader();
	private final RawModel square;

	public TileHighlight(Loader loader) {
		float h = Settings.TILE_SIZE / 2;
		float[] vertices = {
			-h, HEIGHT, -h,
			 h, HEIGHT, -h,
			-h, HEIGHT,  h,
			 h, HEIGHT,  h
		};
		square = loader.loadToVAO(vertices, 3);
	}

	public void render(Matrix4f projection, Camera camera, int col, int row, Vector4f colour) {
		shader.start();
		shader.loadProjectionMatrix(projection);
		shader.loadViewMatrix(camera);
		shader.loadColour(colour);
		shader.loadOffset(col * Settings.TILE_SIZE, 0, row * Settings.TILE_SIZE);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL30.glBindVertexArray(square.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
