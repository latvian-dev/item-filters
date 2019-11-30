package com.latmod.mods.itemfilters.net;

import com.latmod.mods.itemfilters.api.IItemFilter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class MessageUpdateFilterItem
{
	private final Hand hand;
	private final CompoundNBT nbt;

	public MessageUpdateFilterItem(Hand h, @Nullable CompoundNBT is)
	{
		hand = h;
		nbt = is;
	}

	public MessageUpdateFilterItem(PacketBuffer buffer)
	{
		hand = buffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		nbt = buffer.readCompoundTag();
	}

	public void write(PacketBuffer buf)
	{
		buf.writeBoolean(hand == Hand.MAIN_HAND);
		buf.writeCompoundTag(nbt);
	}

	public void handle(Supplier<NetworkEvent.Context> contextSupplier)
	{
		ServerPlayerEntity player = contextSupplier.get().getSender();

		if (player != null)
		{
			ItemStack stack = player.getHeldItem(hand);

			if (stack.getItem() instanceof IItemFilter)
			{
				contextSupplier.get().enqueueWork(() -> stack.setTag(nbt));
			}
		}
	}
}