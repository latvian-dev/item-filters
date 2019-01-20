package com.latmod.mods.itemfilters.filters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public enum NBTMatchingMode
{
	MATCH("match"),
	IGNORE("ignore"),
	CONTAIN("contain");

	public static final NBTMatchingMode[] VALUES = values();
	public static final Map<String, NBTMatchingMode> MAP = new HashMap<>();

	static
	{
		for (NBTMatchingMode mode : VALUES)
		{
			MAP.put(mode.id, mode);
		}
	}

	public static NBTMatchingMode byName(String n)
	{
		NBTMatchingMode m = MAP.get(n);
		return m == null ? MATCH : m;
	}

	public final String id;

	NBTMatchingMode(String i)
	{
		id = i;
	}
}