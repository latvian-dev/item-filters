package com.latmod.mods.itemfilters.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * @author LatvianModder
 */
public interface IPaintable
{
	void paint(IBlockState paint, EnumFacing facing, boolean all);

	IBlockState getPaint();
}