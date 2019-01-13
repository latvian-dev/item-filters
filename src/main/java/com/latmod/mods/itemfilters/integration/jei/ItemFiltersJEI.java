package com.latmod.mods.itemfilters.integration.jei;

import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;

/**
 * @author LatvianModder
 */
@JEIPlugin
public class ItemFiltersJEI implements IModPlugin
{
	@Override
	public void registerItemSubtypes(ISubtypeRegistry r)
	{
		r.useNbtForSubtypes(ItemFiltersItems.MISSING);
	}
}