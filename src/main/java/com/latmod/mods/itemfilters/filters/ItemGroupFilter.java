package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemGroupFilter extends StringValueFilter
{
	private ItemGroup tab = null;

	@SuppressWarnings("deprecation")
	public static String getTabID(ItemGroup tab)
	{
		try
		{
			return tab.getTabLabel().toString();
		}
		catch (Exception ex)
		{
			return "";
		}
	}

	@Override
	public String getID()
	{
		return "creative_tab";
	}

	@Override
	public void setValue(String v)
	{
		super.setValue(v);
		tab = null;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public List<StringValueFilterVariant> getValueVariants()
	{
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (ItemGroup t : ItemGroup.GROUPS)
		{
			if (t == ItemGroup.SEARCH || t == ItemGroup.INVENTORY)
			{
				continue;
			}

			StringValueFilterVariant variant = new StringValueFilterVariant(getTabID(t));
			variant.title = I18n.format(t.getTranslationKey());
			variant.icon = t.getIcon();
			variants.add(variant);
		}

		return variants;
	}

	public ItemGroup getTab()
	{
		if (tab == null)
		{
			for (ItemGroup t : ItemGroup.GROUPS)
			{
				if (getValue().equals(getTabID(t)))
				{
					tab = t;
					return tab;
				}
			}

			if (tab == null)
			{
				tab = ItemGroup.MISC;
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

		for (ItemGroup t : stack.getItem().getCreativeTabs())
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
		if (cachedItems == null)
		{
			NonNullList<ItemStack> allItems = NonNullList.create();

			for (Item item : ForgeRegistries.ITEMS.getValues())
			{
				item.fillItemGroup(getTab(), allItems);
			}

			cachedItems = compress(allItems);
		}

		if (!cachedItems.isEmpty())
		{
			list.addAll(cachedItems);
		}
	}
}