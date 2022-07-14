package dev.latvian.mods.itemfilters.item;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

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

		var mods = new LinkedHashSet<>();

		for (var itemEntry : Registry.ITEM.entrySet()) {
			var id = itemEntry.getKey().location().getNamespace();
			if (mods.add(id)) {
				var variant = new StringValueFilterVariant(id);
				variant.title = new TextComponent(Platform.getOptionalMod(id).map(Mod::getName).orElse(id));
				variant.icon = new ItemStack(itemEntry.getValue());
				if(id.equals("minecraft")) {
					variant.icon = Items.GRASS_BLOCK.getDefaultInstance();;
				}
				variants.add(variant);
			}
		}

		return variants;
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		return !stack.isEmpty() && getValue(filter).equals(Registry.ITEM.getKey(stack.getItem()).getNamespace());
	}
}