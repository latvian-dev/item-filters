package com.latmod.mods.itemfilters.api;

import com.latmod.mods.itemfilters.ItemFilters;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPI
{
	@ObjectHolder(ItemFilters.MOD_ID + ":always_true")
	public static final Item ALWAYS_TRUE = Items.AIR;

	@ObjectHolder(ItemFilters.MOD_ID + ":always_false")
	public static final Item ALWAYS_FALSE = Items.AIR;

	/**
	 * @return IItemFilter if stack is a filter, null otherwise.
	 */
	@Nullable
	public static IItemFilter getFilter(ItemStack stack)
	{
		if (stack.getItem() instanceof IItemFilter)
		{
			return (IItemFilter) stack.getItem();
		}

		return null;
	}

	/**
	 * @return true if stack is a filter.
	 */
	public static boolean isFilter(ItemStack stack)
	{
		return stack.getItem() instanceof IItemFilter;
	}

	/**
	 * Helper method to check if two items are equal ignoring item count.
	 */
	public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
	{
		if (stackA == stackB)
		{
			return true;
		}

		if (stackA.getItem() != stackB.getItem())
		{
			return false;
		}

		return ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	/**
	 * @param filter filter item. If it's not an IItemFilter, then it will be compared using areItemStacksEqual() method.
	 * @param stack  item that is being checked.
	 */
	public static boolean filter(ItemStack filter, ItemStack stack)
	{
		if (filter == stack || filter.isEmpty())
		{
			return true;
		}

		IItemFilter f = getFilter(filter);
		return f == null ? areItemStacksEqual(filter, stack) : f.filter(filter, stack);
	}

	public static void getValidItems(ItemStack filter, List<ItemStack> list)
	{
		if (filter.isEmpty())
		{
			return;
		}

		IItemFilter f = getFilter(filter);

		if (f == null)
		{
			list.add(filter);
		}
		else
		{
			f.getValidFilterItems(filter, list);
		}
	}
}