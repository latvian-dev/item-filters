package dev.latvian.mods.itemfilters.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public interface IStringValueFilter extends IItemFilter
{
	String getValue(ItemStack filter);

	void setValue(ItemStack filter, String v);

	@Environment(EnvType.CLIENT)
	default Collection<StringValueFilterVariant> getValueVariants(ItemStack stack)
	{
		return Collections.emptyList();
	}
}