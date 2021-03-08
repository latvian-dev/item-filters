package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.api.IItemFilter;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class BaseFilterItem extends Item implements IItemFilter {
	public BaseFilterItem() {
		super(new Item.Properties().tab(ItemFilters.creativeTab).stacksTo(64));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public final void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);

		if (Screen.hasShiftDown()) {
			tooltip.add(new TranslatableComponent(I18n.get(getDescriptionId() + ".description")).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
		}

		addInfo(stack, new FilterInfoImpl(tooltip), Screen.hasShiftDown());
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean bl) {
		if (entity instanceof ServerPlayer && ((ServerPlayer) entity).getOffhandItem() == stack) {
			boolean m = ItemFiltersAPI.filter(stack, ((ServerPlayer) entity).getMainHandItem());
			((ServerPlayer) entity).sendMessage(new TextComponent("Filter matches: ").append(new TextComponent(m ? "Yes" : "No").withStyle(m ? ChatFormatting.GREEN : ChatFormatting.RED)), ChatType.GAME_INFO, Util.NIL_UUID);
		}

		super.inventoryTick(stack, level, entity, i, bl);
	}
}