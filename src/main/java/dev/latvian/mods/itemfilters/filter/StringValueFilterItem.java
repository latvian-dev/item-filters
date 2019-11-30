package dev.latvian.mods.itemfilters.filter;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public abstract class StringValueFilterItem extends BaseFilterItem implements IStringValueFilter
{
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (world.isRemote)
		{
			ItemFilters.instance.proxy.openStringValueGUI(this, stack, hand);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public String getValue(ItemStack stack)
	{
		return stack.hasTag() ? stack.getTag().getString("value") : "";
	}

	@Override
	public void setValue(ItemStack stack, String v)
	{
		if (v.isEmpty())
		{
			stack.removeChildTag("value");
		}
		else
		{
			stack.setTagInfo("value", new StringNBT(v));
		}
	}

	@Override
	public void resetFilterData(ItemStack filter)
	{
		filter.removeChildTag("value");
	}
}