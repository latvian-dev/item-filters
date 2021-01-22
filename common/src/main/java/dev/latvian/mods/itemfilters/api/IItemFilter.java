package dev.latvian.mods.itemfilters.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author LatvianModder
 */
public interface IItemFilter
{
	/**
	 * @param stack ItemStack
	 */
	boolean filter(ItemStack filter, ItemStack stack);

	/**
	 * You should only override this if there is a faster way to check valid items
	 *
	 * @param list The list to add items to
	 */
	default void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
		NonNullList<ItemStack> allItems = NonNullList.create();

		for (Item item : Registry.ITEM)
		{
			item.fillItemCategory(CreativeModeTab.TAB_SEARCH, allItems);
		}

		for (ItemStack is : allItems)
		{
			if (filter(filter, is))
			{
				list.add(is);
			}
		}
	}

	default void clearFilterCache(ItemStack filter)
	{
	}

	default void resetFilterData(ItemStack filter)
	{
	}

	@Environment(EnvType.CLIENT)
	default void addInfo(ItemStack filter, FilterInfo info, boolean expanded)
	{
	}
}