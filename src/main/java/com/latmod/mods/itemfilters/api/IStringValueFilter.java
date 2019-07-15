package com.latmod.mods.itemfilters.api;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collection;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public interface IStringValueFilter extends IItemFilter
{
	String getValue();

	void setValue(String v);

	@OnlyIn(Dist.CLIENT)
	default Collection<StringValueFilterVariant> getValueVariants()
	{
		return Collections.emptyList();
	}
}