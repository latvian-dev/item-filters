package com.latmod.mods.itemfilters.item.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CreativeTabFilter extends com.latmod.mods.itemfilters.item.StringValueFilter
{
	private static Field labelField;

	public static String getTabID(CreativeTabs tab)
	{
		if (labelField == null)
		{
			labelField = ObfuscationReflectionHelper.findField(CreativeTabs.class, "field_78034_o");
		}

		try
		{
			return labelField.get(tab).toString();
		}
		catch (Exception ex)
		{
			return "";
		}
	}

	private CreativeTabs tab = null;

	@Override
	public void setValue(String v)
	{
		super.setValue(v);
		tab = null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<StringValueFilterVariant> getValueVariants()
	{
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (CreativeTabs t : CreativeTabs.CREATIVE_TAB_ARRAY)
		{
			StringValueFilterVariant variant = new StringValueFilterVariant(getTabID(t));
			variant.title = I18n.format(t.getTranslationKey());
			variant.icon = t.getIcon();
			variants.add(variant);
		}

		return variants;
	}

	public CreativeTabs getTab()
	{
		if (tab == null)
		{
			for (CreativeTabs t : CreativeTabs.CREATIVE_TAB_ARRAY)
			{
				if (getValue().equals(getTabID(t)))
				{
					tab = t;
					return tab;
				}
			}

			if (tab == null)
			{
				tab = CreativeTabs.MISC;
			}
		}

		return tab;
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		if (getValue().isEmpty())
		{
			return false;
		}

		for (CreativeTabs t : stack.getItem().getCreativeTabs())
		{
			if (t == getTab())
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void getValidItems(List<ItemStack> list)
	{
		NonNullList<ItemStack> allItems = NonNullList.create();

		for (Item item : Item.REGISTRY)
		{
			item.getSubItems(getTab(), allItems);
		}

		list.addAll(allItems);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip)
	{
		if (!getValue().isEmpty())
		{
			tooltip.add(I18n.format("item.itemfilters.creative_tab.text", TextFormatting.YELLOW + I18n.format(getTab().getTranslationKey())));
		}
	}
}