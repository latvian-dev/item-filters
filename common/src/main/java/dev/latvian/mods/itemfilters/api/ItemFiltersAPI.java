package dev.latvian.mods.itemfilters.api;

import dev.architectury.hooks.tags.TagHooks;
import dev.latvian.mods.itemfilters.ItemFilters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPI {
	public static final ResourceLocation FILTERS_ITEM_TAG_ID = new ResourceLocation(ItemFilters.MOD_ID, "filters");
	public static final ResourceLocation CHECK_NBT_ITEM_TAG_ID = new ResourceLocation(ItemFilters.MOD_ID, "check_nbt");

	public static final Tag.Named<Item> FILTERS_ITEM_TAG = TagHooks.optionalItem(FILTERS_ITEM_TAG_ID);
	public static final Tag.Named<Item> CHECK_NBT_ITEM_TAG = TagHooks.optionalItem(CHECK_NBT_ITEM_TAG_ID);

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

		Tag<Item> tag = ItemTags.getAllTags().getTag(CHECK_NBT_ITEM_TAG_ID);

		if (tag == null) {
			return false;
		}

		return !tag.contains(stackA.getItem()) || ItemStack.tagMatches(stackA, stackB);
	}

	/**
	 * @param filter filter item. If it's not an IItemFilter, then it will be compared using areItemStacksEqual() method.
	 * @param stack  item that is being checked.
	 */
	public static boolean filter(ItemStack filter, ItemStack stack) {
		if (filter.isEmpty()) {
			return true;
		}

		IItemFilter f = getFilter(filter);
		return f == null ? areItemStacksEqual(filter, stack) : f.filter(filter, stack);
	}

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

	public static void getItems(ItemStack filter, Set<Item> list) {
		if (filter.isEmpty()) {
			return;
		}

		IItemFilter f = getFilter(filter);

		if (f == null) {
			list.add(filter.getItem());
		} else {
			f.getItems(filter, list);
		}
	}

	public static CustomFilter registerCustomFilter(String id, Predicate<ItemStack> predicate) {
		CustomFilter filter = new CustomFilter(id, predicate);
		CUSTOM_FILTERS.put(id, filter);
		return filter;
	}
}