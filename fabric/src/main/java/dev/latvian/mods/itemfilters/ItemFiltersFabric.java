package dev.latvian.mods.itemfilters;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/**
 * @author LatvianModder
 */
public class ItemFiltersFabric implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		try
		{
			ItemFilters mod = new ItemFilters();
			FabricLoader.getInstance().getEntrypoints("itemfilters-init", ItemFiltersFabricInitializer.class).forEach(ItemFiltersFabricInitializer::init);
			mod.setup();
		}
		catch (Throwable throwable)
		{
			throw new RuntimeException(throwable);
		}
	}
}
