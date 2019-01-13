package com.latmod.mods.itemfilters.integration.forestry;

import com.latmod.mods.itemfilters.filters.FilterBase;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

/**
 * @author LatvianModder
 */
public class BeeFilter extends FilterBase implements INBTSerializable<NBTTagCompound>
{
	private ItemStack beeStack = ItemStack.EMPTY;
	private boolean checkSecondary = false;

	private EnumBeeType beeType = null;
	private String primary = null;
	private String secondary = null;

	@Override
	public String getID()
	{
		return "bee";
	}

	public void setBee(ItemStack stack)
	{
		if (BeeManager.beeRoot != null && BeeManager.beeRoot.getType(stack) != null)
		{
			beeStack = stack.copy();
			clearCache();
		}
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		if (BeeManager.beeRoot == null)
		{
			return false;
		}
		else if (beeType == null)
		{
			beeType = BeeManager.beeRoot.getType(beeStack);

			if (beeType == null)
			{
				return false;
			}

			IBee bee = BeeManager.beeRoot.getMember(beeStack);
			primary = bee.getGenome().getPrimary().getUID();
			secondary = bee.getGenome().getSecondary().getUID();
		}

		else if (BeeManager.beeRoot.isMember(stack, beeType))
		{
			IBee b = BeeManager.beeRoot.getMember(stack);
			return b != null && (!checkSecondary || b.getGenome().getSecondary().getUID().equals(secondary)) && b.getGenome().getPrimary().getUID().equals(primary);
		}

		return false;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = beeStack.serializeNBT();

		if (checkSecondary)
		{
			nbt.setBoolean("check_secondary", true);
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		beeStack = new ItemStack(nbt);
		checkSecondary = nbt.getBoolean("check_secondary");
	}

	@Override
	public void clearCache()
	{
		super.clearCache();
		beeType = null;
		primary = null;
		secondary = null;
	}

	@Override
	public void getValidItems(List<ItemStack> stacks)
	{
		stacks.add(beeStack);
	}
}