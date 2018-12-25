package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.block.EnumIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public class GuiAdvancedHopper extends GuiContainer
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ItemFilters.MOD_ID, "textures/gui/advanced_hopper.png");

	private class ButtonChangeMode extends GuiButton
	{
		public ButtonChangeMode(int id, int x, int y)
		{
			super(id, x, y, 16, 16, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			if (visible)
			{
				GlStateManager.color(1F, 1F, 1F, 1F);
				hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

				if (hovered)
				{
					drawTexturedModalRect(x, y, 176, 0, width, height);
				}

				if (container.hopper.modes[id] == EnumIO.IN)
				{
					drawTexturedModalRect(x, y, 176, id < 3 ? 34 : 17, width, height);
				}
				else if (container.hopper.modes[id] == EnumIO.OUT)
				{
					drawTexturedModalRect(x, y, 176, id < 3 ? 17 : 34, width, height);
				}

				mouseDragged(mc, mouseX, mouseY);
			}
		}
	}

	private class ButtonFilter extends GuiButton
	{
		public ButtonFilter(int id, int x, int y)
		{
			super(id, x, y, 16, 16, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			if (visible)
			{
				GlStateManager.color(1F, 1F, 1F, 1F);
				hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

				if (hovered)
				{
					//int left, int top, int right, int bottom
					drawRect(x, y, x + width, y + height, -2130706433);
				}

				if (!container.hopper.filters[id - 6].isEmpty())
				{
					zLevel = 100F;
					itemRender.zLevel = 100F;
					GlStateManager.enableDepth();
					RenderHelper.enableGUIStandardItemLighting();
					itemRender.renderItemAndEffectIntoGUI(mc.player, container.hopper.filters[id - 6], x, y);
					itemRender.renderItemOverlayIntoGUI(fontRenderer, container.hopper.filters[id - 6], x, y, "");
					itemRender.zLevel = 0F;
					zLevel = 0F;
				}

				mouseDragged(mc, mouseX, mouseY);
			}
		}
	}

	public final ContainerAdvancedHopper container;

	public GuiAdvancedHopper(ContainerAdvancedHopper c)
	{
		super(c);
		container = c;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.add(new ButtonChangeMode(0, guiLeft + 35, guiTop + 17));
		buttonList.add(new ButtonChangeMode(1, guiLeft + 35, guiTop + 35));
		buttonList.add(new ButtonChangeMode(2, guiLeft + 35, guiTop + 53));
		buttonList.add(new ButtonChangeMode(3, guiLeft + 125, guiTop + 17));
		buttonList.add(new ButtonChangeMode(4, guiLeft + 125, guiTop + 35));
		buttonList.add(new ButtonChangeMode(5, guiLeft + 125, guiTop + 53));
		buttonList.add(new ButtonFilter(6, guiLeft + 8, guiTop + 17));
		buttonList.add(new ButtonFilter(7, guiLeft + 8, guiTop + 35));
		buttonList.add(new ButtonFilter(8, guiLeft + 8, guiTop + 53));
		buttonList.add(new ButtonFilter(9, guiLeft + 152, guiTop + 17));
		buttonList.add(new ButtonFilter(10, guiLeft + 152, guiTop + 35));
		buttonList.add(new ButtonFilter(11, guiLeft + 152, guiTop + 53));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button instanceof ButtonChangeMode || button instanceof ButtonFilter)
		{
			if (container.enchantItem(container.player, button.id))
			{
				mc.playerController.sendEnchantPacket(container.windowId, button.id);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = container.hopper.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(container.player.inventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);

		for (GuiButton button : buttonList)
		{
			if (button.isMouseOver())
			{
				if (button instanceof ButtonFilter)
				{
					ItemStack stack = container.hopper.filters[button.id - 6];

					if (!stack.isEmpty())
					{
						renderToolTip(stack, mouseX - guiLeft, mouseY - guiTop);
					}
				}
				else if (button instanceof ButtonChangeMode)
				{
					String s1 = I18n.format("tile.itemfilters.advanced_hopper.mode", I18n.format("tile.itemfilters.advanced_hopper.mode." + container.hopper.modes[button.id].getName()));
					drawHoveringText(Collections.singletonList(s1), mouseX - guiLeft, mouseY - guiTop);
				}
			}
		}
	}
}