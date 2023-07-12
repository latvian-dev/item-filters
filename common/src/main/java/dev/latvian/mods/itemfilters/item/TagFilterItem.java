package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class TagFilterItem extends StringValueFilterItem {
	public static class TagData extends StringValueData<TagKey<Item>> {
		public TagData(ItemStack is) {
			super(is);
		}

		@Nullable
		@Override
		public TagKey<Item> fromString(String s) {
			if (s.isEmpty()) {
				return null;
			}

			ResourceLocation id = new ResourceLocation(s);
			return TagKey.create(Registries.ITEM, id);
		}

		@Override
		public String toString(TagKey<Item> value) {
			return value == null ? "" : value.location().toString();
		}
	}

	@Override
	public StringValueData<?> createData(ItemStack stack) {
		return new TagData(stack);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		return stack.is(TagKey.create(Registries.ITEM, new ResourceLocation(getValue(filter))));
	}

	@Override
	public boolean filterItem(ItemStack filter, Item item) {
		if (item == Items.AIR) {
			return false;
		}

		return item.builtInRegistryHolder().is(TagKey.create(Registries.ITEM, new ResourceLocation(getValue(filter))));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants(ItemStack filter) {
		return BuiltInRegistries.ITEM.getTags().map(e -> {
			StringValueFilterVariant variant = new StringValueFilterVariant(e.getFirst().location().toString());
			variant.icon = e.getSecond().stream().findAny().map(ItemStack::new).orElse(ItemStack.EMPTY);
			return variant;
		}).toList();
	}
}