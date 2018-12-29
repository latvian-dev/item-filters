package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.block.BlockAdvancedHopper;
import com.latmod.mods.itemfilters.block.BlockPipe;
import com.latmod.mods.itemfilters.block.ItemFiltersBlocks;
import com.latmod.mods.itemfilters.block.PipeNetwork;
import com.latmod.mods.itemfilters.block.TileAdvancedHopper;
import com.latmod.mods.itemfilters.block.TilePipe;
import com.latmod.mods.itemfilters.item.ItemFilter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ItemFilters.MOD_ID)
public class ItemFiltersEventHandler
{
	private static final ResourceLocation WORLD_CAP_ID = new ResourceLocation(ItemFilters.MOD_ID, "pipe_network");

	private static Block withName(Block block, String name)
	{
		block.setCreativeTab(ItemFilters.TAB);
		block.setRegistryName(name);
		block.setTranslationKey(ItemFilters.MOD_ID + "." + name);
		return block;
	}

	private static Item withName(Item item, String name)
	{
		item.setCreativeTab(ItemFilters.TAB);
		item.setRegistryName(name);
		item.setTranslationKey(ItemFilters.MOD_ID + "." + name);
		return item;
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(withName(new BlockAdvancedHopper(), "advanced_hopper"));
		event.getRegistry().register(withName(new BlockPipe(), "cobblestone_pipe"));

		GameRegistry.registerTileEntity(TileAdvancedHopper.class, new ResourceLocation(ItemFilters.MOD_ID, "advanced_hopper"));
		GameRegistry.registerTileEntity(TilePipe.class, new ResourceLocation(ItemFilters.MOD_ID, "cobblestone_pipe"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new ItemBlock(ItemFiltersBlocks.ADVANCED_HOPPER).setRegistryName("advanced_hopper"));
		event.getRegistry().register(new ItemBlock(ItemFiltersBlocks.COBBLESTONE_PIPE).setRegistryName("cobblestone_pipe"));
		event.getRegistry().register(withName(new ItemFilter(), "filter"));
	}

	@SubscribeEvent
	public static void attachWorldCap(AttachCapabilitiesEvent<World> event)
	{
		event.addCapability(WORLD_CAP_ID, new PipeNetwork(event.getObject()));
	}

	@SubscribeEvent
	public static void tickServerWorld(TickEvent.WorldTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			PipeNetwork.get(event.world).tick();
		}
	}
}