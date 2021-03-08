package dev.latvian.mods.itemfilters.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import dev.latvian.mods.itemfilters.net.MessageUpdateFilterItem;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class StringValueFilterScreen extends Screen {
	public final IStringValueFilter filter;
	public final ItemStack stack;
	public final InteractionHand hand;
	private final Map<String, StringValueFilterVariant> variants;
	private final ArrayList<StringValueFilterVariant> visibleVariants;
	private EditBox nameField;
	private int selectedVariant = -1;

	public StringValueFilterScreen(IStringValueFilter f, ItemStack is, InteractionHand h) {
		super(is.getDisplayName());
		filter = f;
		stack = is;
		hand = h;
		variants = new HashMap<>();

		for (StringValueFilterVariant variant : filter.getValueVariants(stack)) {
			variants.put(variant.id, variant);
		}

		visibleVariants = new ArrayList<>(variants.values());
		visibleVariants.sort(null);
	}

	@Override
	public void init() {
		super.init();
		minecraft.keyboardHandler.setSendRepeatsToGui(true);
		int i = width / 2;
		int j = height / 2;
		nameField = new EditBox(font, i - 52, j - 6, 104, 12, TextComponent.EMPTY);
		nameField.setTextColor(-1);
		nameField.setTextColorUneditable(-1);
		nameField.setBordered(false);
		nameField.setResponder(this::updateVariants);
		nameField.setValue(filter.getValue(stack));
		nameField.setFocus(true);
	}

	@Override
	public void onClose() {
		super.onClose();
		minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	private void updateVariants(String txt) {
		if (!variants.isEmpty()) {
			visibleVariants.clear();

			txt = nameField.getValue().toLowerCase();

			if (txt.isEmpty()) {
				visibleVariants.addAll(variants.values());
			} else {
				for (StringValueFilterVariant variant : variants.values()) {
					if (variant.id.toLowerCase().contains(txt) || variant.title.getString().toLowerCase().contains(txt)) {
						visibleVariants.add(variant);
					}
				}
			}

			visibleVariants.sort(null);
			selectedVariant = -1;
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ENTER) {
			String text = visibleVariants.size() == 1 ? visibleVariants.get(0).id : selectedVariant == -1 ? nameField.getValue() : visibleVariants.get(selectedVariant).id;

			if (variants.isEmpty() || text.isEmpty() || variants.containsKey(text)) {
				filter.setValue(stack, text);
				minecraft.setScreen(null);

				StringValueFilterVariant variant = variants.get(text);
				minecraft.getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, new TextComponent("Value changed!"), text.isEmpty() ? null : variant == null ? new TextComponent(text) : variant.title.copy()));

				minecraft.player.setItemInHand(hand, stack);
				new MessageUpdateFilterItem(hand, stack).send();
			} else {
				minecraft.getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, new TextComponent("Invalid string!"), null));
			}

			return true;
		} else if (keyCode == GLFW.GLFW_KEY_TAB) {
			selectedVariant++;

			if (selectedVariant == visibleVariants.size() || 14 + selectedVariant * 10 >= height) {
				selectedVariant = 0;
			}

			return true;
		} else if (nameField.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (nameField.charTyped(typedChar, keyCode)) {
			return true;
		}

		return super.charTyped(typedChar, keyCode);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (super.mouseClicked(mouseX, mouseY, mouseButton)) {
			return true;
		}

		if (mouseButton == 1) {
			nameField.setValue("");
			return true;
		} else {
			nameField.mouseClicked(mouseX, mouseY, mouseButton);
			nameField.setFocus(true);
			return true;
		}
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		RenderSystem.disableLighting();
		RenderSystem.disableBlend();

		if (!variants.isEmpty()) {
			drawString(matrixStack, font, "Variants [" + visibleVariants.size() + "] [Press Tab]", 4, 4, -1);

			for (int i = 0; i < visibleVariants.size(); i++) {
				StringValueFilterVariant variant = visibleVariants.get(i);
				drawString(matrixStack, font, variant.title.getString(), variant.icon.isEmpty() ? 4 : 14, 14 + i * 10, i == selectedVariant ? 0xFFFFFF00 : -1);

				if (!variant.icon.isEmpty()) {
					RenderSystem.pushMatrix();
					RenderSystem.translated(4, 14 + i * 10, 0);
					RenderSystem.scaled(0.5F, 0.5F, 1F);
					itemRenderer.blitOffset = 100F;
					RenderSystem.enableDepthTest();
					Lighting.setupFor3DItems();
					itemRenderer.renderAndDecorateItem(minecraft.player, variant.icon, 0, 0);
					itemRenderer.renderGuiItemDecorations(font, variant.icon, 0, 0, "");
					itemRenderer.blitOffset = 0F;
					RenderSystem.popMatrix();
				}

				if (14 + i * 10 >= height) {
					break;
				}
			}
		}

		nameField.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}