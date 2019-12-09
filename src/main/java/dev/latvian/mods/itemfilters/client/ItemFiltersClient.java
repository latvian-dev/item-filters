package dev.latvian.mods.itemfilters.client;

import dev.latvian.mods.itemfilters.ItemFiltersCommon;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.gui.InventoryFilterContainer;
import dev.latvian.mods.itemfilters.gui.InventoryFilterScreen;
import dev.latvian.mods.itemfilters.gui.StringValueFilterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author LatvianModder
 */
public class ItemFiltersClient extends ItemFiltersCommon
{
	@Override
	public void setup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(InventoryFilterContainer.TYPE, InventoryFilterScreen::new);
	}

	@Override
	public void openStringValueFilterScreen(IStringValueFilter filter, ItemStack stack, Hand hand)
	{
		Minecraft.getInstance().displayGuiScreen(new StringValueFilterScreen(filter, stack, hand));
	}
}