package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.api.IItemFilter;
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
		super(new Properties().group(ItemFilters.instance.group).maxStackSize(64));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public final void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);

		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent(I18n.format(getTranslationKey() + ".description")).mergeStyle(TextFormatting.ITALIC, TextFormatting.DARK_GRAY));
		}

		addInfo(stack, new FilterInfoImpl(tooltip), Screen.hasShiftDown());
	}
}