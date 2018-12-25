package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.ItemFilters;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author LatvianModder
 */
@GameRegistry.ObjectHolder(ItemFilters.MOD_ID)
public class ItemFiltersItems
{
	public static final Item ADVANCED_HOPPER = Items.AIR;

	public static final Item BASIC = Items.AIR;
	public static final Item NOT = Items.AIR;
	public static final Item OR = Items.AIR;
	public static final Item AND = Items.AIR;
	public static final Item XOR = Items.AIR;
	public static final Item ORE = Items.AIR;
	public static final Item CREATIVE_TAB = Items.AIR;
	public static final Item MOD = Items.AIR;
}