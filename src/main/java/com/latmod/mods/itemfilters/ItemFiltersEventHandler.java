package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.item.ItemFilter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ItemFilters.MOD_ID)
public class ItemFiltersEventHandler
{
	private static Item withName(Item item, String name)
	{
		item.setCreativeTab(CreativeTabs.TRANSPORTATION);
		item.setRegistryName(name);
		item.setTranslationKey(ItemFilters.MOD_ID + "." + name);
		return item;
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(withName(new ItemFilter(), "filter"));
	}
}