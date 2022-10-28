package dev.latvian.mods.itemfilters.api;

import dev.latvian.mods.itemfilters.DisplayStacksCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * @author LatvianModder
 */
public interface IItemFilter {
	/**
	 * Does the given item stack match the given filter?
	 * @param filter the filter stack, whose item <strong>must</strong> implement {@link IItemFilter}
	 * @param stack the item stack being tested
	 */
	boolean filter(ItemStack filter, ItemStack stack);

	/**
	 * Does the given item match the given filter?
	 * @param filter the filter stack, whose item <strong>must</strong> implement {@link IItemFilter}
	 * @param item the item being tested
	 */
	default boolean filterItem(ItemStack filter, Item item) {
		return filter(filter, new ItemStack(item));
	}

	/**
	 * Add all the known item stacks which match this filter to the given list.
	 * <p>
	 * You should not override this, unless the result is truly trivial to compute; this default implementation
	 * caches results internally for performance.
	 *
	 * @param filter the filter stack, whose item <strong>must</strong> implement {@link IItemFilter}
	 * @param list list to add items to
	 */
	default void getDisplayItemStacks(ItemStack filter, List<ItemStack> list) {
		list.addAll(DisplayStacksCache.getCachedDisplayStacks(filter));
	}

	/**
	 * Get a list of all items this filter should apply to
	 * @param filter the filter item
	 * @param set set of items to add to
	 * @deprecated see notes in {@link ItemFiltersAPI#getItems(ItemStack, Set)}
	 */
	@Deprecated
	default void getItems(ItemStack filter, Set<Item> set) {
		for (Item item : Registry.ITEM) {
			if (filterItem(filter, item)) {
				set.add(item);
			}
		}
	}

	default void clearFilterCache(ItemStack filter) {
	}

	default void resetFilterData(ItemStack filter) {
	}

	@Environment(EnvType.CLIENT)
	default void addInfo(ItemStack filter, FilterInfo info, boolean expanded) {
	}
}