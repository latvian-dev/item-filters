package com.latmod.mods.itemfilters.client;

import com.latmod.mods.itemfilters.ItemFiltersCommon;
import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.gui.GuiSelectFilter;
import com.latmod.mods.itemfilters.item.ItemFilter;
import com.latmod.mods.itemfilters.net.MessageUpdateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;

/**
 * @author LatvianModder
 */
public class ItemFiltersClient extends ItemFiltersCommon
{
	@Override
	public void openSelectionGUI(ItemFilter.ItemFilterData data, EnumHand hand)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiSelectFilter(data, () -> new MessageUpdateItem(hand, data).send()));
	}

	@Override
	public void openGUI(IItemFilter filter, EnumHand hand)
	{
		filter.openEditingGUI(() -> new MessageUpdateItem(hand, filter).send());
	}
}