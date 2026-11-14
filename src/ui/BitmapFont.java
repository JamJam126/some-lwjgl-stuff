package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import renderEngine.Loader;

// 16 x 6 grid of characters (ASCII 32-127), drawn at startup from Java's monospaced font
public class BitmapFont {

	private static final int FIRST_CHAR = 32;
	private static final int COLUMNS = 16;
	private static final int ROWS = 6;

	private final int textureID;
	private final int cellWidth;
	private final int cellHeight;

	public BitmapFont(Loader loader, int fontSize) {
		Font font = new Font(Font.MONOSPACED, Font.BOLD, fontSize);

		BufferedImage scratch = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = scratch.createGraphics();
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		cellWidth = metrics.charWidth('M');
		cellHeight = metrics.getHeight();
		int ascent = metrics.getAscent();
		g.dispose();

		BufferedImage atlas = new BufferedImage(cellWidth * COLUMNS, cellHeight * ROWS, BufferedImage.TYPE_INT_ARGB);
		g = atlas.createGraphics();
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (int i = 0; i < COLUMNS * ROWS; i++) {
			char c = (char) (FIRST_CHAR + i);
			int x = (i % COLUMNS) * cellWidth;
			int y = (i / COLUMNS) * cellHeight;
			g.drawString(String.valueOf(c), x, y + ascent);
		}
		g.dispose();

		textureID = loader.loadTexture(atlas);
	}

	public boolean hasChar(char c) {
		return c >= FIRST_CHAR && c < FIRST_CHAR + COLUMNS * ROWS;
	}

	public float getU(char c) { return ((c - FIRST_CHAR) % COLUMNS) / (float) COLUMNS; }
	public float getV(char c) { return ((c - FIRST_CHAR) / COLUMNS) / (float) ROWS; }

	public float getCellU() { return 1f / COLUMNS; }
	public float getCellV() { return 1f / ROWS; }

	public float getAspect() { return cellWidth / (float) cellHeight; }

	public int getTextureID() { return textureID; }
}
