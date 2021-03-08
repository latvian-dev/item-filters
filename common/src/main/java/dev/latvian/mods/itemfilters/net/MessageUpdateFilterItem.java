package dev.latvian.mods.itemfilters.net;

import dev.latvian.mods.itemfilters.api.IItemFilter;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class MessageUpdateFilterItem {
	private final InteractionHand hand;
	private final ItemStack stack;

	public MessageUpdateFilterItem(InteractionHand h, ItemStack is) {
		hand = h;
		stack = is;
	}

	public MessageUpdateFilterItem(FriendlyByteBuf buffer) {
		hand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		stack = buffer.readItem();
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
		buf.writeItem(stack);
	}

	public void handle(Supplier<NetworkManager.PacketContext> context) {
		context.get().queue(() -> {
			Player player = context.get().getPlayer();

			if (player != null) {
				ItemStack is = player.getItemInHand(hand);

				if (is.getItem() instanceof IItemFilter && stack.getItem() instanceof IItemFilter) {
					player.setItemInHand(hand, stack);
					player.inventory.setChanged();
					player.inventoryMenu.broadcastChanges();
				}
			}
		});

		//context.get().setPacketHandled(true);
	}

	public void send() {
		ItemFiltersNet.MAIN.sendToServer(this);
	}
}