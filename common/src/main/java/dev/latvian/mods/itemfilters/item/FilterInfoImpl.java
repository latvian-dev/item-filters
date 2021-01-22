package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.FilterInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

/**
 * @author LatvianModder
 */
public class FilterInfoImpl implements FilterInfo
{
	private final List<Component> list;
	private String indent = "";

	public FilterInfoImpl(List<Component> l)
	{
		list = l;
	}

	@Override
	public void add(Component component)
	{
		list.add(new TextComponent(indent + "- ").withStyle(ChatFormatting.YELLOW).append((new TextComponent("").append(component)).withStyle(ChatFormatting.GRAY)));
	}

	@Override
	public void push()
	{
		indent += "  ";
	}

	@Override
	public void pop()
	{
		indent = indent.substring(2);
	}
}