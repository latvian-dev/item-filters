package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.api.IRegisteredItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.filters.LogicFilter;
import com.latmod.mods.itemfilters.item.ItemFilter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class GuiSelectFilter extends Screen
{
	private class ButtonFilter extends Button
	{
		private final Map.Entry<String, Supplier<IRegisteredItemFilter>> entry;

		public ButtonFilter(int widthIn, int heightIn, int p_i51141_3_, int p_i51141_4_, String text, IPressable onPress, Map.Entry<String, Supplier<IRegisteredItemFilter>> entry)
		{
			super(widthIn, heightIn, p_i51141_3_, p_i51141_4_, text, onPress);
			this.entry = entry;
		}
	}

	public final ItemFilter.ItemFilterData data;
	public final Runnable save;

	public GuiSelectFilter(ITextComponent titleIn, ItemFilter.ItemFilterData data, Runnable save)
	{
		super(titleIn);
		this.data = data;
		this.save = save;
	}

	@Override
	public void init()
	{
		super.init();

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
				int w = font.getStringWidth(n) + 12;
				addButton(new ButtonFilter((width - w) / 2, offY + i * 24 - 2, w, 20, n, p_onPress_1_ -> {
					minecraft.player.closeScreen();
					data.filter = ((ButtonFilter) p_onPress_1_).entry.getValue().get();
					save.run();
				}, entry));
				i++;
			}
		}
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		renderBackground();
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
}