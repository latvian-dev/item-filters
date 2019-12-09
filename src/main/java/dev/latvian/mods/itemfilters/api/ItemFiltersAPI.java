package dev.latvian.mods.itemfilters.api;

import dev.latvian.mods.itemfilters.ItemFilters;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
@SuppressWarnings("ALL")
public class ItemFiltersAPI
{
	public static final ResourceLocation FILTERS_ITEM_TAG = new ResourceLocation(ItemFilters.MOD_ID, "filters");
	public static final ResourceLocation CHECK_NBT_ITEM_TAG = new ResourceLocation(ItemFilters.MOD_ID, "check_nbt");

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
		else if (!stackA.hasTag() && !stackB.hasTag())
		{
			return true;
		}

		return !stackA.getItem().getTags().contains(CHECK_NBT_ITEM_TAG) || ItemStack.areItemStackTagsEqual(stackA, stackB);
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