package com.latmod.mods.itemfilters.filter;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class NOTFilterItem extends LogicFilterItem
{
	public ItemStack parent = ItemStack.EMPTY;

	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		return !ItemFiltersAPI.filter(parent, item);
	}

	@Override
	public void clearFilterCache(ItemStack filter)
	{
		super.clearFilterCache(filter);

		IItemFilter f = ItemFiltersAPI.getFilter(parent);

		if (f != null)
		{
			f.clearFilterCache(parent);
		}
	}

	@Override
	public void resetFilterData(ItemStack filter)
	{
		parent = ItemStack.EMPTY;
	}
}