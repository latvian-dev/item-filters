package dev.latvian.mods.itemfilters.item;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class AlwaysFalseFilterItem extends BaseFilterItem {
	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		return false;
	}

	@Override
	public boolean filterItem(ItemStack filter, Item item) {
		return false;
	}

	@Override
	public void getDisplayItemStacks(ItemStack filter, List<ItemStack> list) {
	}
}