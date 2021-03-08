package dev.latvian.mods.itemfilters.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author LatvianModder
 */
public interface IItemFilter {
	/**
	 * @param stack ItemStack
	 */
	boolean filter(ItemStack filter, ItemStack stack);

	default boolean filterItem(ItemStack filter, Item item) {
		return filter(filter, new ItemStack(item));
	}

	/**
	 * You should only override this if there is a faster way to check valid items
	 *
	 * @param list The list to add items to
	 */
	default void getDisplayItemStacks(ItemStack filter, List<ItemStack> list) {
		NonNullList<ItemStack> allItems = NonNullList.create();
		Set<Item> items = new LinkedHashSet<>();
		getItems(filter, items);

		for (Item item : items) {
			try {
				item.fillItemCategory(CreativeModeTab.TAB_SEARCH, allItems);
			} catch (Throwable ex) {
			}
		}

		for (ItemStack is : allItems) {
			if (filter(filter, is)) {
				list.add(is);
			}
		}
	}

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