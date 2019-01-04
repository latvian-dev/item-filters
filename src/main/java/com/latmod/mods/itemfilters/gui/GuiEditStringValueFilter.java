package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.api.IStringValueFilter;
import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class GuiEditStringValueFilter extends GuiScreen
{
	public final IStringValueFilter filter;
	public final Runnable save;
	private GuiTextField nameField;
	private final Map<String, StringValueFilterVariant> variants;
	private final ArrayList<StringValueFilterVariant> visibleVariants;
	private int selectedVariant = -1;

	public GuiEditStringValueFilter(IStringValueFilter f, Runnable s)
	{
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
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int i = width / 2;
		int j = height / 2;
		nameField = new GuiTextField(0, fontRenderer, i - 52, j - 6, 104, 12);
		nameField.setTextColor(-1);
		nameField.setDisabledTextColour(-1);
		nameField.setEnableBackgroundDrawing(false);
		nameField.setText(filter.getValue());
		nameField.setFocused(true);
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
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
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (keyCode == Keyboard.KEY_RETURN)
		{
			String text = visibleVariants.size() == 1 ? visibleVariants.get(0).id : selectedVariant == -1 ? nameField.getText() : visibleVariants.get(selectedVariant).id;

			if (variants.isEmpty() || text.isEmpty() || variants.containsKey(text))
			{
				filter.setValue(text);
				save.run();

				mc.displayGuiScreen(null);

				if (mc.currentScreen == null)
				{
					mc.setIngameFocus();
				}

				StringValueFilterVariant variant = variants.get(text);
				mc.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new TextComponentString("Value changed!"), text.isEmpty() ? null : new TextComponentString(variant == null ? text : variant.getTitle())));
			}
			else
			{
				mc.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new TextComponentString("Invalid string!"), null));
			}
		}
		else if (keyCode == Keyboard.KEY_TAB)
		{
			selectedVariant++;

			if (selectedVariant == visibleVariants.size() || 14 + selectedVariant * 10 >= height)
			{
				selectedVariant = 0;
			}
		}
		else if (nameField.textboxKeyTyped(typedChar, keyCode))
		{
			updateVariants();
		}
		else
		{
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 1)
		{
			nameField.setText("");
			updateVariants();
		}
		else
		{
			nameField.mouseClicked(mouseX, mouseY, mouseButton);
		}

		if (!nameField.isFocused())
		{
			nameField.setFocused(true);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();

		if (!variants.isEmpty())
		{
			drawString(fontRenderer, "Variants [" + visibleVariants.size() + "]:", 4, 4, -1);

			for (int i = 0; i < visibleVariants.size(); i++)
			{
				StringValueFilterVariant variant = visibleVariants.get(i);
				drawString(fontRenderer, variant.getTitle(), variant.icon.isEmpty() ? 4 : 14, 14 + i * 10, i == selectedVariant ? 0xFFFFFF00 : -1);

				if (!variant.icon.isEmpty())
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(4, 14 + i * 10, 0);
					GlStateManager.scale(0.5F, 0.5F, 1F);
					zLevel = 100F;
					itemRender.zLevel = 100F;
					GlStateManager.enableDepth();
					RenderHelper.enableGUIStandardItemLighting();
					itemRender.renderItemAndEffectIntoGUI(mc.player, variant.icon, 0, 0);
					itemRender.renderItemOverlayIntoGUI(fontRenderer, variant.icon, 0, 0, "");
					itemRender.zLevel = 0F;
					zLevel = 0F;
					GlStateManager.popMatrix();
				}

				if (14 + i * 10 >= height)
				{
					break;
				}
			}
		}

		nameField.drawTextBox();
	}
}