package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class BaseItemFilter implements IItemFilter, ICapabilityProvider
{
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == ItemFiltersAPI.CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == ItemFiltersAPI.CAPABILITY ? (T) this : null;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip)
	{
	}

	public boolean openGUI(EntityPlayer player, EnumHand hand, ItemStack heldItem)
	{
		return false;
	}
}