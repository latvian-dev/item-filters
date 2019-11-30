package dev.latvian.mods.itemfilters.util;

import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongArrayNBT;

import javax.annotation.Nullable;

public class NBTUtil
{
	public static boolean isNullOrEmpty(@Nullable INBT nbt)
	{
		switch (nbt == null ? 0 : nbt.getId())
		{
			case 12:
				return ((LongArrayNBT) nbt).isEmpty();
			case 11:
				return ((IntArrayNBT) nbt).isEmpty();
			case 10:
				return ((CompoundNBT) nbt).isEmpty();
			case 9:
				return ((ListNBT) nbt).isEmpty();
			case 7:
				return ((ByteArrayNBT) nbt).isEmpty();
			case 8:
				return nbt.getString().isEmpty();
			case 6:
			case 5:
			case 4:
			case 3:
			case 2:
			case 1:
				return false;
			case 0:
			default:
				return true;
		}
	}
}