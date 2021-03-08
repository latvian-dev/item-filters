package dev.latvian.mods.itemfilters.core.mixin.common;

import dev.latvian.mods.itemfilters.core.ItemFiltersStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

/**
 * TODO: Fix this later with capabilities on forge and Cardinal-Components-API on fabric (or arch alternative)
 *
 * @author LatvianModder
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemFiltersStack {
	@Shadow
	public abstract Item getItem();

	private Optional<Object> itemFiltersData;

	@Override
	public Object getItemFiltersData() {
		if (itemFiltersData == null) {
			itemFiltersData = Optional.ofNullable(createDataIF(getItem()));
		}

		return itemFiltersData.orElse(null);
	}
}
