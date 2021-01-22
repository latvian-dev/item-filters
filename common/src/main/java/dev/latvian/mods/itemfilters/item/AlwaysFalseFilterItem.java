package dev.latvian.mods.itemfilters.item;


import net.minecraft.world.item.ItemStack;

import java.util.List;

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

	@Override
	public void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
	}
}