package dev.latvian.mods.itemfilters.net;

import dev.latvian.mods.itemfilters.api.IItemFilter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class MessageUpdateFilterItem
{
	private final Hand hand;
	private final ItemStack stack;

	public MessageUpdateFilterItem(Hand h, ItemStack is)
	{
		hand = h;
		stack = is;
	}

	public MessageUpdateFilterItem(PacketBuffer buffer)
	{
		hand = buffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		stack = buffer.readItemStack();
	}

	public void write(PacketBuffer buf)
	{
		buf.writeBoolean(hand == Hand.MAIN_HAND);
		buf.writeItemStack(stack);
	}

	public void handle(Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(() -> {
			ServerPlayerEntity player = context.get().getSender();

			if (player != null)
			{
				ItemStack is = player.getHeldItem(hand);

				if (is.getItem() instanceof IItemFilter && stack.getItem() instanceof IItemFilter)
				{
					player.setHeldItem(hand, stack);
					player.inventory.markDirty();
					player.container.detectAndSendChanges();
				}
			}
		});

		context.get().setPacketHandled(true);
	}

	public void send()
	{
		ItemFiltersNetHandler.NET.sendToServer(this);
	}
}