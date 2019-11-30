package dev.latvian.mods.itemfilters.filter;

import dev.latvian.mods.itemfilters.api.IItemFilter;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ANDFilterItem extends LogicFilterItem
{
	public final List<ItemStack> items = new ArrayList<>();

	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		for (ItemStack stack1 : items)
		{
			if (!ItemFiltersAPI.filter(stack1, item))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public void clearFilterCache(ItemStack filter)
	{
		super.clearFilterCache(filter);

		for (ItemStack item : items)
		{
			IItemFilter f = ItemFiltersAPI.getFilter(item);

			if (f != null)
			{
				f.clearFilterCache(item);
			}
		}
	}

	@Override
	public void resetFilterData(ItemStack filter)
	{
		items.clear();
	}
}