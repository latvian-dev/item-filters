package dev.latvian.mods.itemfilters.client;

import dev.latvian.mods.itemfilters.ItemFiltersCommon;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.gui.GuiEditStringValueFilter;
import dev.latvian.mods.itemfilters.net.ItemFiltersNetHandler;
import dev.latvian.mods.itemfilters.net.MessageUpdateFilterItem;
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