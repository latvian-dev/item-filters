package com.latmod.mods.itemfilters.item.filters;

import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public class XORFilter extends LogicFilter implements INBTSerializable<NBTTagCompound>
{
	public ItemStack left = ItemStack.EMPTY;
	public ItemStack right = ItemStack.EMPTY;

	@Override
	public boolean filter(ItemStack stack)
	{
		return ItemFiltersAPI.filter(left, stack) != ItemFiltersAPI.filter(right, stack);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		if (!left.isEmpty())
		{
			nbt.setTag("l", left.serializeNBT());
		}

		if (!right.isEmpty())
		{
			nbt.setTag("r", right.serializeNBT());
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		left = new ItemStack(nbt.getCompoundTag("l"));
		right = new ItemStack(nbt.getCompoundTag("r"));

		if (left.isEmpty())
		{
			left = ItemStack.EMPTY;
		}

		if (right.isEmpty())
		{
			right = ItemStack.EMPTY;
		}
	}
}