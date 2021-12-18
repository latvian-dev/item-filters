package dev.latvian.mods.itemfilters.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.latvian.mods.itemfilters.ItemFilters;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author LatvianModder
 */
@Mod(ItemFilters.MOD_ID)
public class ItemFiltersForge {
	public ItemFiltersForge() {
		EventBuses.registerModEventBus(ItemFilters.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		ItemFilters mod = new ItemFilters();
		mod.setup();
	}
}
