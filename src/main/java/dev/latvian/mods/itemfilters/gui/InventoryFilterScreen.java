package dev.latvian.mods.itemfilters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.item.InventoryFilterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public class InventoryFilterScreen extends ContainerScreen<InventoryFilterContainer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ItemFilters.MOD_ID, "textures/gui/filter.png");

	public InventoryFilterScreen(InventoryFilterContainer container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		xSize = 176;
		ySize = 166;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		minecraft.getTextureManager().bindTexture(TEXTURE);
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);

		for (InventoryFilterItem.FilterSlot slot : container.filterSlots)
		{
			blit(guiLeft + slot.x - 1, guiTop + slot.y - 1, 177, 0, 18, 18);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		font.drawString(getTitle().getFormattedText(), 8, 6, 4210752);
		font.drawString(Minecraft.getInstance().player.inventory.getDisplayName().getFormattedText(), 8, 72, 4210752);
	}
}