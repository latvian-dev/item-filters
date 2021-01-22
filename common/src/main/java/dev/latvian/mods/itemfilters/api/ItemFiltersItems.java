package dev.latvian.mods.itemfilters.api;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.item.ANDFilterItem;
import dev.latvian.mods.itemfilters.item.AlwaysFalseFilterItem;
import dev.latvian.mods.itemfilters.item.AlwaysTrueFilterItem;
import dev.latvian.mods.itemfilters.item.BlockFilterItem;
import dev.latvian.mods.itemfilters.item.DamageFilterItem;
import dev.latvian.mods.itemfilters.item.ItemGroupFilterItem;
import dev.latvian.mods.itemfilters.item.MaxCountFilterItem;
import dev.latvian.mods.itemfilters.item.ModFilterItem;
import dev.latvian.mods.itemfilters.item.NOTFilterItem;
import dev.latvian.mods.itemfilters.item.ORFilterItem;
import dev.latvian.mods.itemfilters.item.RegExFilterItem;
import dev.latvian.mods.itemfilters.item.TagFilterItem;
import dev.latvian.mods.itemfilters.item.XORFilterItem;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ItemFiltersItems
{
	private static final Registry<Item> REGISTRY = Registries.get(ItemFilters.MOD_ID).get(net.minecraft.core.Registry.ITEM_REGISTRY);

	public static final Supplier<Item> ALWAYS_TRUE = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "always_true"), AlwaysTrueFilterItem::new);
	public static final Supplier<Item> ALWAYS_FALSE = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "always_false"), AlwaysFalseFilterItem::new);
	public static final Supplier<Item> OR = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "or"), ORFilterItem::new);
	public static final Supplier<Item> AND = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "and"), ANDFilterItem::new);
	public static final Supplier<Item> NOT = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "not"), NOTFilterItem::new);
	public static final Supplier<Item> XOR = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "xor"), XORFilterItem::new);
	public static final Supplier<Item> TAG = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "tag"), TagFilterItem::new);
	public static final Supplier<Item> MOD = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "mod"), ModFilterItem::new);
	public static final Supplier<Item> ITEM_GROUP = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "item_group"), ItemGroupFilterItem::new);
	public static final Supplier<Item> ID_REGEX = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "id_regex"), RegExFilterItem::new);
	public static final Supplier<Item> DAMAGE = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "damage"), DamageFilterItem::new);
	public static final Supplier<Item> BLOCK = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "block"), BlockFilterItem::new);
	public static final Supplier<Item> MAX_COUNT = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "max_count"), MaxCountFilterItem::new);

	public static void init()
	{
	}
}