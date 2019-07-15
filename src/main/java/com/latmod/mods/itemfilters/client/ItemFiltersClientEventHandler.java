package com.latmod.mods.itemfilters.client;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ItemFilters.MOD_ID, value = Dist.CLIENT)
public class ItemFiltersClientEventHandler
{
	private static void addModel(Item item, String variant)
	{
		//		ModelLoader.onRegisterItems(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		addModel(ItemFiltersItems.FILTER, "inventory");
		addModel(ItemFiltersItems.MISSING, "inventory");
	}
}