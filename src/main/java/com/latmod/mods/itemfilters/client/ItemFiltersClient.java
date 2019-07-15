package com.latmod.mods.itemfilters.client;

import com.latmod.mods.itemfilters.ItemFiltersCommon;
import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.gui.GuiSelectFilter;
import com.latmod.mods.itemfilters.item.ItemFilter;
import com.latmod.mods.itemfilters.net.MessageUpdateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author LatvianModder
 */
public class ItemFiltersClient extends ItemFiltersCommon
{
	@Override
	public void openSelectionGUI(ItemFilter.ItemFilterData data, Hand hand)
	{
		Minecraft.getInstance().displayGuiScreen(new GuiSelectFilter(new StringTextComponent("Filter Select"), data, () -> new MessageUpdateItem(hand, data).send()));
	}

	@Override
	public void openGUI(IItemFilter filter, Hand hand)
	{
		filter.openEditingGUI(() -> new MessageUpdateItem(hand, filter).send());
	}
}