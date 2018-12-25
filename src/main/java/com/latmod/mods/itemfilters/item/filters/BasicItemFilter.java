package com.latmod.mods.itemfilters.item.filters;

import com.latmod.mods.itemfilters.item.BaseItemFilter;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class BasicItemFilter extends BaseItemFilter
{
	@Override
	public boolean filter(ItemStack stack)
	{
		return true;
	}
}