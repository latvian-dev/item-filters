package dev.latvian.mods.itemfilters.fabric;

import dev.latvian.mods.itemfilters.ItemFilters;
import net.fabricmc.api.ModInitializer;

/**
 * @author LatvianModder
 */
public class ItemFiltersFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		new ItemFilters().setup();
	}
}
