package com.latmod.mods.itemfilters.client;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.block.PipeNetwork;
import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
		addModel(ItemFiltersItems.COBBLESTONE_PIPE, "axis=y");
		addModel(ItemFiltersItems.FILTER, "inventory");
	}

	@SubscribeEvent
	public static void tickClientWorld(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (event.phase == TickEvent.Phase.END && mc.world != null && !mc.isGamePaused())
		{
			PipeNetwork.get(mc.world).tick();
		}
	}

	@SubscribeEvent
	public static void renderWorld(RenderWorldLastEvent event)
	{
		PipeNetwork.get(Minecraft.getMinecraft().world).render(event.getPartialTicks());
	}
}