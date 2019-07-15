package com.latmod.mods.itemfilters.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
	boolean filter(ItemStack stack);

	/**
	 * Open GUI on client side
	 */
	@OnlyIn(Dist.CLIENT)
	default void openEditingGUI(Runnable save)
	{
	}

	/**
	 * You should only override this if there is a faster way to check valid items
	 *
	 * @param list The list to add items to
	 */
	default void getValidItems(List<ItemStack> list)
	{
		NonNullList<ItemStack> allItems = NonNullList.create();

		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			item.fillItemGroup(ItemGroup.SEARCH, allItems);
		}

		for (ItemStack stack : allItems)
		{
			if (filter(stack))
			{
				list.add(stack);
			}
		}
	}

	default void clearCache()
	{
	}

	default void resetData()
	{
	}
}