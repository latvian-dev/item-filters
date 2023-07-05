package dev.latvian.mods.itemfilters.api;

import dev.architectury.registry.registries.RegistrarManager;
import dev.latvian.mods.itemfilters.ItemFilters;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class CustomFilter {
	public final String id;
	public final Predicate<ItemStack> predicate;
	private Set<Item> items;
	public Component displayName;

	public CustomFilter(String i, Predicate<ItemStack> p) {
		id = i;
		predicate = p;
		displayName = Component.literal(i);
	}

	public Set<Item> getItems() {
		if (items == null) {
			items = new LinkedHashSet<>();

			for (Item item : BuiltInRegistries.ITEM) {
				if (predicate.test(new ItemStack(item))) {
					items.add(item);
				}
			}
		}

		return items;
	}
}
