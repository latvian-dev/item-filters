package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.IRegisteredItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.filters.AlwaysTrueItemFilter;
import com.latmod.mods.itemfilters.filters.LogicFilter;
import com.latmod.mods.itemfilters.gui.GuiSelectFilter;
import com.latmod.mods.itemfilters.net.MessageUpdateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
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
	public static class ItemFilterData implements IItemFilter, ICapabilitySerializable<NBTTagCompound>
	{
		public IRegisteredItemFilter filter = AlwaysTrueItemFilter.INSTANCE;

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability == ItemFiltersAPI.CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
		{
			return capability == ItemFiltersAPI.CAPABILITY ? (T) this : null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("id", filter.getID());
			NBTBase nbt1 = filter instanceof INBTSerializable ? ((INBTSerializable) filter).serializeNBT() : null;

			if (nbt1 != null && !nbt1.isEmpty())
			{
				nbt.setTag("data", nbt1);
			}

			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			filter = ItemFiltersAPI.createFromID(nbt.getString("id"));

			if (filter == null)
			{
				filter = AlwaysTrueItemFilter.INSTANCE;
			}

			if (filter instanceof INBTSerializable && nbt.hasKey("data"))
			{
				((INBTSerializable) filter).deserializeNBT(nbt.getTag("data"));
			}
		}

		@Override
		public boolean filter(ItemStack stack)
		{
			return filter.filter(stack);
		}

		@Override
		@SideOnly(Side.CLIENT)
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

	@Override
	public ItemFilterData initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new ItemFilterData();
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof ItemFilterData)
		{
			if (world.isRemote)
			{
				if (player.isSneaking())
				{
					openSelectionGUI((ItemFilterData) filter, hand);
				}
				else
				{
					openGUI(filter, hand);
				}
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	private void openSelectionGUI(ItemFilterData data, EnumHand hand)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiSelectFilter(data, () -> new MessageUpdateItem(hand, data).send()));
	}

	private void openGUI(IItemFilter filter, EnumHand hand)
	{
		filter.openEditingGUI(() -> new MessageUpdateItem(hand, filter).send());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);

		IItemFilter filter = ItemFiltersAPI.getFilter(stack);

		if (filter instanceof ItemFilterData)
		{
			IRegisteredItemFilter f = ((ItemFilterData) filter).filter;
			tooltip.add(TextFormatting.ITALIC + I18n.format("filter.itemfilters." + f.getID() + ".name"));

			if (GuiScreen.isShiftKeyDown())
			{
				tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + I18n.format("filter.itemfilters." + f.getID() + ".filter"));
			}
		}
	}
}