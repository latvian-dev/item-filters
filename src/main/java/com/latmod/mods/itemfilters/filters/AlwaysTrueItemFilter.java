package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.IRegisteredItemFilter;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public enum AlwaysTrueItemFilter implements IRegisteredItemFilter
{
	INSTANCE;

	@Override
	public String getID()
	{
		return "always_true";
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		return true;
	}
}