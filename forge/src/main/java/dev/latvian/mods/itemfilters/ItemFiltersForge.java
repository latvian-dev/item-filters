package dev.latvian.mods.itemfilters;

import me.shedaniel.architectury.platform.forge.EventBuses;
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
