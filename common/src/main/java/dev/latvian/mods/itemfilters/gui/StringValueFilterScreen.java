package dev.latvian.mods.itemfilters.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import dev.latvian.mods.itemfilters.item.StringValueFilterItem;
import dev.latvian.mods.itemfilters.net.MessageUpdateFilterItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private int selectedVariant = 0;

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
		int w = (width * 3) / 4;
		nameField = new EditBox(font, i - w / 2, j - 6, w, 12, TextComponent.EMPTY);
		nameField.setTextColor(-1);
		nameField.setTextColorUneditable(-1);
		nameField.setResponder(this::updateVariants);
		nameField.setMaxLength(4096);
		nameField.setValue(filter.getValue(stack));
		nameField.setFocus(true);

		addRenderableWidget(nameField);
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
			selectedVariant = 0;
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ENTER) {
			String text;
			if (!variants.isEmpty() && !visibleVariants.isEmpty() && selectedVariant >= 0 && selectedVariant < visibleVariants.size()) {
				text = visibleVariants.get(selectedVariant).id;
			} else {
				text = nameField.getValue();
			}

			if (variants.isEmpty() || text.isEmpty() || variants.containsKey(text)) {
				ItemStack newStack = stack.copy();
				filter.setValue(newStack, text);
				if (newStack.hasTag() || text.isEmpty()) {
					minecraft.setScreen(null);

					StringValueFilterVariant variant = variants.get(text);
					minecraft.getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, new TextComponent("Value changed!"), text.isEmpty() ? null : variant == null ? new TextComponent(text) : variant.title.copy()));

					minecraft.player.setItemInHand(hand, newStack);
					new MessageUpdateFilterItem(hand, newStack).send();
				} else {
					minecraft.getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, new TextComponent("Invalid string!"), null));
				}
			} else {
				minecraft.getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, new TextComponent("Invalid string!"), null));
			}

			return true;
		} else if (keyCode == GLFW.GLFW_KEY_TAB) {
			adjustSelected(!Screen.hasShiftDown());
			return true;
		} else if (keyCode == GLFW.GLFW_KEY_DOWN) {
			adjustSelected(true);
			return true;
		} else if (keyCode == GLFW.GLFW_KEY_UP) {
			adjustSelected(false);
			return true;
		} else if (nameField.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	private void adjustSelected(boolean forward) {
		selectedVariant += forward ? 1 : -1;
		if (selectedVariant < 0) {
			selectedVariant = visibleVariants.size() - 1;
		} else if (selectedVariant >= visibleVariants.size()) {
			selectedVariant = 0;
		}
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
		} else {
			nameField.mouseClicked(mouseX, mouseY, mouseButton);
			nameField.setFocus(true);
		}
		return true;
	}

	@Override
	public boolean mouseScrolled(double d, double e, double delta) {
		adjustSelected(delta < 0);
		return true;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		RenderSystem.disableBlend();

		if (!variants.isEmpty()) {
			int drawY = 4 + font.lineHeight;
			int nLines = (height - drawY) / font.lineHeight;

			drawString(matrixStack, font, new TranslatableComponent("itemfilters.variants", visibleVariants.size()).withStyle(ChatFormatting.AQUA), 4, 4, -1);

			List<FormattedCharSequence> lines = ComponentRenderUtils.wrapComponents(new TranslatableComponent("itemfilters.help_text.variants").withStyle(ChatFormatting.DARK_AQUA), width / 2, font);
			for (int i = 0; i < lines.size(); i++) {
				FormattedCharSequence line = lines.get(i);
				drawString(matrixStack, font, line, width - font.width(line) - 4, 4 + i * font.lineHeight, -1);
			}

			int first = visibleVariants.size() < nLines ? 0 : Math.max(0, selectedVariant - (nLines / 2));
			for (int i = first; i < visibleVariants.size() && drawY < (height - font.lineHeight); i++) {
				StringValueFilterVariant variant = visibleVariants.get(i);
				drawString(matrixStack, font, variant.title.getString(), variant.icon.isEmpty() ? 4 : 14, drawY, i == selectedVariant ? 0xFFFFFF00 : -1);

				if (!variant.icon.isEmpty()) {
					PoseStack modelViewStack = RenderSystem.getModelViewStack();
					modelViewStack.pushPose();
					modelViewStack.translate(4, drawY, 0);
					modelViewStack.scale(0.5F, 0.5F, 1F);
					RenderSystem.applyModelViewMatrix();
					itemRenderer.blitOffset = 100F;
					RenderSystem.enableDepthTest();
					Lighting.setupFor3DItems();
					itemRenderer.renderAndDecorateItem(variant.icon, 0, 0);
					itemRenderer.renderGuiItemDecorations(font, variant.icon, 0, 0, "");
					itemRenderer.blitOffset = 0F;
					modelViewStack.popPose();
					RenderSystem.applyModelViewMatrix();
				}
				drawY += font.lineHeight;
			}
			nameField.x = width / 2;
			nameField.setWidth(width / 3);

			drawString(matrixStack, font, new TranslatableComponent("itemfilters.help_text.filter"), nameField.x, nameField.y - font.lineHeight - 2, 0xFFFFFFFF);
		} else {
			int w = (width * 3) / 4;
			nameField.x = (width - w) / 2;
			nameField.setWidth(w);

			if (stack.getItem() instanceof StringValueFilterItem filterItem) {
				List<FormattedCharSequence> lines = ComponentRenderUtils.wrapComponents(new TranslatableComponent(filterItem.getHelpKey()), nameField.getWidth(), font);
				int textY = nameField.y - 3 - font.lineHeight * lines.size();
				int color = 0xFFFFFFFF;
				for (var line : lines) {
					drawString(matrixStack, font, line, nameField.x, textY, color);
					color = 0xFF999999;
					textY += font.lineHeight;
				}
			}
		}
	}
}