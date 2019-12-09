package dev.latvian.mods.itemfilters.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;

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

		for (Block block : ForgeRegistries.BLOCKS.getValues())
		{
			Item item = block.asItem();

			if (item != Items.AIR && item instanceof BlockItem)
			{
				item.fillItemGroup(ItemGroup.SEARCH, allItems);
			}
		}

		list.addAll(allItems);
	}
}