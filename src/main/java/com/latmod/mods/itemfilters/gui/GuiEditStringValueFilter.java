package com.latmod.mods.itemfilters.gui;

import com.latmod.mods.itemfilters.api.IStringValueFilter;
import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import com.latmod.mods.itemfilters.net.MessageUpdateItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author LatvianModder
 */
public class GuiEditStringValueFilter extends GuiScreen
{
	public final IStringValueFilter filter;
	public final EnumHand hand;
	private GuiTextField nameField;
	private final HashSet<StringValueFilterVariant> variants;

	public GuiEditStringValueFilter(IStringValueFilter f, EnumHand h)
	{
		filter = f;
		hand = h;
		variants = new HashSet<>(filter.getValueVariants());
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

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (nameField.textboxKeyTyped(typedChar, keyCode))
		{
		}
		else if (keyCode == Keyboard.KEY_RETURN)
		{
			String text = nameField.getText();
			if (variants.isEmpty() || text.isEmpty() || variants.contains(new StringValueFilterVariant(text)))
			{
				filter.setValue(text);
				new MessageUpdateItem(hand, filter).send();

				mc.displayGuiScreen(null);

				if (mc.currentScreen == null)
				{
					mc.setIngameFocus();
				}

				mc.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new TextComponentString("Value changed!"), text.isEmpty() ? null : new TextComponentString(text)));
			}
			else
			{
				mc.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new TextComponentString("Invalid string!"), null));
			}
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
		nameField.drawTextBox();
	}
}