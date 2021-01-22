package dev.latvian.mods.itemfilters.core;

import dev.latvian.mods.itemfilters.item.ItemInventory;
import dev.latvian.mods.itemfilters.item.StringValueData;

/**
 * @author LatvianModder
 */
public interface ItemFiltersStack
{
	Object getItemFiltersData();

	default StringValueData<?> getStringValueData()
	{
		return (StringValueData<?>) getItemFiltersData();
	}

	default ItemInventory getInventoryFilterData()
	{
		return (ItemInventory) getItemFiltersData();
	}
}
