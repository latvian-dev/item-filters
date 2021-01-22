package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import me.shedaniel.architectury.platform.Mod;
import me.shedaniel.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ModFilterItem extends StringValueFilterItem
{
	private static class ModData extends StringValueData<String>
	{
		public ModData(ItemStack is)
		{
			super(is);
		}

		@Override
		public String fromString(String s)
		{
			return s;
		}

		@Override
		public String toString(String value)
		{
			return value;
		}
	}

	@Override
	public StringValueData createData(ItemStack stack)
	{
		return new ModData(stack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants(ItemStack stack)
	{
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (Mod info : Platform.getMods())
		{
			StringValueFilterVariant variant = new StringValueFilterVariant(info.getModId());
			variant.title = new TextComponent(info.getName());
			variants.add(variant);
		}

		return variants;
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		return getValue(filter).equals(Registry.ITEM.getKey(stack.getItem()).getNamespace());
	}
}