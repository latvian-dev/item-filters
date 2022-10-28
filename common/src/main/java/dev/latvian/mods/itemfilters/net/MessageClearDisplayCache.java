package dev.latvian.mods.itemfilters.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.latvian.mods.itemfilters.DisplayStacksCache;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

/**
 * Sent to non-local clients by the server when tags are reloaded
 * TODO: remove this when architectury supports a tags-synced event
 */
public class MessageClearDisplayCache {
    public MessageClearDisplayCache() {
    }

    public MessageClearDisplayCache(@SuppressWarnings("unused") FriendlyByteBuf buf) {
    }

    public void write(@SuppressWarnings("unused") FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkManager.PacketContext> context) {
        if (context.get().getEnvironment() == Env.CLIENT) {
            context.get().queue(DisplayStacksCache::clear);
        }
    }
}
