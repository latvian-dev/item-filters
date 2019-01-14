package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.api.IRegisteredItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.filters.LogicFilter;
import com.latmod.mods.itemfilters.item.ItemFilter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class GuiSelectFilter extends GuiScreen
{
	private class ButtonFilter extends GuiButton
	{
		private final Map.Entry<String, Supplier<IRegisteredItemFilter>> entry;

		public ButtonFilter(int i, int x, int y, int w, int h, String s, Map.Entry<String, Supplier<IRegisteredItemFilter>> e)
		{
			super(i, x, y, w, h, s);
			entry = e;
		}
	}

	public final ItemFilter.ItemFilterData data;
	public final Runnable save;

	public GuiSelectFilter(ItemFilter.ItemFilterData d, Runnable s)
	{
		data = d;
		save = s;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int s = 0;

		for (Map.Entry<String, Supplier<IRegisteredItemFilter>> entry : ItemFiltersAPI.REGISTRY.entrySet())
		{
			if (!(entry.getValue().get() instanceof LogicFilter))
			{
				s++;
			}
		}

		int offY = (height - s * 24) / 2;
		int i = 0;

		for (Map.Entry<String, Supplier<IRegisteredItemFilter>> entry : ItemFiltersAPI.REGISTRY.entrySet())
		{
			if (!(entry.getValue().get() instanceof LogicFilter))
			{
				String n = I18n.format("filter.itemfilters." + entry.getKey() + ".name");
				int w = fontRenderer.getStringWidth(n) + 12;
				addButton(new ButtonFilter(i, (width - w) / 2, offY + i * 24 - 2, w, 20, n, entry));
				i++;
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button instanceof ButtonFilter)
		{
			mc.player.closeScreen();
			data.filter = ((ButtonFilter) button).entry.getValue().get();
			save.run();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}