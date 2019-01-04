package com.latmod.mods.itemfilters.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPI
{
	@CapabilityInject(IItemFilter.class)
	public static Capability<IItemFilter> CAPABILITY;

	private static final Map<String, Supplier<IRegisteredItemFilter>> REGISTRY0 = new HashMap<>();
	public static final Map<String, Supplier<IRegisteredItemFilter>> REGISTRY = Collections.unmodifiableMap(REGISTRY0);

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
		if (filter.isEmpty())
		{
			return true;
		}

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

	public static void register(String id, Supplier<IRegisteredItemFilter> supplier)
	{
		REGISTRY0.put(id, supplier);
	}

	@Nullable
	public static IRegisteredItemFilter createFromID(String id)
	{
		Supplier<IRegisteredItemFilter> supplier = REGISTRY0.get(id);
		return supplier == null ? null : supplier.get();
	}
}