package com.latmod.mods.itemfilters.api;

import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class StringValueFilterVariant implements Comparable<StringValueFilterVariant>
{
	public final String id;
	public String title = "";
	public ItemStack icon = ItemStack.EMPTY;

	public StringValueFilterVariant(String s)
	{
		id = s;
	}

	public boolean equals(Object o)
	{
		return id.equals(String.valueOf(o));
	}

	public String toString()
	{
		return id;
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public int compareTo(StringValueFilterVariant o)
	{
		return title.compareToIgnoreCase(o.title);
	}
}