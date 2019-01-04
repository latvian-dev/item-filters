package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ORFilter extends LogicFilter implements INBTSerializable<NBTTagList>
{
	public final List<ItemStack> items = new ArrayList<>();

	@Override
	public String getID()
	{
		return "or";
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		if (items.size() == 1)
		{
			return ItemFiltersAPI.filter(items.get(0), stack);
		}

		for (ItemStack stack1 : items)
		{
			if (ItemFiltersAPI.filter(stack1, stack))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public NBTTagList serializeNBT()
	{
		NBTTagList list = new NBTTagList();

		for (ItemStack stack : items)
		{
			if (!stack.isEmpty())
			{
				list.appendTag(stack.serializeNBT());
			}
		}

		return list;
	}

	@Override
	public void deserializeNBT(NBTTagList nbt)
	{
		items.clear();

		for (int i = 0; i < nbt.tagCount(); i++)
		{
			ItemStack stack = new ItemStack(nbt.getCompoundTagAt(i));

			if (!stack.isEmpty())
			{
				items.add(stack);
			}
		}
	}

	@Override
	public void getValidItems(List<ItemStack> list)
	{
		for (ItemStack item : items)
		{
			if (ItemFiltersAPI.isFilter(item))
			{
				super.getValidItems(list);
				return;
			}
		}

		list.addAll(items);
	}
}