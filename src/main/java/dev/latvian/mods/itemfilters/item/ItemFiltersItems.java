package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.filter.ANDFilterItem;
import dev.latvian.mods.itemfilters.filter.AlwaysFalseFilterItem;
import dev.latvian.mods.itemfilters.filter.AlwaysTrueFilterItem;
import dev.latvian.mods.itemfilters.filter.ItemGroupFilterItem;
import dev.latvian.mods.itemfilters.filter.ModFilterItem;
import dev.latvian.mods.itemfilters.filter.NOTFilterItem;
import dev.latvian.mods.itemfilters.filter.ORFilterItem;
import dev.latvian.mods.itemfilters.filter.TagFilterItem;
import dev.latvian.mods.itemfilters.filter.XORFilterItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

/**
 * @author LatvianModder
 */
@ObjectHolder(ItemFilters.MOD_ID)
public class ItemFiltersItems
{
	public static final Item MISSING = Items.AIR;

	public static void register(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new ItemMissing().setRegistryName("missing"));

		event.getRegistry().registerAll(
				new AlwaysTrueFilterItem().setRegistryName("always_true"),
				new AlwaysFalseFilterItem().setRegistryName("always_false"),
				new ORFilterItem().setRegistryName("or"),
				new ANDFilterItem().setRegistryName("and"),
				new NOTFilterItem().setRegistryName("not"),
				new XORFilterItem().setRegistryName("xor"),
				new TagFilterItem().setRegistryName("tag"),
				new ModFilterItem().setRegistryName("mod"),
				new ItemGroupFilterItem().setRegistryName("item_group")
		);
	}
}