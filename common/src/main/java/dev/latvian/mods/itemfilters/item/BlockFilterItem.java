package dev.latvian.mods.itemfilters.item;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * @author LatvianModder
 */
public class BlockFilterItem extends BaseFilterItem
{
	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		return stack.getItem() instanceof BlockItem;
	}

	@Override
	public void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
		NonNullList<ItemStack> allItems = NonNullList.create();

		for (Block block : Registry.BLOCK)
		{
			Item item = block.asItem();

			if (item != Items.AIR && item instanceof BlockItem)
			{
				item.fillItemCategory(CreativeModeTab.TAB_SEARCH, allItems);
			}
		}

		list.addAll(allItems);
	}
}