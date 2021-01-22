package dev.latvian.mods.itemfilters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.item.InventoryFilterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author LatvianModder
 */
public class InventoryFilterScreen extends AbstractContainerScreen<InventoryFilterContainer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ItemFilters.MOD_ID, "textures/gui/filter.png");

	public InventoryFilterScreen(InventoryFilterContainer container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		imageWidth = 176;
		imageHeight = 166;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		minecraft.getTextureManager().bind(TEXTURE);
		blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		for (InventoryFilterItem.FilterSlot slot : menu.filterSlots)
		{
			blit(matrixStack, leftPos + slot.x - 1, topPos + slot.y - 1, 177, 0, 18, 18);
		}
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		font.draw(matrixStack, getTitle().getString(), 8, 6, 4210752);
		font.draw(matrixStack, Minecraft.getInstance().player.inventory.getDisplayName().getString(), 8, 72, 4210752);
	}
}