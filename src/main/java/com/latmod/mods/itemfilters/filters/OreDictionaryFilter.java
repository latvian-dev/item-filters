package com.latmod.mods.itemfilters.filters;

import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
//TODO: Replace with a Tag filter
public class OreDictionaryFilter extends StringValueFilter
{
	@Override
	public String getID()
	{
		return null;
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		return false;
	}

	//	private int oreID = -1;
	//
	//	@Override
	//	public String getID()
	//	{
	//		return "ore";
	//	}
	//
	//	@Override
	//	public void setValue(String v)
	//	{
	//		super.setValue(v);
	//		oreID = -1;
	//	}
	//
	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public Collection<StringValueFilterVariant> getValueVariants()
	//	{
	//		List<StringValueFilterVariant> variants = new ArrayList<>();
	//
	//		for (String s : OreDictionary.getOreNames())
	//		{
	//			List<ItemStack> list = OreDictionary.getOres(s);
	//
	//			if (!list.isEmpty())
	//			{
	//				StringValueFilterVariant variant = new StringValueFilterVariant(s);
	//				variant.icon = list.get(0).copy();
	//
	//				if (variant.icon.getItemDamage() == OreDictionary.WILDCARD_VALUE)
	//				{
	//					variant.icon.setItemDamage(0);
	//				}
	//
	//				variants.add(variant);
	//			}
	//		}
	//
	//		return variants;
	//	}
	//
	//	public int getOreID()
	//	{
	//		if (oreID == -1)
	//		{
	//			oreID = OreDictionary.getOreID(getValue());
	//		}
	//
	//		return oreID;
	//	}
	//
	//	@Override
	//	public boolean filter(ItemStack stack)
	//	{
	//		if (getValue().isEmpty() || stack.isEmpty())
	//		{
	//			return false;
	//		}
	//
	//		for (int i : OreDictionary.getOreIDs(stack))
	//		{
	//			if (i == getOreID())
	//			{
	//				return true;
	//			}
	//		}
	//
	//		return false;
	//	}
	//
	//	@Override
	//	public void getValidItems(List<ItemStack> list)
	//	{
	//		if (cachedItems == null)
	//		{
	//			cachedItems = new ArrayList<>();
	//
	//			if (!getValue().isEmpty())
	//			{
	//				for (ItemStack stack : OreDictionary.getOres(getValue()))
	//				{
	//					if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
	//					{
	//						NonNullList<ItemStack> list1 = NonNullList.create();
	//						stack.getItem().getSubItems(CreativeTabs.SEARCH, list1);
	//						cachedItems.addAll(list1);
	//					}
	//					else
	//					{
	//						cachedItems.add(stack);
	//					}
	//				}
	//			}
	//
	//			cachedItems = compress(cachedItems);
	//		}
	//
	//		if (!cachedItems.isEmpty())
	//		{
	//			list.addAll(cachedItems);
	//		}
	//	}
}