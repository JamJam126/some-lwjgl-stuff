package main;

import java.util.List;

import org.joml.Vector3f;

import entities.machine.Conveyor;
import entities.machine.Dropper;
import entities.machine.Furnace;
import entities.machine.Machine;
import items.ConveyorDefinition;
import items.DropperDefinition;
import items.FurnaceDefinition;
import items.Inventory;
import items.ItemDefinition;
import tile.MachineGrid;

public class MachinePlacer {

	private final Assets assets;
	private final MachineGrid grid;
	private final Inventory inventory;
	private final List<Dropper> droppers;
	private final List<Conveyor> conveyors;
	private final List<Furnace> furnaces;

	public MachinePlacer(
		Assets assets,
		MachineGrid grid,
		Inventory inventory,
		List<Dropper> droppers,
		List<Conveyor> conveyors,
		List<Furnace> furnaces
	) {

		this.assets = assets;
		this.grid = grid;
		this.inventory = inventory;
		this.droppers = droppers;
		this.conveyors = conveyors;
		this.furnaces = furnaces;
	}

	public boolean place(ItemDefinition item, int col, int row, float rotY) {
		if (!grid.isFree(col, row)) {
			return false;
		}

		if (!inventory.take(item)) {
			return false;
		}

		Vector3f position = new Vector3f(col * Settings.TILE_SIZE, 0, row * Settings.TILE_SIZE);
		switch (item) {
			case DropperDefinition d:
				Dropper dropper = new Dropper(assets.dropper, assets.ore, position, rotY, d);
				grid.place(dropper);
				droppers.add(dropper);
				break;

			case ConveyorDefinition c:
				Conveyor conveyor = new Conveyor(assets.conveyor, position, rotY, c);
				grid.place(conveyor);
				conveyors.add(conveyor);
				break;

			case FurnaceDefinition f:
				Furnace furnace = new Furnace(assets.furnace, position, rotY, f);
				grid.place(furnace);
				furnaces.add(furnace);
				break;
		}
	
		return true;
	}

	public boolean remove(int col, int row) {
		Machine machine = grid.get(col, row);
		if (machine == null) return false;
		
		grid.remove(machine);
		inventory.add(machine.getDefinition(), 1);

		if (machine instanceof Dropper dropper) droppers.remove(dropper);
		if (machine instanceof Conveyor conveyor) conveyors.remove(conveyor);
		if (machine instanceof Furnace furnace) furnaces.remove(furnace);

		return true;
	}
}
