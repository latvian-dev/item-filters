package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.ItemFilters;
import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.IRegisteredItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.filters.AlwaysTrueItemFilter;
import com.latmod.mods.itemfilters.filters.LogicFilter;
import com.latmod.mods.itemfilters.util.NBTUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ItemFilter extends Item
{
	public static class ItemFilterData implements IItemFilter, ICapabilitySerializable<CompoundNBT>
	{
		public IRegisteredItemFilter filter = AlwaysTrueItemFilter.INSTANCE;
		public LazyOptional<IItemFilter> filterOpt = LazyOptional.of(() -> this);

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
		{
			return cap == ItemFiltersAPI.CAPABILITY ? filterOpt.cast() : LazyOptional.empty();
		}

		@Override
		public CompoundNBT serializeNBT()
		{
			CompoundNBT nbt = new CompoundNBT();
			nbt.putString("id", filter.getID());
			INBT nbt1 = filter instanceof INBTSerializable ? ((INBTSerializable) filter).serializeNBT() : null;

			if (!NBTUtil.isNullOrEmpty(nbt1))
			{
				nbt.put("data", nbt1);
			}

			return nbt;
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt)
		{
			filter = ItemFiltersAPI.createFromID(nbt.getString("id"));

			if (filter == null)
			{
				filter = AlwaysTrueItemFilter.INSTANCE;
			}

			if (filter instanceof INBTSerializable && nbt.contains("data"))
			{
				((INBTSerializable) filter).deserializeNBT(nbt.get("data"));
			}
		}

		@Override
		public boolean filter(ItemStack stack)
		{
			return filter.filter(stack);
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void openEditingGUI(Runnable save)
		{
			filter.openEditingGUI(save);
		}

		@Override
		public void getValidItems(List<ItemStack> list)
		{
			filter.getValidItems(list);
		}
	}

	public ItemFilter()
	{
		super(new Item.Properties().group(ItemGroup.TRANSPORTATION));
	}

	@Override
	public ItemFilterData initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		return new ItemFilterData();
	}

	@Override
	@Nullable
	public CompoundNBT getShareTag(ItemStack stack)
	{
		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof INBTSerializable || stack.hasTag())
		{
			CompoundNBT nbt = new CompoundNBT();

			if (stack.getTag() != null)
			{
				nbt.put("nbt", stack.getTag());
			}

			if (filter instanceof INBTSerializable)
			{
				nbt.put("filter", ((INBTSerializable) filter).serializeNBT());
			}

			return nbt;
		}

		return null;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		stack.setTag(nbt == null ? null : (CompoundNBT) nbt.get("nbt"));

		IItemFilter filter = ItemFiltersAPI.getFilter(stack);
		filter.resetData();

		if (nbt != null && filter instanceof INBTSerializable)
		{
			INBT nbt1 = nbt.get("filter");

			if (nbt1 != null)
			{
				((INBTSerializable) filter).deserializeNBT(nbt1);
			}
		}
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if (isInGroup(group))
		{
			for (Supplier<IRegisteredItemFilter> supplier : ItemFiltersAPI.REGISTRY.values())
			{
				IRegisteredItemFilter filter = supplier.get();

				if (!(filter instanceof LogicFilter))
				{
					ItemStack stack = new ItemStack(this);
					((ItemFilterData) ItemFiltersAPI.getFilter(stack)).filter = filter;
					items.add(stack);
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof ItemFilterData)
		{
			if (world.isRemote)
			{
				if (player.isSneaking())
				{
					ItemFilters.PROXY.openSelectionGUI((ItemFilterData) filter, hand);
				}
				else
				{
					ItemFilters.PROXY.openGUI(filter, hand);
				}
			}

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult<>(ActionResultType.PASS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);

		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof ItemFilterData)
		{
			IRegisteredItemFilter f = ((ItemFilterData) filter).filter;
			tooltip.add(new TranslationTextComponent("filter.itemfilters." + f.getID() + ".name").setStyle(new Style().setItalic(true)));

			if (Screen.hasShiftDown())
			{
				tooltip.add(new TranslationTextComponent(I18n.format("filter.itemfilters." + f.getID() + ".filter")).setStyle(new Style().setItalic(true).setColor(TextFormatting.DARK_GRAY)));
			}
		}
	}
}