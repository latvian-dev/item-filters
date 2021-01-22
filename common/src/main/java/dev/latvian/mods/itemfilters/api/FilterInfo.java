package dev.latvian.mods.itemfilters.api;

import net.minecraft.network.chat.Component;

/**
 * @author LatvianModder
 */
public interface FilterInfo
{
	void add(Component component);

	void push();

	void pop();
}