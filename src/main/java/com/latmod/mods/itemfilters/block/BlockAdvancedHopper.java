package com.latmod.mods.itemfilters.block;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.gui.ItemFiltersGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
		return new TileAdvancedHopper();
	}

	@Override
	@Deprecated
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side != EnumFacing.DOWN;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			player.openGui(ItemFilters.INSTANCE, ItemFiltersGuiHandler.ADVANCED_HOPPER, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileAdvancedHopper)
		{
			TileAdvancedHopper hopper = (TileAdvancedHopper) tileEntity;

			for (int i = 0; i < hopper.buffer.getSlots(); i++)
			{
				ItemStack stack = hopper.buffer.getStackInSlot(i);

				if (!stack.isEmpty())
				{
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				}
			}
		}

		super.breakBlock(world, pos, state);
	}
}