package dev.latvian.mods.itemfilters.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemInventory implements ICapabilityProvider, IInventory
{
	@CapabilityInject(ItemInventory.class)
	public static Capability<ItemInventory> CAPABILITY;

	public final LazyOptional<ItemInventory> provider = LazyOptional.of(() -> this);

	public final ItemStack filter;
	public final InventoryFilterItem filterItem;
	private List<ItemStack> items;

	public ItemInventory(ItemStack is)
	{
		filter = is;
		filterItem = (InventoryFilterItem) is.getItem();
		items = null;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		return cap == CAPABILITY ? provider.cast() : LazyOptional.empty();
	}

	public List<ItemStack> getItems()
	{
		if (items == null)
		{
			if (filter.hasTag())
			{
				ListNBT list = filter.getTag().getList("items", Constants.NBT.TAG_COMPOUND);
				items = new ArrayList<>(list.size());

				for (int i = 0; i < list.size(); i++)
				{
					ItemStack stack = ItemStack.read(list.getCompound(i));

					if (!stack.isEmpty())
					{
						items.add(stack);
					}
				}
			}
			else
			{
				items = new ArrayList<>(2);
			}
		}

		return items;
	}

	public void save()
	{
		if (filter.isEmpty())
		{
			return;
		}

		if (getItems().isEmpty())
		{
			filter.removeChildTag("items");
			return;
		}

		ListNBT list = new ListNBT();

		for (ItemStack stack : getItems())
		{
			list.add(stack.serializeNBT());
		}

		filter.setTagInfo("items", list);
	}

	@Override
	public int getSizeInventory()
	{
		return filterItem.getInventorySize(filter);
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack stack : getItems())
		{
			if (!stack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= getItems().size() ? ItemStack.EMPTY : getItems().get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public void markDirty()
	{
		save();
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void clear()
	{
		getItems().clear();
		save();
	}
}