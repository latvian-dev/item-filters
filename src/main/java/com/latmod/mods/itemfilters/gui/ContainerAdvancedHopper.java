package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.block.EnumIO;
import com.latmod.mods.itemfilters.block.TileAdvancedHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author LatvianModder
 */
public class ContainerAdvancedHopper extends Container
{
	public final EntityPlayer player;
	public final TileAdvancedHopper hopper;

	public ContainerAdvancedHopper(EntityPlayer ep, TileAdvancedHopper h)
	{
		player = ep;
		hopper = h;

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				addSlotToContainer(new SlotItemHandler(h.buffer, j + i * 3, 62 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (index < 9)
			{
				if (!mergeItemStack(stack1, 9, 45, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(stack1, 0, 9, false))
			{
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (stack1.getCount() == stack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(player, stack1);
		}

		return stack;
	}

	@Override
	public boolean enchantItem(EntityPlayer player, int id)
	{
		if (id >= 0 && id <= 5)
		{
			hopper.modes[id] = EnumIO.VALUES[(hopper.modes[id].ordinal() + 1) % EnumIO.VALUES.length];
			hopper.markDirty();
			return true;
		}
		else if (id >= 6 && id <= 11)
		{
			hopper.filters[id - 6] = player.inventory.getItemStack().isEmpty() ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(player.inventory.getItemStack(), 1);
			hopper.markDirty();
			return true;
		}

		return false;
	}
}