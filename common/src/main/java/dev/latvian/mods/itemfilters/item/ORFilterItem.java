package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author LatvianModder
 */
public class ORFilterItem extends InventoryFilterItem
{
	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		ItemInventory inventory = getInventory(filter);

		for (ItemStack stack1 : inventory.getItems())
		{
			if (ItemFiltersAPI.filter(stack1, item))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
		ItemInventory inventory = getInventory(filter);

		for (ItemStack item : inventory.getItems())
		{
			if (ItemFiltersAPI.isFilter(item))
			{
				super.getValidFilterItems(filter, list);
				return;
			}
		}

		list.addAll(inventory.getItems());
	}
}