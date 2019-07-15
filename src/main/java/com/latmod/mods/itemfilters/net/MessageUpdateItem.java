package com.latmod.mods.itemfilters.net;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author LatvianModder
 */
public class MessageUpdateItem
{
	private Hand hand;
	private CompoundNBT nbt;
	public MessageUpdateItem()
	{
	}

	public MessageUpdateItem(Hand h, IItemFilter filter)
	{
		hand = h;

		if (filter instanceof INBTSerializable)
		{
			INBT nbt1 = ((INBTSerializable) filter).serializeNBT();

			if (nbt1 != null)
			{
				nbt = new CompoundNBT();
				nbt.put("x", nbt1);
			}
		}
	}

	public MessageUpdateItem(PacketBuffer buffer)
	{
		fromBytes(buffer);
	}

	public void onMessage(NetworkEvent.Context ctx)
	{
		if (nbt != null)
		{
			ServerPlayerEntity player = ctx.getSender();
			player.server.execute(() -> {
				ItemStack stack = player.getHeldItem(hand);
				IItemFilter filter = ItemFiltersAPI.getFilter(stack);

				if (filter instanceof INBTSerializable)
				{
					((INBTSerializable) filter).deserializeNBT(nbt.get("x"));
				}
			});
		}
	}

	public void fromBytes(PacketBuffer buf)
	{
		hand = buf.readBoolean() ? Hand.OFF_HAND : Hand.MAIN_HAND;
		nbt = buf.readCompoundTag();
	}

	public void toBytes(PacketBuffer buf)
	{
		buf.writeBoolean(hand != Hand.MAIN_HAND);
		buf.writeCompoundTag(nbt);
	}

	public void send()
	{
		ItemFiltersNetHandler.NET.sendToServer(this);
	}
}