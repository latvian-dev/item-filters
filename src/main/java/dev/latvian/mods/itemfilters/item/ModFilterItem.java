package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

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
	@OnlyIn(Dist.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants(ItemStack stack)
	{
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (ModInfo info : ModList.get().getMods())
		{
			StringValueFilterVariant variant = new StringValueFilterVariant(info.getModId());
			variant.title = new StringTextComponent(info.getDisplayName());
			variants.add(variant);
		}

		return variants;
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		return getValue(filter).equals(stack.getItem().getRegistryName().getNamespace());
	}
}