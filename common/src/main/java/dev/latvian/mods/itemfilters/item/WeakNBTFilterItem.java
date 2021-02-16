package dev.latvian.mods.itemfilters.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public class WeakNBTFilterItem extends StrongNBTFilterItem
{
	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return false;
		}

		NBTData data = getStringValueData(filter);
		CompoundTag tag1 = data.getValue();
		CompoundTag tag2 = stack.getTag();

		if (tag1 == null || tag1.isEmpty())
		{
			return true;
		}
		else if (tag2 == null || tag2.isEmpty())
		{
			return false;
		}

		for (String s : tag1.getAllKeys())
		{
			if (!Objects.equals(tag2.get(s), tag1.get(s)))
			{
				return false;
			}
		}

		return true;
	}
}