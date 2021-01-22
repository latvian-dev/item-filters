package dev.latvian.mods.itemfilters.core;

import dev.latvian.mods.itemfilters.item.InventoryFilterItem;
import dev.latvian.mods.itemfilters.item.ItemInventory;
import dev.latvian.mods.itemfilters.item.StringValueData;
import dev.latvian.mods.itemfilters.item.StringValueFilterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * @author LatvianModder
 */
public interface ItemFiltersStack
{
	Object getItemFiltersData();

	default Object createDataIF(Item item)
	{
		if (item instanceof InventoryFilterItem)
		{
			return new ItemInventory((ItemStack) (Object) this);
		}
		else if (item instanceof StringValueFilterItem)
		{
			return ((StringValueFilterItem) item).createData((ItemStack) (Object) this);
		}

		return null;
	}

	default StringValueData<?> getStringValueFilterData()
	{
		return (StringValueData<?>) getItemFiltersData();
	}

	default ItemInventory getInventoryFilterData()
	{
		return (ItemInventory) getItemFiltersData();
	}
}
