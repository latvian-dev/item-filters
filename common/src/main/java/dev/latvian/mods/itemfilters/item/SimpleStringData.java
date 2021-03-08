package dev.latvian.mods.itemfilters.item;

import net.minecraft.world.item.ItemStack;

/**
 * @author LatvianModder
 */
public class SimpleStringData extends StringValueData<String> {
	public SimpleStringData(ItemStack is) {
		super(is);
	}

	@Override
	public String fromString(String s) {
		return s;
	}

	@Override
	public String toString(String value) {
		return value == null ? "" : value;
	}
}
