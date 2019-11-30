package dev.latvian.mods.itemfilters.integration.jei;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.item.ItemFiltersItems;
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