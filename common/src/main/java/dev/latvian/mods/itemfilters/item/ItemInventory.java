package dev.latvian.mods.itemfilters.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemInventory implements Container
{
	public final ItemStack filter;
	public final InventoryFilterItem filterItem;
	private List<ItemStack> items;

	public ItemInventory(ItemStack is)
	{
		filter = is;
		filterItem = (InventoryFilterItem) is.getItem();
		items = null;
	}

	public List<ItemStack> getItems()
	{
		if (items == null)
		{
			if (filter.hasTag())
			{
				ListTag list = filter.getTag().getList("items", 10);
				items = new ArrayList<>(list.size());

				for (int i = 0; i < list.size(); i++)
				{
					ItemStack stack = ItemStack.of(list.getCompound(i));

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
			filter.removeTagKey("items");
			return;
		}

		ListTag list = new ListTag();

		for (ItemStack stack : getItems())
		{
			list.add(stack.save(new CompoundTag()));
		}

		filter.addTagElement("items", list);
	}

	@Override
	public int getContainerSize()
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
	public ItemStack getItem(int index)
	{
		return index >= getItems().size() ? ItemStack.EMPTY : getItems().get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack stack)
	{
	}

	@Override
	public int getMaxStackSize()
	{
		return 1;
	}

	@Override
	public void setChanged()
	{
		save();
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	@Override
	public void clearContent()
	{
		getItems().clear();
		save();
	}
}