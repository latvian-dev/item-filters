package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.FilterInfo;
import dev.latvian.mods.itemfilters.api.IItemFilter;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import dev.latvian.mods.itemfilters.gui.InventoryFilterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class InventoryFilterItem extends BaseFilterItem
{
	public static class FilterSlot
	{
		public final int x, y;

		public FilterSlot(int _x, int _y)
		{
			x = _x;
			y = _y;
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		return new ItemInventory(stack);
	}

	public static ItemInventory getInventory(ItemStack stack)
	{
		return stack.getCapability(ItemInventory.CAPABILITY).orElseThrow(NullPointerException::new);
	}

	public int getInventorySize(ItemStack filter)
	{
		return 27;
	}

	public void addSlots(ItemStack filter, List<FilterSlot> list)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				list.add(new FilterSlot(8 + j * 18, 16 + i * 18));
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (!world.isRemote)
		{
			NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider()
			{
				@Override
				public ITextComponent getDisplayName()
				{
					return stack.getDisplayName();
				}

				@Override
				public InventoryFilterContainer createMenu(int id, PlayerInventory playerInventory, PlayerEntity player)
				{
					return new InventoryFilterContainer(id, playerInventory, hand);
				}
			}, buffer -> buffer.writeBoolean(hand == Hand.MAIN_HAND));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public void clearFilterCache(ItemStack filter)
	{
		super.clearFilterCache(filter);
		ItemInventory inventory = getInventory(filter);

		for (ItemStack item : inventory.getItems())
		{
			IItemFilter f = ItemFiltersAPI.getFilter(item);

			if (f != null)
			{
				f.clearFilterCache(item);
			}
		}
	}

	@Override
	public void resetFilterData(ItemStack filter)
	{
		ItemInventory inventory = getInventory(filter);
		inventory.clear();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInfo(ItemStack filter, FilterInfo info, boolean expanded)
	{
		ItemInventory inventory = getInventory(filter);

		for (ItemStack stack1 : inventory.getItems())
		{
			info.add(stack1.getDisplayName());

			if (expanded)
			{
				IItemFilter filter1 = ItemFiltersAPI.getFilter(stack1);

				if (filter1 != null)
				{
					info.push();
					filter1.addInfo(stack1, info, expanded);
					info.pop();
				}
			}
		}
	}
}