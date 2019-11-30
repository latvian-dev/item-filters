package com.latmod.mods.itemfilters.filter;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
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