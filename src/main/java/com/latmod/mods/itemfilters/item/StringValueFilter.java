package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.api.IStringValueFilter;
import com.latmod.mods.itemfilters.gui.GuiEditStringValueFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public abstract class StringValueFilter extends BaseItemFilter implements IStringValueFilter, INBTSerializable<NBTTagString>
{
	private String value = "";

	@Override
	public void setValue(String v)
	{
		value = v;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public NBTTagString serializeNBT()
	{
		return new NBTTagString(getValue());
	}

	@Override
	public void deserializeNBT(NBTTagString nbt)
	{
		setValue(nbt.getString());
	}

	@Override
	public boolean openGUI(EntityPlayer player, EnumHand hand, ItemStack heldItem)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiEditStringValueFilter(this, hand));
		return true;
	}
}
