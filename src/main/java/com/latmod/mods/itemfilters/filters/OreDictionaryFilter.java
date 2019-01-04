package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import com.latmod.mods.itemfilters.item.StringValueFilter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class OreDictionaryFilter extends StringValueFilter
{
	private int oreID = -1;

	@Override
	public String getID()
	{
		return "ore";
	}

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
				variant.icon = list.get(0).copy();

				if (variant.icon.getItemDamage() == OreDictionary.WILDCARD_VALUE)
				{
					variant.icon.setItemDamage(0);
				}

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
}