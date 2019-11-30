package com.latmod.mods.itemfilters.filter;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.api.IItemFilter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class BaseFilterItem extends Item implements IItemFilter
{
	public BaseFilterItem()
	{
		super(new Properties().group(ItemFilters.instance.group).maxStackSize(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(new TranslationTextComponent("item.itemfilters.filter").applyTextStyle(TextFormatting.ITALIC).applyTextStyle(TextFormatting.GRAY));

		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent(I18n.format(getTranslationKey() + ".description")).applyTextStyle(TextFormatting.ITALIC).applyTextStyle(TextFormatting.DARK_GRAY));
		}
	}
}