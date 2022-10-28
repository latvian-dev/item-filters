package dev.latvian.mods.itemfilters.net;

import dev.architectury.networking.NetworkChannel;
import dev.latvian.mods.itemfilters.ItemFilters;
import net.minecraft.resources.ResourceLocation;

/**
 * @author LatvianModder
 */
public class ItemFiltersNet {
	public static final NetworkChannel MAIN = NetworkChannel.create(new ResourceLocation(ItemFilters.MOD_ID, "main"));

	public static void init() {
		MAIN.register(MessageUpdateFilterItem.class, MessageUpdateFilterItem::write, MessageUpdateFilterItem::new, MessageUpdateFilterItem::handle);
		MAIN.register(MessageClearDisplayCache.class, MessageClearDisplayCache::write, MessageClearDisplayCache::new, MessageClearDisplayCache::handle);
	}
}