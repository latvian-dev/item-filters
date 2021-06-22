package dev.latvian.mods.itemfilters.item;

import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * @author LatvianModder
 */
public class RegExFilterItem extends StringValueFilterItem {
	public static class RegExData extends StringValueData<Pattern> {
		public RegExData(ItemStack is) {
			super(is);
		}

		@Nullable
		@Override
		protected Pattern fromString(String s) {
			return s.isEmpty() ? null : Pattern.compile(s);
		}

		@Override
		protected String toString(Pattern value) {
			return value == null ? "" : value.toString();
		}
	}

	@Override
	public StringValueData createData(ItemStack stack) {
		return new RegExData(stack);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		RegExData data = getStringValueData(filter);

		if (data.getValue() != null) {
			return data.getValue().matcher(Registry.ITEM.getKey(stack.getItem()).toString()).find();
		}

		return false;
	}
}