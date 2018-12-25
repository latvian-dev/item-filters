package com.latmod.mods.itemfilters.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface IItemFilter
{
	/**
	 * @param stack ItemStack
	 */
	boolean filter(ItemStack stack);

	/**
	 * You should only override this if there is a faster way to check valid items
	 *
	 * @param list The list to add items to
	 */
	default void getValidItems(List<ItemStack> list)
	{
		NonNullList<ItemStack> allItems = NonNullList.create();

		for (Item item : Item.REGISTRY)
		{
			item.getSubItems(CreativeTabs.SEARCH, allItems);
		}

		for (ItemStack stack : allItems)
		{
			if (filter(stack))
			{
				list.add(stack);
			}
		}
	}
}