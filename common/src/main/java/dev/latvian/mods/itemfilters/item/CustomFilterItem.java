package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.CustomFilter;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class CustomFilterItem extends StringValueFilterItem {
	public static class CustomFilterData extends StringValueData<CustomFilter> {
		public CustomFilterData(ItemStack is) {
			super(is);
		}

		@Nullable
		@Override
		protected CustomFilter fromString(String s) {
			return s.isEmpty() ? null : ItemFiltersAPI.CUSTOM_FILTERS.get(s);
		}

		@Override
		protected String toString(CustomFilter value) {
			return value == null ? "" : value.id;
		}

		@Override
		public Component getValueAsComponent() {
			CustomFilter v = getValue();
			return v == null ? TextComponent.EMPTY : v.displayName;
		}
	}

	@Override
	public StringValueData<?> createData(ItemStack stack) {
		return new CustomFilterData(stack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants(ItemStack stack) {
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (CustomFilter filter : ItemFiltersAPI.CUSTOM_FILTERS.values()) {
			variants.add(new StringValueFilterVariant(filter.id));
		}

		return variants;
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		CustomFilterData v = getStringValueData(filter);
		return v.getValue() != null && v.getValue().predicate.test(stack);
	}

	@Override
	public boolean filterItem(ItemStack filter, Item item) {
		if (item == Items.AIR) {
			return false;
		}

		CustomFilterData v = getStringValueData(filter);
		return v.getValue() != null && v.getValue().getItems().contains(item);
	}

	@Override
	public void getItems(ItemStack filter, Set<Item> set) {
		CustomFilterData v = getStringValueData(filter);

		if (v.getValue() != null) {
			set.addAll(v.getValue().getItems());
		}
	}
}