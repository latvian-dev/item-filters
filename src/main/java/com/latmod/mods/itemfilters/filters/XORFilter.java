package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.item.ItemMissing;
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
	public String getID()
	{
		return "xor";
	}

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
			nbt.setTag("l", ItemMissing.write(left, false));
		}

		if (!right.isEmpty())
		{
			nbt.setTag("r", ItemMissing.write(right, false));
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		left = ItemMissing.read(nbt.getTag("l"));
		right = ItemMissing.read(nbt.getTag("r"));
	}

	@Override
	public void clearCache()
	{
		super.clearCache();

		IItemFilter leftf = ItemFiltersAPI.getFilter(left);

		if (leftf != null)
		{
			leftf.clearCache();
		}

		IItemFilter rightf = ItemFiltersAPI.getFilter(right);

		if (rightf != null)
		{
			rightf.clearCache();
		}
	}
}