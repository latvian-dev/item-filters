package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.block.TileAdvancedHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public enum ItemFiltersGuiHandler implements IGuiHandler
{
	INSTANCE;

	public static final int ADVANCED_HOPPER = 1;

	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == ADVANCED_HOPPER)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

			if (tileEntity instanceof TileAdvancedHopper)
			{
				return new ContainerAdvancedHopper(player, (TileAdvancedHopper) tileEntity);
			}
		}

		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return getClientGuiElement0(id, player, world, x, y, z);
	}

	@Nullable
	private Object getClientGuiElement0(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == ADVANCED_HOPPER)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

			if (tileEntity instanceof TileAdvancedHopper)
			{
				return new GuiAdvancedHopper(new ContainerAdvancedHopper(player, (TileAdvancedHopper) tileEntity));
			}
		}

		return null;
	}
}