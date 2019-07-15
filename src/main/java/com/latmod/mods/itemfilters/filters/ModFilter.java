package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author LatvianModder
 */
public class ModFilter extends StringValueFilter
{
	@Override
	public String getID()
	{
		return "mod";
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants()
	{
		HashSet<String> modIDs = new HashSet<>();

		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			modIDs.add(item.getRegistryName().getNamespace());
		}

		Collection<StringValueFilterVariant> variants = new ArrayList<>();

		for (String id : modIDs)
		{
			ModList.get().getModContainerById(id).ifPresent(container -> {
				StringValueFilterVariant variant = new StringValueFilterVariant(container.getModId());
				variant.title = container.getNamespace();
				variants.add(variant);
			});
		}

		return variants;
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		return !getValue().isEmpty() && stack.getItem().getRegistryName().getNamespace().equals(getValue());
	}
}