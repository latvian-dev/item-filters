package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.api.IStringValueFilter;
import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class GuiEditStringValueFilter extends Screen
{
	public final IStringValueFilter filter;
	public final Runnable save;
	private final Map<String, StringValueFilterVariant> variants;
	private final ArrayList<StringValueFilterVariant> visibleVariants;
	private TextFieldWidget nameField;
	private int selectedVariant = -1;

	public GuiEditStringValueFilter(IStringValueFilter f, Runnable s)
	{
		super(new StringTextComponent(""));
		filter = f;
		save = s;
		variants = new HashMap<>();

		for (StringValueFilterVariant variant : filter.getValueVariants())
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
		//		Keyboard.enableRepeatEvents(true);
		int i = width / 2;
		int j = height / 2;
		nameField = new TextFieldWidget(font, i - 52, j - 6, 104, 12, "");
		nameField.setTextColor(-1);
		nameField.setDisabledTextColour(-1);
		nameField.setEnableBackgroundDrawing(false);
		nameField.setText(filter.getValue());
		nameField.setFocused2(true);
		updateVariants();
	}


	private void updateVariants()
	{
		if (!variants.isEmpty())
		{
			visibleVariants.clear();

			String txt = nameField.getText().toLowerCase();

			if (txt.isEmpty())
			{
				visibleVariants.addAll(variants.values());
			}
			else
			{
				for (StringValueFilterVariant variant : variants.values())
				{
					if (variant.id.toLowerCase().contains(txt) || variant.title.toLowerCase().contains(txt))
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
	public boolean charTyped(char typedChar, int keyCode)
	{
		if (keyCode == GLFW.GLFW_KEY_ENTER)
		{
			String text = visibleVariants.size() == 1 ? visibleVariants.get(0).id : selectedVariant == -1 ? nameField.getText() : visibleVariants.get(selectedVariant).id;

			if (variants.isEmpty() || text.isEmpty() || variants.containsKey(text))
			{
				filter.setValue(text);
				save.run();

				minecraft.displayGuiScreen(null);

				if (minecraft.currentScreen == null)
				{
					minecraft.setGameFocused(true);
				}

				StringValueFilterVariant variant = variants.get(text);
				minecraft.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new StringTextComponent("Value changed!"), text.isEmpty() ? null : new StringTextComponent(variant == null ? text : variant.getTitle())));
			}
			else
			{
				minecraft.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new StringTextComponent("Invalid string!"), null));
			}
		}
		else if (keyCode == GLFW.GLFW_KEY_TAB)
		{
			selectedVariant++;

			if (selectedVariant == visibleVariants.size() || 14 + selectedVariant * 10 >= height)
			{
				selectedVariant = 0;
			}
		}
		else if (nameField.charTyped(typedChar, keyCode))
		{
			updateVariants();
		}
		else
		{
			return super.charTyped(typedChar, keyCode);
		}
		return true;
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
			updateVariants();
			return true;
		}
		else
		{
			return nameField.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();

		if (!variants.isEmpty())
		{
			drawString(font, "Variants [" + visibleVariants.size() + "]:", 4, 4, -1);

			for (int i = 0; i < visibleVariants.size(); i++)
			{
				StringValueFilterVariant variant = visibleVariants.get(i);
				drawString(font, variant.getTitle(), variant.icon.isEmpty() ? 4 : 14, 14 + i * 10, i == selectedVariant ? 0xFFFFFF00 : -1);

				if (!variant.icon.isEmpty())
				{
					GlStateManager.pushMatrix();
					GlStateManager.translated(4, 14 + i * 10, 0);
					GlStateManager.scaled(0.5F, 0.5F, 1F);
					//					zLevel = 100F;
					itemRenderer.zLevel = 100F;
					GlStateManager.enableDepthTest();
					RenderHelper.enableGUIStandardItemLighting();
					itemRenderer.renderItemAndEffectIntoGUI(minecraft.player, variant.icon, 0, 0);
					itemRenderer.renderItemOverlayIntoGUI(font, variant.icon, 0, 0, "");
					itemRenderer.zLevel = 0F;
					GlStateManager.popMatrix();
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