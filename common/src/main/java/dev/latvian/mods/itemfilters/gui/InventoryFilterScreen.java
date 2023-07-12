package dev.latvian.mods.itemfilters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.item.InventoryFilterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author LatvianModder
 */
public class InventoryFilterScreen extends AbstractContainerScreen<InventoryFilterMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(ItemFilters.MOD_ID, "textures/gui/filter.png");

	public InventoryFilterScreen(InventoryFilterMenu container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		imageWidth = 176;
		imageHeight = 166;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTicks);
		renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		for (InventoryFilterItem.FilterSlot slot : menu.filterSlots) {
			graphics.blit(TEXTURE, leftPos + slot.x - 1, topPos + slot.y - 1, 177, 0, 18, 18);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		graphics.drawString(font, getTitle(), 8, 6, 0x404040, false);
		graphics.drawString(font, Minecraft.getInstance().player.getInventory().getDisplayName(), 8, 72, 0x404040);
	}
}