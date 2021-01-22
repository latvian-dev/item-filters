package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author LatvianModder
 */
public class XORFilterItem extends InventoryFilterItem
{
	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		ItemInventory inventory = getInventory(filter);
		return inventory.getItems().size() >= 2 && ItemFiltersAPI.filter(inventory.getItems().get(0), item) != ItemFiltersAPI.filter(inventory.getItems().get(1), item);
	}

	@Override
	public int getInventorySize(ItemStack filter)
	{
		return 2;
	}

	@Override
	public void addSlots(ItemStack filter, List<FilterSlot> list)
	{
		list.add(new FilterSlot(50, 34));
		list.add(new FilterSlot(110, 34));
	}
}