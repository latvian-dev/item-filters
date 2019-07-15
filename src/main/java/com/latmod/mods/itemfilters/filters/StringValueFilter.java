package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.IStringValueFilter;
import com.latmod.mods.itemfilters.gui.GuiEditStringValueFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public abstract class StringValueFilter extends FilterBase implements IStringValueFilter, INBTSerializable<StringNBT>
{
	private String value = "";

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(String v)
	{
		value = v;
	}

	@Override
	public StringNBT serializeNBT()
	{
		return new StringNBT(getValue());
	}

	@Override
	public void deserializeNBT(StringNBT nbt)
	{
		setValue(nbt.getString());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void openEditingGUI(Runnable save)
	{
		Minecraft.getInstance().displayGuiScreen(new GuiEditStringValueFilter(this, save));
	}

	@Override
	public void resetData()
	{
		value = "";
	}
}