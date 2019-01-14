package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.item.ItemFilter;
import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import com.latmod.mods.itemfilters.item.ItemMissing;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
		item.setRegistryName(name);
		item.setTranslationKey(ItemFilters.MOD_ID + "." + name);
		return item;
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(withName(new ItemFilter(), "filter").setCreativeTab(CreativeTabs.TRANSPORTATION));
		event.getRegistry().register(withName(new ItemMissing(), "missing").setCreativeTab(CreativeTabs.MISC));
	}

	@SubscribeEvent
	public static void remapItems(RegistryEvent.MissingMappings<Item> event)
	{
		ResourceLocation MISSING_ID = new ResourceLocation("ftbquests:missing");

		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings())
		{
			if (mapping.key.equals(MISSING_ID))
			{
				mapping.remap(ItemFiltersItems.MISSING);
			}
		}
	}
}