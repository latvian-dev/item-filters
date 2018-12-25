package com.latmod.mods.itemfilters.item.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class OreDictionaryFilter extends com.latmod.mods.itemfilters.item.StringValueFilter
{
	private int oreID = -1;

	@Override
	public void setValue(String v)
	{
		super.setValue(v);
		oreID = -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants()
	{
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (String s : OreDictionary.getOreNames())
		{
			List<ItemStack> list = OreDictionary.getOres(s);

			if (!list.isEmpty())
			{
				StringValueFilterVariant variant = new StringValueFilterVariant(s);
				variant.icon = list.get(0);
				variants.add(variant);
			}
		}

		return variants;
	}

	public int getOreID()
	{
		if (oreID == -1)
		{
			oreID = OreDictionary.getOreID(getValue());
		}

		return oreID;
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		if (getValue().isEmpty())
		{
			return false;
		}

		for (int i : OreDictionary.getOreIDs(stack))
		{
			if (i == getOreID())
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void getValidItems(List<ItemStack> list)
	{
		if (!getValue().isEmpty())
		{
			list.addAll(OreDictionary.getOres(getValue()));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip)
	{
		if (!getValue().isEmpty())
		{
			tooltip.add(I18n.format("item.itemfilters.ore.text", TextFormatting.YELLOW + getValue()));
		}
	}
}