package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.item.ItemFilter;
import com.latmod.mods.itemfilters.item.ItemMissing;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

/**
 * @author LatvianModder
 */
public class ItemFiltersEventHandler
{
	private static Item withName(Item item, String name)
	{
		item.setRegistryName(name);
		return item;
	}

	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(withName(new ItemFilter(), "filter"));
		event.getRegistry().register(withName(new ItemMissing(), "missing"));
	}
}