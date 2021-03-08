package dev.latvian.mods.itemfilters.item;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class BlockFilterItem extends BaseFilterItem {
	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof BlockItem;
	}

	@Override
	public boolean filterItem(ItemStack filter, Item item) {
		return item != Items.AIR && item instanceof BlockItem;
	}

	@Override
	public void getDisplayItemStacks(ItemStack filter, List<ItemStack> list) {
		NonNullList<ItemStack> allItems = NonNullList.create();

		for (Block block : Registry.BLOCK) {
			Item item = block.asItem();

			if (item != Items.AIR && item instanceof BlockItem) {
				item.fillItemCategory(CreativeModeTab.TAB_SEARCH, allItems);
			}
		}

		list.addAll(allItems);
	}

	@Override
	public void getItems(ItemStack filter, Set<Item> set) {
		for (Block block : Registry.BLOCK) {
			Item item = block.asItem();

			if (item != Items.AIR && item instanceof BlockItem) {
				set.add(item);
			}
		}
	}
}