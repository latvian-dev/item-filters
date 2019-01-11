package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.item.ItemMissing;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public class NOTFilter extends LogicFilter implements INBTSerializable<NBTBase>
{
	public ItemStack filter = ItemStack.EMPTY;

	@Override
	public String getID()
	{
		return "not";
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		return !ItemFiltersAPI.filter(filter, stack);
	}

	@Override
	public NBTBase serializeNBT()
	{
		return ItemMissing.write(filter, false);
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		filter = ItemMissing.read(nbt);
	}

	@Override
	public void clearCache()
	{
		super.clearCache();

		IItemFilter f = ItemFiltersAPI.getFilter(filter);

		if (f != null)
		{
			f.clearCache();
		}
	}
}