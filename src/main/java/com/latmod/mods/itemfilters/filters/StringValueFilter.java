package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.IStringValueFilter;
import com.latmod.mods.itemfilters.gui.GuiEditStringValueFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public abstract class StringValueFilter extends FilterBase implements IStringValueFilter, INBTSerializable<NBTTagString>
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
	@SideOnly(Side.CLIENT)
	public void openEditingGUI(Runnable save)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiEditStringValueFilter(this, save));
	}

	@Override
	public void resetData()
	{
		value = "";
	}
}