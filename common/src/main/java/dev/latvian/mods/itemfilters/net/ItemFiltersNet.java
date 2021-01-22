package dev.latvian.mods.itemfilters.net;

import dev.latvian.mods.itemfilters.ItemFilters;
import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;

import static me.shedaniel.architectury.networking.NetworkManager.*;

/**
 * @author LatvianModder
 */
public class ItemFiltersNet
{
	public static final NetworkChannel MAIN = NetworkChannel.create(new ResourceLocation(ItemFilters.MOD_ID, "main"));

	public static void init()
	{
		MAIN.register(clientToServer(), 1, MessageUpdateFilterItem.class, MessageUpdateFilterItem::write, MessageUpdateFilterItem::new, MessageUpdateFilterItem::handle);
	}
}