package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	@SideOnly(Side.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants()
	{
		HashSet<String> modIDs = new HashSet<>();

		for (Item item : Item.REGISTRY)
		{
			modIDs.add(item.getRegistryName().getNamespace());
		}

		Collection<StringValueFilterVariant> variants = new ArrayList<>();

		for (String id : modIDs)
		{
			ModContainer container = Loader.instance().getIndexedModList().get(id);
			StringValueFilterVariant variant = new StringValueFilterVariant(container.getModId());
			variant.title = container.getName();
			variants.add(variant);
		}

		return variants;
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		return !getValue().isEmpty() && stack.getItem().getRegistryName().getNamespace().equals(getValue());
	}
}