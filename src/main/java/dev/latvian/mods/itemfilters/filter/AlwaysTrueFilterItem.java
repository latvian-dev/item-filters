package dev.latvian.mods.itemfilters.filter;

import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class AlwaysTrueFilterItem extends BaseFilterItem
{
	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		return true;
	}
}