package com.latmod.mods.itemfilters.integration.forestry;

import com.latmod.mods.itemfilters.api.ItemFiltersAPI;

/**
 * @author LatvianModder
 */
public class ForestryIntegration
{
	public static void init()
	{
		ItemFiltersAPI.register("bee", BeeFilter::new);
	}
}