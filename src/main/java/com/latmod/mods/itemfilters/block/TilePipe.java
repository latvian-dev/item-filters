package com.latmod.mods.itemfilters.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TilePipe extends TileBase
{
	public static final EnumFacing[][] FROM_AXIS;

	static
	{
		EnumFacing.Axis[] axes = EnumFacing.Axis.values();
		EnumFacing.AxisDirection[] directions = EnumFacing.AxisDirection.values();
		FROM_AXIS = new EnumFacing[axes.length][directions.length];

		for (EnumFacing.Axis axis : axes)
		{
			for (EnumFacing.AxisDirection direction : directions)
			{
				FROM_AXIS[axis.ordinal()][direction.ordinal()] = EnumFacing.getFacingFromAxis(direction, axis);
			}
		}
	}

	public final PipeInventory fromNegative = new PipeInventory(this);
	public final PipeInventory fromPositive = new PipeInventory(this);
	private EnumFacing.Axis axis = null;
	private boolean isDirty = false;
	public boolean sync = false;

	public TilePipe()
	{
		fromNegative.opposite = fromPositive;
		fromPositive.opposite = fromNegative;
	}

	@Override
	public void writeData(NBTTagCompound nbt)
	{
		if (!fromNegative.items.isEmpty())
		{
			nbt.setTag("neg", fromNegative.serializeNBT());
		}

		if (!fromPositive.items.isEmpty())
		{
			nbt.setTag("pos", fromPositive.serializeNBT());
		}
	}

	@Override
	public void readData(NBTTagCompound nbt)
	{
		fromNegative.deserializeNBT(nbt.getTagList("neg", Constants.NBT.TAG_COMPOUND));
		fromPositive.deserializeNBT(nbt.getTagList("pos", Constants.NBT.TAG_COMPOUND));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null && side.getAxis() == getAxis() || super.hasCapability(capability, side);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null && side.getAxis() == getAxis())
		{
			return (T) (side.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? fromPositive : fromNegative);
		}

		return super.getCapability(capability, side);
	}

	public EnumFacing.Axis getAxis()
	{
		if (axis == null)
		{
			IBlockState state = world.getBlockState(pos);

			if (state.getBlock() instanceof BlockRotatedPillar)
			{
				axis = state.getValue(BlockRotatedPillar.AXIS);
			}
			else
			{
				axis = EnumFacing.Axis.Y;
			}
		}

		return axis;
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		axis = null;
	}

	@Override
	public void invalidate()
	{
		if (hasWorld())
		{
			PipeNetwork.get(getWorld()).refresh();
		}

		super.invalidate();
	}

	@Override
	public void setWorld(World world)
	{
		super.setWorld(world);

		if (hasWorld())
		{
			PipeNetwork.get(getWorld()).refresh();
		}
	}

	public void moveItems()
	{
		fromNegative.moveItems();
		fromPositive.moveItems();
	}

	public void tick()
	{
		if (!fromNegative.items.isEmpty())
		{
			fromNegative.update(FROM_AXIS[getAxis().ordinal()][EnumFacing.AxisDirection.POSITIVE.ordinal()]);
		}

		if (!fromPositive.items.isEmpty())
		{
			fromPositive.update(FROM_AXIS[getAxis().ordinal()][EnumFacing.AxisDirection.NEGATIVE.ordinal()]);
		}

		if (isDirty)
		{
			super.markDirty();

			if (!world.isRemote && sync)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 11);
			}

			isDirty = false;
		}
	}

	@Override
	public void markDirty()
	{
		isDirty = true;
		sync = true;
	}
}