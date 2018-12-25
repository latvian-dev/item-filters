package com.latmod.mods.itemfilters.net;

import com.latmod.mods.itemfilters.ItemFilters;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public class ItemFiltersNetHandler
{
	public static SimpleNetworkWrapper NET;

	public static void init()
	{
		NET = new SimpleNetworkWrapper(ItemFilters.MOD_ID);
		NET.registerMessage(new MessageUpdateItem.Handler(), MessageUpdateItem.class, 1, Side.SERVER);
	}
}