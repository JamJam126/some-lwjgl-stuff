package main;

import entities.machine.MachineType;

public class AssetSetter {

	// Starting line is centred on the tile the camera looks at
	public void setMachines(MachinePlacer placer) {
		int col = Settings.CAMERA_START_COL;
		int row = Settings.CAMERA_START_ROW;

		placer.place(MachineType.DROPPER, col - 2, row, 0);
		for (int c = col - 1; c <= col + 1; c++) placer.place(MachineType.CONVEYOR, c, row, 0);
		placer.place(MachineType.FURNACE, col + 2, row, 0);
	}
}
