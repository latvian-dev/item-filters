package dev.latvian.mods.itemfilters.core.mixin.common;

import dev.latvian.mods.itemfilters.core.ItemFiltersStack;
import dev.latvian.mods.itemfilters.item.InventoryFilterItem;
import dev.latvian.mods.itemfilters.item.ItemInventory;
import dev.latvian.mods.itemfilters.item.StringValueFilterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * TODO: Fix this later with capabilities on forge and Cardinal-Components-API on fabric (or arch alternative)
 *
 * @author LatvianModder
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemFiltersStack
{
	@Shadow
	public abstract Item getItem();

	private Object itemFiltersData;

	@Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V", at = @At("RETURN"))
	private void initIF(ItemLike item, int i, CallbackInfo ci)
	{
		Item i1 = item.asItem();

		if (i1 instanceof InventoryFilterItem)
		{
			itemFiltersData = new ItemInventory((ItemStack) (Object) this);
		}
		else if (i1 instanceof StringValueFilterItem)
		{
			itemFiltersData = ((StringValueFilterItem) i1).createData((ItemStack) (Object) this);
		}
	}

	@Override
	public Object getItemFiltersData()
	{
		return itemFiltersData;
	}
}
