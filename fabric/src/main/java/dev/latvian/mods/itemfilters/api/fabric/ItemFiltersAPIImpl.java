package dev.latvian.mods.itemfilters.api.fabric;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

/**
 * @author LatvianModder
 */
public class ItemFiltersAPIImpl
{
	public static Tag.Named<Item> getNamedTag(String s)
	{
		return ItemTags.bind(s);
	}
}
