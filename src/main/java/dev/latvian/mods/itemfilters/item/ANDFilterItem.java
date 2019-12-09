package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ANDFilterItem extends InventoryFilterItem
{
	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		ItemInventory inventory = getInventory(filter);

		for (ItemStack stack1 : inventory.getItems())
		{
			if (!ItemFiltersAPI.filter(stack1, item))
			{
				return false;
			}
		}

		return true;
	}
}