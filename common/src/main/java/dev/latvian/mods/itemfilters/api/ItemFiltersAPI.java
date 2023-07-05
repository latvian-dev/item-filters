package dev.latvian.mods.itemfilters.api;

import dev.latvian.mods.itemfilters.ItemFilters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPI {
	public static final TagKey<Item> FILTERS_ITEM_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(ItemFilters.MOD_ID, "filters"));
	public static final TagKey<Item> CHECK_NBT_ITEM_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(ItemFilters.MOD_ID, "check_nbt"));

	public static final Map<String, CustomFilter> CUSTOM_FILTERS = new LinkedHashMap<>();

	/**
	 * @return IItemFilter if stack is a filter, null otherwise.
	 */
	@Nullable
	public static IItemFilter getFilter(ItemStack stack) {
		if (stack.getItem() instanceof IItemFilter) {
			return (IItemFilter) stack.getItem();
		}

		return null;
	}

	/**
	 * @return true if stack is a filter.
	 */
	public static boolean isFilter(ItemStack stack) {
		return stack.getItem() instanceof IItemFilter;
	}

	/**
	 * Helper method to check if two items are equal ignoring item count.
	 */
	public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
		if (stackA == stackB) {
			return true;
		}

		if (stackA.getItem() != stackB.getItem()) {
			return false;
		} else if (!stackA.hasTag() && !stackB.hasTag()) {
			return true;
		}

		return !stackA.is(CHECK_NBT_ITEM_TAG) || stackA.hasTag() && stackA.getTag().equals(stackB.getTag());
	}

	/**
	 * @param filter filter item. If it's not an IItemFilter, then it will be compared using areItemStacksEqual() method.
	 * @param stack  item that is being checked.
	 * @return true if the item matches the filter
	 */
	public static boolean filter(ItemStack filter, ItemStack stack) {
		if (filter.isEmpty()) {
			return true;
		}

		IItemFilter f = getFilter(filter);
		return f == null ? areItemStacksEqual(filter, stack) : f.filter(filter, stack);
	}

	/**
	 * Add all the known item stacks that match the given filter to the given list. Do not modify these item stacks
	 * without first copying them; they come from a shared cached pool.
	 * @param filter the filter item
	 * @param list list of item stacks to add to
	 * @implNote this list is computed from the list of all item stacks known from the creative search tab and is
	 * cached internally per filter item for performance reasons
	 */
	public static void getDisplayItemStacks(ItemStack filter, List<ItemStack> list) {
		if (filter.isEmpty()) {
			return;
		}

		IItemFilter f = getFilter(filter);

		if (f == null) {
			list.add(filter);
		} else {
			f.getDisplayItemStacks(filter, list);
		}
	}

	public static CustomFilter registerCustomFilter(String id, Predicate<ItemStack> predicate) {
		CustomFilter filter = new CustomFilter(id, predicate);
		CUSTOM_FILTERS.put(id, filter);
		return filter;
	}
}
