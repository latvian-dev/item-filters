package dev.latvian.mods.itemfilters.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author LatvianModder
 */
public class MaxCountFilterItem extends StringValueFilterItem {
	public static class MaxCountCheck {
		public int mode;
		public int maxCount;
	}

	public static class MaxCountData extends StringValueData<MaxCountCheck> {
		public MaxCountData(ItemStack is) {
			super(is);
		}

		@Nullable
		@Override
		protected MaxCountCheck fromString(String s) {
			s = s.replaceAll("\\s", "");
			try {
				MaxCountCheck check = new MaxCountCheck();

				if (s.startsWith(">=")) {
					check.mode = 1;
					s = s.substring(2);
				} else if (s.startsWith("<=")) {
					check.mode = 2;
					s = s.substring(2);
				} else if (s.startsWith(">")) {
					check.mode = 3;
					s = s.substring(1);
				} else if (s.startsWith("<")) {
					check.mode = 4;
					s = s.substring(1);
				}

				check.maxCount = Integer.parseInt(s);
				return check;
			} catch (Exception ex) {
				return null;
			}
		}

		@Override
		protected String toString(MaxCountCheck value) {
			if (value == null) {
				return "";
			}

			StringBuilder builder = new StringBuilder();

			switch (value.mode) {
				case 1 -> builder.append(">=");
				case 2 -> builder.append("<=");
				case 3 -> builder.append(">");
				case 4 -> builder.append("<");
			}

			builder.append(value.maxCount);
			return builder.toString();
		}
	}

	@Override
	public StringValueData createData(ItemStack stack) {
		return new MaxCountData(stack);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack) {
		MaxCountData data = getStringValueData(filter);

		if (data.getValue() == null) {
			return false;
		}

		int d1 = stack.getMaxStackSize();
		int d2 = data.getValue().maxCount;

		return switch (data.getValue().mode) {
			case 1 -> d1 >= d2;
			case 2 -> d1 <= d2;
			case 3 -> d1 > d2;
			case 4 -> d1 < d2;
			default -> d1 == d2;
		};
	}
}