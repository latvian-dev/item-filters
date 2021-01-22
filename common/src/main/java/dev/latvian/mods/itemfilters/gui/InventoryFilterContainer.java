package dev.latvian.mods.itemfilters.gui;

import dev.latvian.mods.itemfilters.item.InventoryFilterItem;
import dev.latvian.mods.itemfilters.item.ItemInventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class InventoryFilterContainer extends AbstractContainerMenu
{
	public static Supplier<MenuType<?>> TYPE;

	public final InteractionHand hand;
	public final ItemInventory inventory;
	public final List<InventoryFilterItem.FilterSlot> filterSlots;

	public InventoryFilterContainer(int id, Inventory playerInventory, InteractionHand h)
	{
		super(TYPE.get(), id);
		hand = h;
		inventory = InventoryFilterItem.getInventory(playerInventory.player.getItemInHand(hand));
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
				public boolean mayPickup(Player player)
				{
					return index != player.inventory.selected;
				}
			});
		}
	}

	public InventoryFilterContainer(int id, Inventory playerInventory, FriendlyByteBuf data)
	{
		this(id, playerInventory, data.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index)
	{
		if (index >= filterSlots.size() && inventory.getItems().size() < filterSlots.size() && index != player.inventory.selected + filterSlots.size())
		{
			Slot slot = getSlot(index);

			if (slot != null)
			{
				ItemStack stack = slot.getItem();

				if (!stack.isEmpty())
				{
					for (ItemStack stack1 : inventory.getItems())
					{
						if (stack1.getItem() == stack.getItem() && ItemStack.tagMatches(stack1, stack))
						{
							return ItemStack.EMPTY;
						}
					}

					ItemStack is = stack.copy();
					is.setCount(1);
					inventory.getItems().add(is);
					inventory.save();
					return stack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack clicked(int slotId, int dragType, ClickType clickType, Player player)
	{
		if (slotId >= 0 && slotId < filterSlots.size())
		{
			ItemStack stack = player.inventory.getCarried().copy();
			stack.setCount(1);

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
					if (stack1.getItem() == stack.getItem() && ItemStack.tagMatches(stack1, stack))
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

		return super.clicked(slotId, dragType, clickType, player);
	}
}
