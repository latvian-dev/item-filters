package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.util.NBTUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ItemStackSerializer
{
	public static ItemStack parseItemThrowingException(String input) throws Exception
	{
		input = input.trim();
		if (input.isEmpty() || input.equals("-") || input.equals("minecraft:air"))
		{
			return ItemStack.EMPTY;
		}
		else if (input.startsWith("{"))
		{
			CompoundNBT nbt = JsonToNBT.getTagFromJson(input);

			if (nbt.getByte("Count") <= 0)
			{
				nbt.putByte("Count", (byte) 1);
			}

			return ItemStack.read(nbt);
		}

		String[] s1 = input.split(" ", 3);
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s1[0]));

		if (item == null)
		{
			throw new NullPointerException("Unknown item: " + s1[0]);
		}
		else if (item == Items.AIR)
		{
			return ItemStack.EMPTY;
		}

		int stackSize = 1;

		if (s1.length >= 2)
		{
			stackSize = MathHelper.getInt(s1[1], 1);
		}

		ItemStack itemstack = new ItemStack(item, stackSize);

		if (s1.length >= 3)
		{
			itemstack.setTag(JsonToNBT.getTagFromJson(s1[3]));
		}

		return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
	}

	public static ItemStack parseItem(String input)
	{
		try
		{
			return parseItemThrowingException(input);
		}
		catch (Exception ex)
		{
			return ItemStack.EMPTY;
		}
	}

	public static String toString(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return "minecraft:air";
		}

		CompoundNBT nbt = stack.serializeNBT();

		if (nbt.contains("ForgeCaps"))
		{
			return nbt.toString();
		}

		StringBuilder builder = new StringBuilder(String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())));

		int count = stack.getCount();
		CompoundNBT tag = stack.getTag();

		if (count > 1 || tag != null)
		{
			builder.append(' ');
			builder.append(count);
		}

		if (tag != null)
		{
			builder.append(' ');
			builder.append(tag);
		}

		return builder.toString();
	}

	public static INBT write(ItemStack stack, boolean forceCompound)
	{
		if (stack.isEmpty())
		{
			return forceCompound ? new CompoundNBT() : new StringNBT("");
		}

		CompoundNBT nbt = stack.serializeNBT();

		if (!nbt.contains("ForgeCaps") && !nbt.contains("tag"))
		{
			if (!forceCompound)
			{
				return new StringNBT(toString(stack));
			}

			CompoundNBT nbt1 = new CompoundNBT();
			nbt1.putString("item", toString(stack));
			return nbt1;
		}

		if (nbt.getByte("Count") == 1)
		{
			nbt.remove("Count");
		}

		if (nbt.getShort("Damage") == 0)
		{
			nbt.remove("Damage");
		}

		return nbt;
	}

	public static ItemStack read(@Nullable INBT inbt)
	{
		if (NBTUtil.isNullOrEmpty(inbt))
		{
			return ItemStack.EMPTY;
		}
		else if (inbt instanceof StringNBT)
		{
			return parseItem(((StringNBT) inbt).getString());
		}
		else if (!(inbt instanceof CompoundNBT))
		{
			return ItemStack.EMPTY;
		}

		CompoundNBT nbt = (CompoundNBT) inbt;

		if (nbt.contains("item", Constants.NBT.TAG_STRING))
		{
			return parseItem(nbt.getString("item"));
		}

		if (!nbt.contains("Count"))
		{
			nbt.putByte("Count", (byte) 1);
		}

		ItemStack stack = ItemStack.read(nbt);
		return stack.isEmpty() ? ItemStack.EMPTY : stack;
	}
}