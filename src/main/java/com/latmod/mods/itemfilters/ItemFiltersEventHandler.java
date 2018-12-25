package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.block.BlockAdvancedHopper;
import com.latmod.mods.itemfilters.block.ItemFiltersBlocks;
import com.latmod.mods.itemfilters.block.TileAdvancedHopper;
import com.latmod.mods.itemfilters.item.ItemFilter;
import com.latmod.mods.itemfilters.item.filters.ANDFilter;
import com.latmod.mods.itemfilters.item.filters.BasicItemFilter;
import com.latmod.mods.itemfilters.item.filters.CreativeTabFilter;
import com.latmod.mods.itemfilters.item.filters.ModFilter;
import com.latmod.mods.itemfilters.item.filters.NOTFilter;
import com.latmod.mods.itemfilters.item.filters.ORFilter;
import com.latmod.mods.itemfilters.item.filters.OreDictionaryFilter;
import com.latmod.mods.itemfilters.item.filters.XORFilter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ItemFilters.MOD_ID)
public class ItemFiltersEventHandler
{
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
		GameRegistry.registerTileEntity(TileAdvancedHopper.class, new ResourceLocation(ItemFilters.MOD_ID, "advanced_hopper"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new ItemBlock(ItemFiltersBlocks.ADVANCED_HOPPER).setRegistryName("advanced_hopper"),
				withName(new ItemFilter(BasicItemFilter::new), "basic"),
				withName(new ItemFilter(NOTFilter::new), "not"),
				withName(new ItemFilter(ORFilter::new), "or"),
				withName(new ItemFilter(ANDFilter::new), "and"),
				withName(new ItemFilter(XORFilter::new), "xor"),
				withName(new ItemFilter(OreDictionaryFilter::new), "ore"),
				withName(new ItemFilter(CreativeTabFilter::new), "creative_tab"),
				withName(new ItemFilter(ModFilter::new), "mod")
		);
	}
}