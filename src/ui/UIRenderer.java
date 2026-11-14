package ui;

import java.awt.image.BufferedImage;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import shaders.UIShader;

// Draws in screen pixels, (0, 0) = top-left of the window
public class UIRenderer {

	public static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);

	private static final int FONT_SIZE = 32;
	private static final float[] QUAD = { 0, 0,  0, 1,  1, 0,  1, 1 };

	private final UIShader shader = new UIShader();
	private final RawModel quad;
	private final int whiteTexture;
	private final BitmapFont font;
	private final Matrix4f projection = new Matrix4f();

	public UIRenderer(Loader loader) {
		quad = loader.loadToVAO(QUAD, 2);

		BufferedImage white = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		white.setRGB(0, 0, 0xFFFFFFFF);
		whiteTexture = loader.loadTexture(white);

		font = new BitmapFont(loader, FONT_SIZE);
	}

	public void begin() {
		shader.start();

		projection.setOrtho(0, DisplayManager.getWidth(), DisplayManager.getHeight(), 0, -1, 1);
		shader.loadProjectionMatrix(projection);

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	public void end() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.stop();
	}

	public void drawImage(int texture, float x, float y, float width, float height) {
		drawRegion(texture, x, y, width, height, 0, 0, 1, 1, WHITE);
	}

	public void drawRect(float x, float y, float width, float height, Vector4f colour) {
		drawRegion(whiteTexture, x, y, width, height, 0, 0, 1, 1, colour);
	}

	public void drawText(String text, float x, float y, float size, Vector4f colour) {
		float charWidth = size * font.getAspect();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (font.hasChar(c) && c != ' ') {
				drawRegion(font.getTextureID(), x, y, charWidth, size,
						font.getU(c), font.getV(c), font.getCellU(), font.getCellV(), colour);
			}
			x += charWidth;
		}
	}

	public float getTextWidth(String text, float size) {
		return text.length() * size * font.getAspect();
	}

	public void drawRegion(int texture, float x, float y, float width, float height,
			float u, float v, float uWidth, float vHeight, Vector4f colour) {
		shader.loadRect(x, y, width, height);
		shader.loadUvRect(u, v, uWidth, vHeight);
		shader.loadColour(colour);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
