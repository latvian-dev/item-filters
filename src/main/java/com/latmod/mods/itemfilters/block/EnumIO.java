package com.latmod.mods.itemfilters.block;

import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumIO implements IStringSerializable
{
	NONE("none"),
	IN("in"),
	OUT("out");

	public static final EnumIO[] VALUES = values();

	private final String name;

	EnumIO(String n)
	{
		name = n;
	}

	@Override
	public String getName()
	{
		return name;
	}
}