package dev.latvian.mods.itemfilters.api;

import dev.latvian.mods.itemfilters.ItemFilters;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ObjectHolder;

/**
 * @author LatvianModder
 */
@ObjectHolder(ItemFilters.MOD_ID)
public class ItemFiltersItems
{
	public static final Item ALWAYS_TRUE = Items.AIR;
	public static final Item ALWAYS_FALSE = Items.AIR;
	public static final Item OR = Items.AIR;
	public static final Item AND = Items.AIR;
	public static final Item NOT = Items.AIR;
	public static final Item XOR = Items.AIR;
	public static final Item TAG = Items.AIR;
	public static final Item MOD = Items.AIR;
	public static final Item ITEM_GROUP = Items.AIR;
	public static final Item ID_REGEX = Items.AIR;
	public static final Item DAMAGE = Items.AIR;
	public static final Item BLOCK = Items.AIR;
	public static final Item MAX_COUNT = Items.AIR;
}