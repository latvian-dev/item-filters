package com.latmod.mods.itemfilters.filter;

import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class AlwaysFalseFilterItem extends BaseFilterItem
{
	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		return false;
	}
}