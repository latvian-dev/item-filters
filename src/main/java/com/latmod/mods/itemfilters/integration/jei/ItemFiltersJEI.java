package com.latmod.mods.itemfilters.integration.jei;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class ItemFiltersJEI implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation(ItemFilters.MOD_ID, "jei");
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		registration.useNbtForSubtypes(ItemFiltersItems.MISSING);
	}
}