package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.api.IItemFilter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class BaseFilterItem extends Item implements IItemFilter
{
	public BaseFilterItem()
	{
		super(new Item.Properties().tab(ItemFilters.instance.group).stacksTo(64));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public final void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, tooltip, flag);

		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent(I18n.get(getDescriptionId() + ".description")).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
		}

		addInfo(stack, new FilterInfoImpl(tooltip), Screen.hasShiftDown());
	}
}