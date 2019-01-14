package com.latmod.mods.itemfilters.api;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPI
{
	/**
	 * IItemFilter Capability. It's recommended to use getFilter() and isFilter() methods.
	 */
	@CapabilityInject(IItemFilter.class)
	public static Capability<IItemFilter> CAPABILITY;

	private static final Map<String, Supplier<IRegisteredItemFilter>> REGISTRY0 = new LinkedHashMap<>();

	/**
	 * Immutable map of registered filters.
	 */
	public static final Map<String, Supplier<IRegisteredItemFilter>> REGISTRY = Collections.unmodifiableMap(REGISTRY0);

	/**
	 * This item can be used as 'filter' to disable pipe face or something similar, since it's not possible to obtain it in survival.
	 */
	public static final Item NULL_ITEM = Item.getItemFromBlock(Blocks.BARRIER);

	/**
	 * @return IItemFilter if stack is a filter, null otherwise.
	 */
	@Nullable
	public static IItemFilter getFilter(ItemStack stack)
	{
		return stack.getCapability(CAPABILITY, null);
	}

	/**
	 * @return true if stack is a filter.
	 */
	public static boolean isFilter(ItemStack stack)
	{
		return stack.hasCapability(CAPABILITY, null);
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

	/**
	 * If you don't want to create your own IItemFilter item, you can register it here and Item Filters mod will make an item.
	 * Registered filter must return the same id.
	 */
	public static void register(String id, Supplier<IRegisteredItemFilter> supplier)
	{
		REGISTRY0.put(id, supplier);
	}

	/**
	 * @return New instance of registered filter from id or null if it's not registered.
	 */
	@Nullable
	public static IRegisteredItemFilter createFromID(String id)
	{
		Supplier<IRegisteredItemFilter> supplier = REGISTRY0.get(id);
		return supplier == null ? null : supplier.get();
	}
}