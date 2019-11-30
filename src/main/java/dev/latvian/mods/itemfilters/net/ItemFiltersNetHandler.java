package dev.latvian.mods.itemfilters.net;

import dev.latvian.mods.itemfilters.ItemFilters;
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

		NET.registerMessage(1, MessageUpdateFilterItem.class, MessageUpdateFilterItem::write, MessageUpdateFilterItem::new, MessageUpdateFilterItem::handle);
	}
}