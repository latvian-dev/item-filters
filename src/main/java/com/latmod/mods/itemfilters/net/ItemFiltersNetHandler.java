package com.latmod.mods.itemfilters.net;

import com.latmod.mods.itemfilters.ItemFilters;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * @author LatvianModder
 */
public class ItemFiltersNetHandler
{
	public static SimpleChannel NET;

	public static void init()
	{
		NET = NetworkRegistry.newSimpleChannel(
				new ResourceLocation(ItemFilters.MOD_ID, "channel"),
				() -> "1.0.0",
				s -> s.startsWith("1"),
				s -> s.startsWith("1")
		);
		NET.registerMessage(0, MessageUpdateItem.class, MessageUpdateItem::toBytes, MessageUpdateItem::new, (m, ctxS) -> m.onMessage(ctxS.get()));
	}
}