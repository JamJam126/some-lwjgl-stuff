package main;

import items.Inventory;
import items.Items;

public class AssetSetter {

	// Starting line is centred on the tile the camera looks at
	public void setMachines(MachinePlacer placer) {
		int col = Settings.CAMERA_START_COL;
		int row = Settings.CAMERA_START_ROW;

		placer.place(Items.BASIC_DROPPER, col - 2, row, 0);
		for (int c = col - 1; c <= col + 1; c++) placer.place(Items.CONVEYOR, c, row, 0);
		placer.place(Items.FURNACE, col + 2, row, 0);
	}

	public void setInventory(Inventory inventory) {
		inventory.add(Items.BASIC_DROPPER, 2);
		inventory.add(Items.CONVEYOR,      13);
		inventory.add(Items.FURNACE,       1);
	}
}
