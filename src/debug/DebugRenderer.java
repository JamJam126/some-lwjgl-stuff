package debug;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import main.Settings;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import shaders.LineShader;
import tile.TileManager;
import tile.TileManager.VisibleArea;
import toolbox.Maths;
import ui.UIRenderer;

public class DebugRenderer {

	private static final Vector4f GRID_COLOUR = new Vector4f(0, 0, 0, 0.35f);
	private static final Vector4f LABEL_COLOUR = new Vector4f(1, 1, 1, 1);
	private static final Vector4f SHADOW_COLOUR = new Vector4f(0, 0, 0, 0.8f);
	private static final float LABEL_SIZE = 14;
	private static final float GRID_HEIGHT = 0.05f; // just above the ground so the lines don't flicker

	private final LineShader shader = new LineShader();
	private final RawModel grid;
	private final Matrix4f projectionView = new Matrix4f();
	private final Vector4f point = new Vector4f();

	public DebugRenderer(Loader loader, int cols, int rows) {
		grid = loader.loadToVAO(createGridLines(cols, rows), 3);
	}

	public void renderGrid(Matrix4f projection, Camera camera) {
		shader.start();
		shader.loadProjectionMatrix(projection);
		shader.loadViewMatrix(camera);
		shader.loadColour(GRID_COLOUR);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL30.glBindVertexArray(grid.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		GL11.glDrawArrays(GL11.GL_LINES, 0, grid.getVertexCount());

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	// Call between uiRenderer.begin() and end()
	public void renderLabels(UIRenderer ui, Matrix4f projection, Camera camera, TileManager tileManager) {
		projectionView.set(projection).mul(Maths.createViewMatrix(camera));
		VisibleArea area = tileManager.getVisibleArea(camera);

		for (int row = area.minRow(); row <= area.maxRow(); row++) {
			for (int col = area.minCol(); col <= area.maxCol(); col++) {
				point.set(col * Settings.TILE_SIZE, 0, row * Settings.TILE_SIZE, 1);
				projectionView.transform(point);

				// -1..1 on screen -> pixels from the top-left
				float screenX = (point.x / point.w + 1) * 0.5f * DisplayManager.getWidth();
				float screenY = (1 - point.y / point.w) * 0.5f * DisplayManager.getHeight();

				String label = col + "," + row;
				float x = screenX - ui.getTextWidth(label, LABEL_SIZE) / 2;
				float y = screenY - LABEL_SIZE / 2;
				ui.drawText(label, x + 1, y + 1, LABEL_SIZE, SHADOW_COLOUR);
				ui.drawText(label, x, y, LABEL_SIZE, LABEL_COLOUR);
			}
		}
	}

	// Tiles are centred on col * TILE_SIZE, so their edges are half a tile either side
	private static float[] createGridLines(int cols, int rows) {
		float tile = Settings.TILE_SIZE;
		float minX = -tile / 2, maxX = (cols - 0.5f) * tile;
		float minZ = -tile / 2, maxZ = (rows - 0.5f) * tile;

		float[] vertices = new float[(cols + 1 + rows + 1) * 6];
		int i = 0;
		for (int col = 0; col <= cols; col++) {
			float x = minX + col * tile;
			i = addLine(vertices, i, x, minZ, x, maxZ);
		}
		for (int row = 0; row <= rows; row++) {
			float z = minZ + row * tile;
			i = addLine(vertices, i, minX, z, maxX, z);
		}
		return vertices;
	}

	private static int addLine(float[] vertices, int i, float x1, float z1, float x2, float z2) {
		vertices[i++] = x1; vertices[i++] = GRID_HEIGHT; vertices[i++] = z1;
		vertices[i++] = x2; vertices[i++] = GRID_HEIGHT; vertices[i++] = z2;
		return i;
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
