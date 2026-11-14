package tile;

import entities.Entity;

public class MachineGrid {

	private final Entity[][] machines; 

	public MachineGrid(int cols, int rows) {
		machines = new Entity[rows][cols];
	}

	public boolean isInside(int col, int row) {
		return row >= 0 && row < machines.length && col >= 0 && col < machines[0].length;
	}

	public Entity get(int col, int row) {
		if (!isInside(col, row)) return null;
		return machines[row][col];
	}

	public boolean isFree(int col, int row) {
		return isInside(col, row) && machines[row][col] == null;
	}

	public boolean place(Entity machine) {
		int col = machine.getCol(), row = machine.getRow();
		if (!isFree(col, row)) return false;
		machines[row][col] = machine;
		return true;
	}

	public void remove(Entity machine) {
		int col = machine.getCol(), row = machine.getRow();
		if (get(col, row) == machine) machines[row][col] = null;
	}
}
