package com.latmod.mods.itemfilters.item.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ModFilter extends com.latmod.mods.itemfilters.item.StringValueFilter
{
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

	@Override
	public void getValidItems(List<ItemStack> list)
	{
		NonNullList<ItemStack> allItems = NonNullList.create();

		for (Item item : Item.REGISTRY)
		{
			if (item.getRegistryName().getNamespace().equals(getValue()))
			{
				item.getSubItems(CreativeTabs.SEARCH, allItems);
			}
		}

		for (ItemStack stack : allItems)
		{
			if (filter(stack))
			{
				list.add(stack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip)
	{
		if (!getValue().isEmpty())
		{
			tooltip.add(I18n.format("item.itemfilters.mod.text", TextFormatting.YELLOW + Loader.instance().getIndexedModList().get(getValue()).getName()));
		}
	}
}