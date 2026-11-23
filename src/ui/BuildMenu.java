package ui;

import java.util.List;

import org.joml.Vector4f;

import items.Inventory;
import items.ItemDefinition;
import items.Items;
import renderEngine.DisplayManager;

public class BuildMenu {

	private static final float BUTTON_WIDTH = 140;
	private static final float BUTTON_HEIGHT = 64;
	private static final float GAP = 10;
	private static final float BOTTOM_MARGIN = 16;
	private static final float LABEL_SIZE = 17;
	private static final float PRICE_SIZE = 18;

	private static final Vector4f BUTTON_COLOUR = new Vector4f(0, 0, 0, 0.5f);
	private static final Vector4f HOVER_COLOUR = new Vector4f(0.2f, 0.2f, 0.2f, 0.7f);
	private static final Vector4f SELECTED_COLOUR = new Vector4f(1, 0.85f, 0.3f, 0.45f);
	private static final Vector4f PRICE_COLOUR = new Vector4f(1, 0.85f, 0.3f, 1);

	private static final List<ItemDefinition> ITEMS = Items.ALL;

	public void render(UIRenderer ui, Inventory inventory, ItemDefinition selectedType, float mouseX, float mouseY) {
		ItemDefinition hovered = getButtonAt(mouseX, mouseY);

		for (int i = 0; i < ITEMS.size(); i++) {
			ItemDefinition type = ITEMS.get(i);
			float x = getButtonX(i);
			float y = getButtonY();

			Vector4f background = BUTTON_COLOUR;
			if (type == selectedType) background = SELECTED_COLOUR;
			else if (type == hovered) background = HOVER_COLOUR;
			ui.drawRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, background);

			String label = (i + 1) + " " + type.name(); // number = keyboard shortcut
			String amount = "x" + inventory.getCount(type);
			drawCentred(ui, label, x, y + 8, LABEL_SIZE, UIRenderer.WHITE);
			drawCentred(ui, amount, x, y + 36, PRICE_SIZE, PRICE_COLOUR);
		}
	}

	public ItemDefinition getButtonAt(float mouseX, float mouseY) {
		float y = getButtonY();
		if (mouseY < y || mouseY > y + BUTTON_HEIGHT) return null;

		for (int i = 0; i < ITEMS.size(); i++) {
			float x = getButtonX(i);
			if (mouseX >= x && mouseX <= x + BUTTON_WIDTH) return ITEMS.get(i);
		}
		return null;
	}

	private void drawCentred(UIRenderer ui, String text, float buttonX, float y, float size, Vector4f colour) {
		float x = buttonX + (BUTTON_WIDTH - ui.getTextWidth(text, size)) / 2;
		ui.drawText(text, x, y, size, colour);
	}

	private float getButtonX(int index) {
		float totalWidth = ITEMS.size() * BUTTON_WIDTH + (ITEMS.size() - 1) * GAP;
		float startX = (DisplayManager.getWidth() - totalWidth) / 2;
		return startX + index * (BUTTON_WIDTH + GAP);
	}

	private float getButtonY() {
		return DisplayManager.getHeight() - BOTTOM_MARGIN - BUTTON_HEIGHT;
	}
}
