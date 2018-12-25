package com.latmod.mods.itemfilters.client;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ItemFilters.MOD_ID, value = Side.CLIENT)
public class ItemFiltersClientEventHandler
{
	private static void addModel(Item item, String variant)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		addModel(ItemFiltersItems.ADVANCED_HOPPER, "normal");
		addModel(ItemFiltersItems.BASIC, "inventory");
		addModel(ItemFiltersItems.NOT, "inventory");
		addModel(ItemFiltersItems.OR, "inventory");
		addModel(ItemFiltersItems.AND, "inventory");
		addModel(ItemFiltersItems.XOR, "inventory");
		addModel(ItemFiltersItems.ORE, "inventory");
		addModel(ItemFiltersItems.CREATIVE_TAB, "inventory");
		addModel(ItemFiltersItems.MOD, "inventory");
	}
}