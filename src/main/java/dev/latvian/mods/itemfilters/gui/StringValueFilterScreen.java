package dev.latvian.mods.itemfilters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import dev.latvian.mods.itemfilters.net.MessageUpdateFilterItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class StringValueFilterScreen extends Screen
{
	public final IStringValueFilter filter;
	public final ItemStack stack;
	public final Hand hand;
	private final Map<String, StringValueFilterVariant> variants;
	private final ArrayList<StringValueFilterVariant> visibleVariants;
	private TextFieldWidget nameField;
	private int selectedVariant = -1;

	public StringValueFilterScreen(IStringValueFilter f, ItemStack is, Hand h)
	{
		super(is.getDisplayName());
		filter = f;
		stack = is;
		hand = h;
		variants = new HashMap<>();

		for (StringValueFilterVariant variant : filter.getValueVariants(stack))
		{
			variants.put(variant.id, variant);
		}

		visibleVariants = new ArrayList<>(variants.values());
		visibleVariants.sort(null);
	}

	@Override
	public void init()
	{
		super.init();
		minecraft.keyboardListener.enableRepeatEvents(true);
		int i = width / 2;
		int j = height / 2;
		nameField = new TextFieldWidget(font, i - 52, j - 6, 104, 12, "");
		nameField.setTextColor(-1);
		nameField.setDisabledTextColour(-1);
		nameField.setEnableBackgroundDrawing(false);
		nameField.setResponder(this::updateVariants);
		nameField.setText(filter.getValue(stack));
		nameField.setFocused2(true);
	}

	@Override
	public void removed()
	{
		super.removed();
		minecraft.keyboardListener.enableRepeatEvents(true);
	}

	private void updateVariants(String txt)
	{
		if (!variants.isEmpty())
		{
			visibleVariants.clear();

			txt = nameField.getText().toLowerCase();

			if (txt.isEmpty())
			{
				visibleVariants.addAll(variants.values());
			}
			else
			{
				for (StringValueFilterVariant variant : variants.values())
				{
					if (variant.id.toLowerCase().contains(txt) || variant.title.getString().toLowerCase().contains(txt))
					{
						visibleVariants.add(variant);
					}
				}
			}

			visibleVariants.sort(null);
			selectedVariant = -1;
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (keyCode == GLFW.GLFW_KEY_ENTER)
		{
			String text = visibleVariants.size() == 1 ? visibleVariants.get(0).id : selectedVariant == -1 ? nameField.getText() : visibleVariants.get(selectedVariant).id;

			if (variants.isEmpty() || text.isEmpty() || variants.containsKey(text))
			{
				filter.setValue(stack, text);

				minecraft.displayGuiScreen(null);

				if (minecraft.currentScreen == null)
				{
					minecraft.setGameFocused(true);
				}

				StringValueFilterVariant variant = variants.get(text);
				minecraft.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new StringTextComponent("Value changed!"), text.isEmpty() ? null : variant == null ? new StringTextComponent(text) : variant.title.deepCopy()));

				minecraft.player.setHeldItem(hand, stack);
				new MessageUpdateFilterItem(hand, stack).send();
			}
			else
			{
				minecraft.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new StringTextComponent("Invalid string!"), null));
			}

			return true;
		}
		else if (keyCode == GLFW.GLFW_KEY_TAB)
		{
			selectedVariant++;

			if (selectedVariant == visibleVariants.size() || 14 + selectedVariant * 10 >= height)
			{
				selectedVariant = 0;
			}

			return true;
		}
		else if (nameField.keyPressed(keyCode, scanCode, modifiers))
		{
			return true;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode)
	{
		if (nameField.charTyped(typedChar, keyCode))
		{
			return true;
		}

		return super.charTyped(typedChar, keyCode);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if (super.mouseClicked(mouseX, mouseY, mouseButton))
		{
			return true;
		}

		if (mouseButton == 1)
		{
			nameField.setText("");
			return true;
		}
		else
		{
			nameField.mouseClicked(mouseX, mouseY, mouseButton);
			nameField.setFocused2(true);
			return true;
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		RenderSystem.disableLighting();
		RenderSystem.disableBlend();

		if (!variants.isEmpty())
		{
			drawString(font, "Variants [" + visibleVariants.size() + "] [Press Tab]", 4, 4, -1);

			for (int i = 0; i < visibleVariants.size(); i++)
			{
				StringValueFilterVariant variant = visibleVariants.get(i);
				drawString(font, variant.title.getFormattedText(), variant.icon.isEmpty() ? 4 : 14, 14 + i * 10, i == selectedVariant ? 0xFFFFFF00 : -1);

				if (!variant.icon.isEmpty())
				{
					RenderSystem.pushMatrix();
					RenderSystem.translated(4, 14 + i * 10, 0);
					RenderSystem.scaled(0.5F, 0.5F, 1F);
					itemRenderer.zLevel = 100F;
					RenderSystem.enableDepthTest();
					RenderHelper.enableStandardItemLighting();
					itemRenderer.renderItemAndEffectIntoGUI(minecraft.player, variant.icon, 0, 0);
					itemRenderer.renderItemOverlayIntoGUI(font, variant.icon, 0, 0, "");
					itemRenderer.zLevel = 0F;
					RenderSystem.popMatrix();
				}

				if (14 + i * 10 >= height)
				{
					break;
				}
			}
		}

		nameField.render(mouseX, mouseY, partialTicks);
	}
}