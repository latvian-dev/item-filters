package dev.latvian.mods.itemfilters.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public abstract class StringValueData<T> implements ICapabilityProvider
{
	@CapabilityInject(ItemInventory.class)
	public static Capability<StringValueData> CAPABILITY;

	public final LazyOptional<StringValueData> provider = LazyOptional.of(() -> this);

	@Override
	public <E> LazyOptional<E> getCapability(Capability<E> cap, @Nullable Direction side)
	{
		return cap == CAPABILITY ? provider.cast() : LazyOptional.empty();
	}

	public final ItemStack filter;
	private T value;
	private boolean load;

	public StringValueData(ItemStack is)
	{
		filter = is;
		value = null;
		load = true;
	}

	@Nullable
	protected abstract T fromString(String s);

	protected abstract String toString(T value);

	public final String toString()
	{
		return filter.getItem().getRegistryName() + ":" + toString(getValue());
	}

	@Nullable
	public T getValue()
	{
		if (load)
		{
			load = false;
			value = null;

			if (filter.hasTag())
			{
				value = fromString(filter.getTag().getString("value"));
			}
		}

		return value;
	}

	public void setValue(@Nullable T v)
	{
		value = v;
		load = false;
		String s = value == null ? "" : toString(value);

		if (s.isEmpty())
		{
			filter.removeChildTag("value");
		}
		else
		{
			filter.setTagInfo("value", new StringNBT(s));
		}
	}

	public void setValueFromString(String v)
	{
		setValue(fromString(v));
	}
}