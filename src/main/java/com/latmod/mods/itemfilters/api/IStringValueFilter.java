package com.latmod.mods.itemfilters.api;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public interface IStringValueFilter extends IItemFilter
{
	void setValue(String v);

	String getValue();

	@SideOnly(Side.CLIENT)
	default Collection<StringValueFilterVariant> getValueVariants()
	{
		return Collections.emptyList();
	}
}