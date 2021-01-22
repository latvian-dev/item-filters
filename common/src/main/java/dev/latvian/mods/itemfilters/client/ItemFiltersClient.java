package dev.latvian.mods.itemfilters.client;

import dev.latvian.mods.itemfilters.ItemFiltersCommon;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.gui.InventoryFilterContainer;
import dev.latvian.mods.itemfilters.gui.InventoryFilterScreen;
import dev.latvian.mods.itemfilters.gui.StringValueFilterScreen;
import me.shedaniel.architectury.registry.MenuRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemFiltersClient extends ItemFiltersCommon
{
	@Override
	public void setup()
	{
		MenuRegistry.registerScreenFactory(InventoryFilterContainer.TYPE, InventoryFilterScreen::new);
	}

	@Override
	public void openStringValueFilterScreen(IStringValueFilter filter, ItemStack stack, InteractionHand hand)
	{
		Minecraft.getInstance().setScreen(new StringValueFilterScreen(filter, stack, hand));
	}
}