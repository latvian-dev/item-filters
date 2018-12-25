package com.latmod.mods.itemfilters.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockAdvancedHopper extends Block
{
	public BlockAdvancedHopper()
	{
		super(Material.IRON, MapColor.STONE);
		setHardness(2F);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityHopper();
	}
}