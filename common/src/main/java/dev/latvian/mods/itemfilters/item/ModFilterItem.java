package dev.latvian.mods.itemfilters.item;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ModFilterItem extends StringValueFilterItem {
	@Override
	public StringValueData createData(ItemStack stack) {
		return new SimpleStringData(stack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants(ItemStack stack) {
		List<StringValueFilterVariant> variants = new ArrayList<>();

		LinkedHashSet<String> mods = new LinkedHashSet<>();

		for (Map.Entry<ResourceKey<Item>, Item> entry : Registry.ITEM.entrySet()) {
			if (mods.add(entry.getKey().location().getNamespace())) {
				Mod info = Platform.getMod(entry.getKey().location().getNamespace());
				StringValueFilterVariant variant = new StringValueFilterVariant(info.getModId());
				variant.title = new TextComponent(info.getName());
				variant.icon = new ItemStack(entry.getValue());
				variants.add(variant);
			}
		}

		variants.stream().filter(variant -> variant.id.equals("minecraft")).findFirst().ifPresent(variant -> variant.icon = new ItemStack(Items.GRASS_BLOCK));

		return variants;
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		return !stack.isEmpty() && getValue(filter).equals(Registry.ITEM.getKey(stack.getItem()).getNamespace());
	}
}