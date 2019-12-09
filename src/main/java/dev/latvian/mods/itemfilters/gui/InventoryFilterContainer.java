package dev.latvian.mods.itemfilters.gui;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.item.InventoryFilterItem;
import dev.latvian.mods.itemfilters.item.ItemInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class InventoryFilterContainer extends Container
{
	@ObjectHolder(ItemFilters.MOD_ID + ":inventory_filter")
	public static ContainerType<InventoryFilterContainer> TYPE;

	public final Hand hand;
	public final ItemInventory inventory;
	public final List<InventoryFilterItem.FilterSlot> filterSlots;

	public InventoryFilterContainer(int id, PlayerInventory playerInventory, Hand h)
	{
		super(TYPE, id);
		hand = h;
		inventory = InventoryFilterItem.getInventory(playerInventory.player.getHeldItem(hand));
		filterSlots = new ArrayList<>();
		inventory.filterItem.addSlots(inventory.filter, filterSlots);

		for (int i = 0; i < filterSlots.size(); i++)
		{
			InventoryFilterItem.FilterSlot s = filterSlots.get(i);
			addSlot(new Slot(inventory, i, s.x, s.y));
		}

		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++)
		{
			addSlot(new Slot(playerInventory, x, 8 + x * 18, 142)
			{
				@Override
				public boolean canTakeStack(PlayerEntity player)
				{
					return getSlotIndex() != player.inventory.currentItem;
				}
			});
		}
	}

	public InventoryFilterContainer(int id, PlayerInventory playerInventory, PacketBuffer data)
	{
		this(id, playerInventory, data.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND);
	}

	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index)
	{
		if (index >= filterSlots.size() && inventory.getItems().size() < filterSlots.size() && index != player.inventory.currentItem + filterSlots.size())
		{
			Slot slot = getSlot(index);

			if (slot != null)
			{
				ItemStack stack = slot.getStack();

				if (!stack.isEmpty())
				{
					for (ItemStack stack1 : inventory.getItems())
					{
						if (stack1.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack))
						{
							return ItemStack.EMPTY;
						}
					}

					inventory.getItems().add(ItemHandlerHelper.copyStackWithSize(stack, 1));
					inventory.save();
					return stack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickType, PlayerEntity player)
	{
		if (slotId >= 0 && slotId < filterSlots.size())
		{
			ItemStack stack = ItemHandlerHelper.copyStackWithSize(player.inventory.getItemStack(), 1);

			if (stack.isEmpty())
			{
				if (slotId < inventory.getItems().size())
				{
					ItemStack stack1 = inventory.getItems().get(slotId);
					inventory.getItems().remove(slotId);
					inventory.save();
					return stack1;
				}
			}
			else
			{
				for (ItemStack stack1 : inventory.getItems())
				{
					if (stack1.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack))
					{
						return ItemStack.EMPTY;
					}
				}

				if (slotId < inventory.getItems().size())
				{
					inventory.getItems().set(slotId, stack);
					inventory.save();
				}
				else
				{
					inventory.getItems().add(stack);
					inventory.save();
				}

				return ItemStack.EMPTY;
			}
		}

		return super.slotClick(slotId, dragType, clickType, player);
	}
}
