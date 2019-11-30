package com.latmod.mods.itemfilters.client;

import com.latmod.mods.itemfilters.ItemFiltersCommon;
import com.latmod.mods.itemfilters.api.IStringValueFilter;
import com.latmod.mods.itemfilters.gui.GuiEditStringValueFilter;
import com.latmod.mods.itemfilters.net.ItemFiltersNetHandler;
import com.latmod.mods.itemfilters.net.MessageUpdateFilterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * @author LatvianModder
 */
public class ItemFiltersClient extends ItemFiltersCommon
{
	@Override
	public void openStringValueGUI(IStringValueFilter filter, ItemStack stack, Hand hand)
	{
		Minecraft.getInstance().displayGuiScreen(new GuiEditStringValueFilter(filter, stack, stack1 -> ItemFiltersNetHandler.NET.sendToServer(new MessageUpdateFilterItem(hand, stack1.getTag()))));
	}
}