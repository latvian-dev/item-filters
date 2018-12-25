package com.latmod.mods.itemfilters.block;

import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author LatvianModder
 */
public class TileAdvancedHopper extends TileBase implements ITickable, IWorldNameable
{
	public final ItemStackHandler buffer = new ItemStackHandler(9);
	public final ItemStack[] filters = new ItemStack[6];
	public final EnumIO[] modes = new EnumIO[6];

	public TileAdvancedHopper()
	{
		Arrays.fill(filters, ItemStack.EMPTY);
		Arrays.fill(modes, EnumIO.NONE);
	}

	@Override
	public void writeData(NBTTagCompound nbt)
	{
		NBTTagList bufferList = new NBTTagList();

		for (int i = 0; i < buffer.getSlots(); i++)
		{
			ItemStack stack = buffer.getStackInSlot(i);

			if (!stack.isEmpty())
			{
				NBTTagCompound nbt1 = stack.serializeNBT();
				nbt1.setByte("slot", (byte) i);
				bufferList.appendTag(nbt1);
			}
		}

		nbt.setTag("buffer", bufferList);

		NBTTagList configList = new NBTTagList();

		for (int i = 0; i < 6; i++)
		{
			NBTTagCompound nbt1 = filters[i].isEmpty() ? new NBTTagCompound() : filters[i].serializeNBT();

			if (modes[i] != EnumIO.NONE)
			{
				nbt1.setByte("mode", (byte) modes[i].ordinal());
			}

			configList.appendTag(nbt1);
		}

		nbt.setTag("config", configList);
	}

	@Override
	public void readData(NBTTagCompound nbt)
	{
		buffer.setSize(9);
		Arrays.fill(filters, ItemStack.EMPTY);
		Arrays.fill(modes, EnumIO.NONE);

		NBTTagList bufferList = nbt.getTagList("buffer", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < bufferList.tagCount(); i++)
		{
			NBTTagCompound nbt1 = bufferList.getCompoundTagAt(i);
			int slot = nbt1.getByte("slot");

			if (slot >= 0 && slot < buffer.getSlots())
			{
				ItemStack stack = new ItemStack(nbt1);

				if (!stack.isEmpty())
				{
					buffer.setStackInSlot(slot, stack);
				}
			}
		}

		NBTTagList configList = nbt.getTagList("config", Constants.NBT.TAG_COMPOUND);

		if (configList.tagCount() == 6)
		{
			for (int i = 0; i < 6; i++)
			{
				NBTTagCompound nbt1 = configList.getCompoundTagAt(i);
				filters[i] = new ItemStack(nbt1);

				if (filters[i].isEmpty())
				{
					filters[i] = ItemStack.EMPTY;
				}

				modes[i] = EnumIO.VALUES[MathHelper.clamp(nbt1.getByte("mode"), 0, EnumIO.VALUES.length)];
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, side);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) buffer : super.getCapability(capability, side);
	}

	@Override
	public void update()
	{
		if (!world.isRemote && (world.getTotalWorldTime() & 15L) == (hashCode() & 15L))
		{
			moveItems();
		}
	}

	private void moveItems()
	{
		IItemHandler[] inventories = new IItemHandler[6];

		for (int i = 0; i < 6; i++)
		{
			BlockPos offset = pos.offset(EnumFacing.VALUES[i]);
			TileEntity tileEntity = world.isBlockLoaded(offset) ? world.getTileEntity(offset) : null;
			inventories[i] = tileEntity == null ? null : tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.VALUES[i].getOpposite());
		}

		moveItemsOut(inventories);
		moveItemsIn(inventories);
	}

	private void moveItemsOut(IItemHandler[] inventories)
	{
		for (int i = 0; i < 6; i++)
		{
			if (inventories[i] != null && modes[i] == EnumIO.OUT && !filters[i].isEmpty())
			{
				if (transferItems(buffer, inventories[i], filters[i]))
				{
					return;
				}
			}
		}

		for (int i = 0; i < 6; i++)
		{
			if (inventories[i] != null && modes[i] == EnumIO.OUT && filters[i].isEmpty())
			{
				if (transferItems(buffer, inventories[i], ItemStack.EMPTY))
				{
					return;
				}
			}
		}
	}

	private void moveItemsIn(IItemHandler[] inventories)
	{
		for (int i = 0; i < 6; i++)
		{
			if (inventories[i] != null && modes[i] == EnumIO.IN && !filters[i].isEmpty())
			{
				if (transferItems(inventories[i], buffer, filters[i]))
				{
					return;
				}
			}
		}

		for (int i = 0; i < 6; i++)
		{
			if (inventories[i] != null && modes[i] == EnumIO.IN && filters[i].isEmpty())
			{
				if (transferItems(inventories[i], buffer, ItemStack.EMPTY))
				{
					return;
				}
			}
		}
	}

	private boolean transferItems(@Nullable IItemHandler from, @Nullable IItemHandler to, ItemStack filter)
	{
		if (from == null || to == null || from.getSlots() == 0 || to.getSlots() == 0)
		{
			return false;
		}

		for (int slot = 0; slot < from.getSlots(); slot++)
		{
			ItemStack stack = from.extractItem(slot, 64, true);

			if (!stack.isEmpty() && (filter.isEmpty() || ItemFiltersAPI.filter(filter, stack)))
			{
				ItemStack stack1 = ItemHandlerHelper.insertItem(to, stack, true);

				if (!ItemFiltersAPI.areItemStacksEqual(stack, stack1))
				{
					ItemHandlerHelper.insertItem(to, stack, false);
					from.extractItem(slot, stack.getCount(), false);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public String getName()
	{
		return ItemFiltersBlocks.ADVANCED_HOPPER.getTranslationKey() + ".name";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getName());
	}
}