package dev.latvian.mods.itemfilters.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author LatvianModder
 */
public class StringValueFilterVariant implements Comparable<StringValueFilterVariant>
{
	public final String id;
	public ITextComponent title;
	public ItemStack icon;

	public StringValueFilterVariant(String s)
	{
		id = s;
		title = new StringTextComponent(id);
		icon = ItemStack.EMPTY;
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
		return title.getString().compareToIgnoreCase(o.title.getString());
	}
}