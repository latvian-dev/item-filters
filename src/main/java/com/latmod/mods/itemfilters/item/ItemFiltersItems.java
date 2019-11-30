package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.filter.ANDFilterItem;
import com.latmod.mods.itemfilters.filter.AlwaysFalseFilterItem;
import com.latmod.mods.itemfilters.filter.AlwaysTrueFilterItem;
import com.latmod.mods.itemfilters.filter.ItemGroupFilterItem;
import com.latmod.mods.itemfilters.filter.ModFilterItem;
import com.latmod.mods.itemfilters.filter.NOTFilterItem;
import com.latmod.mods.itemfilters.filter.ORFilterItem;
import com.latmod.mods.itemfilters.filter.TagFilterItem;
import com.latmod.mods.itemfilters.filter.XORFilterItem;
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