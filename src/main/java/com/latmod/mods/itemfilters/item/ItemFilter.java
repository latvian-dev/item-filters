package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ItemFilter extends Item
{
	private final Supplier<BaseItemFilter> function;

	public ItemFilter(Supplier<BaseItemFilter> f)
	{
		function = f;
	}

	@Override
	public BaseItemFilter initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return function.get();
	}

	@Override
	@Nullable
	public NBTTagCompound getNBTShareTag(ItemStack stack)
	{
		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof INBTSerializable || stack.hasTagCompound())
		{
			NBTTagCompound nbt = new NBTTagCompound();

			if (stack.getTagCompound() != null)
			{
				nbt.setTag("nbt", stack.getTagCompound());
			}

			if (filter instanceof INBTSerializable)
			{
				nbt.setTag("filter", ((INBTSerializable) filter).serializeNBT());
			}

			return nbt;
		}

		return null;
	}

	@Override
	public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		stack.setTagCompound(null);

		if (nbt != null)
		{
			if (nbt.hasKey("nbt"))
			{
				stack.setTagCompound(nbt.getCompoundTag("nbt"));
			}

			if (nbt.hasKey("filter"))
			{
				IItemFilter filter = ItemFiltersAPI.getFilter(stack);

				if (filter instanceof INBTSerializable)
				{
					((INBTSerializable) filter).deserializeNBT(nbt.getTag("filter"));
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof BaseItemFilter && openGUI((BaseItemFilter) filter, player, hand, stack))
		{
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	private boolean openGUI(BaseItemFilter filter, EntityPlayer player, EnumHand hand, ItemStack heldItem)
	{
		return filter.openGUI(player, hand, heldItem);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format("itemfilters.filter") + ": " + I18n.format(stack.getTranslationKey() + ".filter"));

		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof BaseItemFilter)
		{
			((BaseItemFilter) filter).addInformation(tooltip);
		}
	}
}