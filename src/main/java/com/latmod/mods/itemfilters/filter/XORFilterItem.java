package com.latmod.mods.itemfilters.filter;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class XORFilterItem extends LogicFilterItem
{
	public ItemStack left = ItemStack.EMPTY;
	public ItemStack right = ItemStack.EMPTY;

	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		return ItemFiltersAPI.filter(left, item) != ItemFiltersAPI.filter(right, item);
	}

	@Override
	public void clearFilterCache(ItemStack filter)
	{
		IItemFilter leftf = ItemFiltersAPI.getFilter(left);

		if (leftf != null)
		{
			leftf.clearFilterCache(left);
		}

		IItemFilter rightf = ItemFiltersAPI.getFilter(right);

		if (rightf != null)
		{
			rightf.clearFilterCache(right);
		}
	}

	@Override
	public void resetFilterData(ItemStack filter)
	{
		left = ItemStack.EMPTY;
		right = ItemStack.EMPTY;
	}
}