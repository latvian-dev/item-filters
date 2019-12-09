package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.ItemFilters;
import dev.latvian.mods.itemfilters.api.FilterInfo;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public abstract class StringValueFilterItem extends BaseFilterItem implements IStringValueFilter
{
	public <T extends StringValueData> T getStringValueData(ItemStack filter)
	{
		return (T) filter.getCapability(StringValueData.CAPABILITY).orElseThrow(NullPointerException::new);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		return createData(stack);
	}

	public abstract StringValueData createData(ItemStack stack);

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (world.isRemote)
		{
			ItemFilters.instance.proxy.openStringValueFilterScreen(this, stack, hand);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public String getValue(ItemStack filter)
	{
		return filter.hasTag() ? filter.getTag().getString("value") : "";
	}

	@Override
	public void setValue(ItemStack filter, String v)
	{
		StringValueData<?> data = getStringValueData(filter);
		data.setValueFromString(v);
	}

	@Override
	public void resetFilterData(ItemStack filter)
	{
		StringValueData<?> data = getStringValueData(filter);
		data.setValue(null);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInfo(ItemStack filter, FilterInfo info, boolean expanded)
	{
		String s = getValue(filter);

		if (!s.isEmpty())
		{
			info.add(new StringTextComponent(s));
		}
	}
}