package items;

import java.util.LinkedHashMap;
import java.util.Map;

public class Inventory {
	private final Map<ItemDefinition, Integer> counts = new LinkedHashMap<>(); 

	public int getCount(ItemDefinition item) {
		return counts.getOrDefault(item, 0);
	}

	public void add(ItemDefinition item, int amount) {
		counts.put(item, getCount(item) + amount);
	}

	public boolean take(ItemDefinition item) {
		if (getCount(item) == 0) {
			return false;
		}
			
		counts.put(item, getCount(item) - 1);
		return true;
	}
}
