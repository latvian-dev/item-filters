package dev.latvian.mods.itemfilters.item;


import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class DamageFilterItem extends StringValueFilterItem
{
	public static class DamageCheck
	{
		public int mode;
		public int damage;
		public boolean percent;
	}

	public static class DamageData extends StringValueData<DamageCheck>
	{
		public DamageData(ItemStack is)
		{
			super(is);
		}

		@Nullable
		@Override
		protected DamageCheck fromString(String s)
		{
			s = s.replaceAll("\\s", "");
			try
			{
				DamageCheck check = new DamageCheck();

				if (s.endsWith("%"))
				{
					check.percent = true;
					s = s.substring(0, s.length() - 1);
				}

				if (s.startsWith(">="))
				{
					check.mode = 1;
					s = s.substring(2);
				}
				else if (s.startsWith("<="))
				{
					check.mode = 2;
					s = s.substring(2);
				}
				else if (s.startsWith(">"))
				{
					check.mode = 3;
					s = s.substring(1);
				}
				else if (s.startsWith("<"))
				{
					check.mode = 4;
					s = s.substring(1);
				}

				check.damage = Integer.parseInt(s);
				return check;
			}
			catch (Exception ex)
			{
				return null;
			}
		}

		@Override
		protected String toString(DamageCheck value)
		{
			if (value == null)
			{
				return "";
			}

			StringBuilder builder = new StringBuilder();

			switch (value.mode)
			{
				case 1:
					builder.append(">=");
					break;
				case 2:
					builder.append("<=");
					break;
				case 3:
					builder.append(">");
					break;
				case 4:
					builder.append("<");
					break;
			}

			builder.append(value.damage);

			if (value.percent)
			{
				builder.append('%');
			}

			return builder.toString();
		}
	}

	@Override
	public StringValueData createData(ItemStack stack)
	{
		return new DamageData(stack);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return false;
		}

		DamageData data = getStringValueData(filter);

		if (data.getValue() == null)
		{
			return false;
		}

		int d1 = stack.getDamageValue();
		int d2 = data.getValue().percent ? (int) (stack.getMaxDamage() * data.getValue().damage / 100D) : data.getValue().damage;

		switch (data.getValue().mode)
		{
			case 1:
				return d1 >= d2;
			case 2:
				return d1 <= d2;
			case 3:
				return d1 > d2;
			case 4:
				return d1 < d2;
			default:
				return d1 == d2;
		}
	}

	@Override
	public void getDisplayItemStacks(ItemStack filter, List<ItemStack> list)
	{
	}
}