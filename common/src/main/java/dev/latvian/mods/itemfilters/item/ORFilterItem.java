package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author LatvianModder
 */
public class ORFilterItem extends InventoryFilterItem {
	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		ItemInventory inventory = getInventory(filter);

		for (ItemStack stack1 : inventory.getItems()) {
			if (ItemFiltersAPI.filter(stack1, stack)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void getDisplayItemStacks(ItemStack filter, List<ItemStack> list) {
		ItemInventory inventory = getInventory(filter);

		for (ItemStack stack1 : inventory.getItems()) {
			if (ItemFiltersAPI.isFilter(stack1)) {
				super.getDisplayItemStacks(filter, list);
			} else {
				// allows for the possibility of custom items in the filter which aren't in the creative search tab
				list.add(stack1);
			}
		}
	}
}