package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.world.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ANDFilterItem extends InventoryFilterItem {
	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		ItemInventory inventory = getInventory(filter);

		for (ItemStack stack1 : inventory.getItems()) {
			if (!ItemFiltersAPI.filter(stack1, stack)) {
				return false;
			}
		}

		return true;
	}
}