package dev.latvian.mods.itemfilters.api;

import dev.latvian.mods.itemfilters.ItemFilters;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPI
{
	public static final Tag.Named<Item> FILTERS_ITEM_TAG = getNamedTag(ItemFilters.MOD_ID + ":filters");
	public static final Tag.Named<Item> CHECK_NBT_ITEM_TAG = getNamedTag(ItemFilters.MOD_ID + ":check_nbt");

	@ExpectPlatform
	public static Tag.Named<Item> getNamedTag(String s)
	{
		throw new AssertionError();
	}

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

		return !CHECK_NBT_ITEM_TAG.contains(stackA.getItem()) || ItemStack.tagMatches(stackA, stackB);
	}

	/**
	 * @param filter filter item. If it's not an IItemFilter, then it will be compared using areItemStacksEqual() method.
	 * @param stack  item that is being checked.
	 */
	public static boolean filter(ItemStack filter, ItemStack stack)
	{
		if (filter.isEmpty())
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