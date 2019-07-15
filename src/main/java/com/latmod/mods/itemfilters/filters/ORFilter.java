package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.item.ItemMissing;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ORFilter extends LogicFilter implements INBTSerializable<ListNBT>
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
	public ListNBT serializeNBT()
	{
		ListNBT list = new ListNBT();

		for (ItemStack stack : items)
		{
			if (!stack.isEmpty())
			{
				list.add(ItemMissing.write(stack, true));
			}
		}

		return list;
	}

	@Override
	public void deserializeNBT(ListNBT nbt)
	{
		items.clear();

		for (INBT aNbt : nbt)
		{
			ItemStack stack = ItemMissing.read(aNbt);

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

	@Override
	public void clearCache()
	{
		super.clearCache();

		for (ItemStack item : items)
		{
			IItemFilter f = ItemFiltersAPI.getFilter(item);

			if (f != null)
			{
				f.clearCache();
			}
		}
	}

	@Override
	public void resetData()
	{
		items.clear();
	}
}