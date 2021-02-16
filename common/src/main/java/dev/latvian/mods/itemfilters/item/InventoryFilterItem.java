package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.FilterInfo;
import dev.latvian.mods.itemfilters.api.IItemFilter;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import dev.latvian.mods.itemfilters.core.ItemFiltersStack;
import dev.latvian.mods.itemfilters.gui.InventoryFilterMenu;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

	public static ItemInventory getInventory(ItemStack stack)
	{
		return ((ItemFiltersStack) (Object) stack).getInventoryFilterData();
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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		if (!world.isClientSide())
		{
			MenuRegistry.openExtendedMenu((ServerPlayer) player, new ExtendedMenuProvider()
			{
				@Override
				public void saveExtraData(FriendlyByteBuf buf)
				{
					buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
				}

				@Override
				public Component getDisplayName()
				{
					return stack.getDisplayName();
				}

				@Override
				public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
				{
					return new InventoryFilterMenu(i, inventory, hand);
				}
			});
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
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
		inventory.clearContent();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void addInfo(ItemStack filter, FilterInfo info, boolean expanded)
	{
		ItemInventory inventory = getInventory(filter);

		if (inventory == null)
		{
			return;
		}

		for (ItemStack stack1 : inventory.getItems())
		{
			info.add(stack1.getHoverName());

			if (expanded)
			{
				IItemFilter filter1 = ItemFiltersAPI.getFilter(stack1);

				if (filter1 != null)
				{
					info.push();
					filter1.addInfo(stack1, info, true);
					info.pop();
				}
			}
		}
	}
}