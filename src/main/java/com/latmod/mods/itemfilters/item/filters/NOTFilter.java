package com.latmod.mods.itemfilters.item.filters;

import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public class NOTFilter extends LogicFilter implements INBTSerializable<NBTTagCompound>
{
	public ItemStack filter = ItemStack.EMPTY;

	@Override
	public boolean filter(ItemStack stack)
	{
		return !ItemFiltersAPI.filter(filter, stack);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return filter.isEmpty() ? new NBTTagCompound() : filter.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		filter = new ItemStack(nbt);

		if (filter.isEmpty())
		{
			filter = ItemStack.EMPTY;
		}
	}
}