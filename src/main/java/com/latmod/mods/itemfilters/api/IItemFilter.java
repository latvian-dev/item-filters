package com.latmod.mods.itemfilters.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;

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

		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			item.fillItemGroup(ItemGroup.SEARCH, allItems);
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
}