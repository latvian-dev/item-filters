package dev.latvian.mods.itemfilters.api;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ItemFiltersItems {
	public static RegistrySupplier<CreativeModeTab> CREATIVE_TAB;

	private static final Registrar<Item> REGISTRY = RegistrarManager.get(ItemFilters.MOD_ID).get(Registries.ITEM);

	public static final Supplier<Item> ALWAYS_TRUE = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "always_true"), AlwaysTrueFilterItem::new);
	public static final Supplier<Item> ALWAYS_FALSE = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "always_false"), AlwaysFalseFilterItem::new);
	public static final Supplier<Item> OR = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "or"), ORFilterItem::new);
	public static final Supplier<Item> AND = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "and"), ANDFilterItem::new);
	public static final Supplier<Item> NOT = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "not"), NOTFilterItem::new);
	public static final Supplier<Item> XOR = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "xor"), XORFilterItem::new);
	public static final Supplier<Item> TAG = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "tag"), TagFilterItem::new);
	public static final Supplier<Item> MOD = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "mod"), ModFilterItem::new);
//	public static final Supplier<Item> ITEM_GROUP = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "item_group"), ItemGroupFilterItem::new);
	public static final Supplier<Item> ID_REGEX = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "id_regex"), RegExFilterItem::new);
	public static final Supplier<Item> DAMAGE = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "damage"), DamageFilterItem::new);
	public static final Supplier<Item> BLOCK = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "block"), BlockFilterItem::new);
	public static final Supplier<Item> MAX_COUNT = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "max_count"), MaxCountFilterItem::new);
	public static final Supplier<Item> STRONG_NBT = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "strong_nbt"), StrongNBTFilterItem::new);
	public static final Supplier<Item> WEAK_NBT = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "weak_nbt"), WeakNBTFilterItem::new);
	public static final Supplier<Item> CUSTOM = REGISTRY.register(new ResourceLocation(ItemFilters.MOD_ID, "custom"), CustomFilterItem::new);

	public static void init() {
	}
}