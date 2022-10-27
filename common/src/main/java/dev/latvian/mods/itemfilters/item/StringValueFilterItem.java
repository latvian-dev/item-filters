package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.api.FilterInfo;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import dev.latvian.mods.itemfilters.core.ItemFiltersStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author LatvianModder
 */
public abstract class StringValueFilterItem extends BaseFilterItem implements IStringValueFilter {
	public <T extends StringValueData<?>> T getStringValueData(ItemStack filter) {
		return (T) ((ItemFiltersStack) (Object) filter).getStringValueFilterData();
	}

	public abstract StringValueData<?> createData(ItemStack stack);

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (world.isClientSide()) {
			ItemFilters.proxy.openStringValueFilterScreen(this, stack, hand);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public String getValue(ItemStack filter) {
		return getStringValueData(filter).getValueAsString();
	}

	@Override
	public void setValue(ItemStack filter, String v) {
		getStringValueData(filter).setValueFromString(v);
	}

	@Override
	public void resetFilterData(ItemStack filter) {
		getStringValueData(filter).setValue(null);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void addInfo(ItemStack filter, FilterInfo info, boolean expanded) {
		Component s = getStringValueData(filter).getValueAsComponent();

		if (s != TextComponent.EMPTY) {
			info.add(s);
		}
	}

	public String getHelpKey() {
		return "itemfilters.help_text.filter";
	}
}