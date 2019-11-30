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
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new ItemFilter().setRegistryName("filter"));
		event.getRegistry().register(new ItemMissing().setRegistryName("missing"));
	}
}