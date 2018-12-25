package com.latmod.mods.itemfilters.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPI
{
	@CapabilityInject(IItemFilter.class)
	public static Capability<IItemFilter> CAPABILITY;

	@Nullable
	public static IItemFilter getFilter(ItemStack stack)
	{
		return stack.getCapability(CAPABILITY, null);
	}

	public static boolean isFilter(ItemStack stack)
	{
		return stack.hasCapability(CAPABILITY, null);
	}

	public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
	{
		if (stackA.getItem() != stackB.getItem())
		{
			return false;
		}

		if (stackA.getHasSubtypes())
		{
			if (stackA.getMetadata() != stackB.getMetadata())
			{
				return false;
			}
		}
		else if (stackA.getItemDamage() != stackB.getItemDamage())
		{
			return false;
		}

		return ItemStack.areItemStackShareTagsEqual(stackA, stackB);
	}

	public static boolean filter(ItemStack filter, ItemStack stack)
	{
		IItemFilter f = getFilter(filter);
		return f == null ? areItemStacksEqual(filter, stack) : f.filter(stack);
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
			f.getValidItems(list);
		}
	}
}