package dev.latvian.mods.itemfilters;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.EnvExecutor;
import dev.architectury.utils.GameInstance;
import dev.latvian.mods.itemfilters.api.ItemFiltersItems;
import dev.latvian.mods.itemfilters.client.ItemFiltersClient;
import dev.latvian.mods.itemfilters.gui.InventoryFilterMenu;
import dev.latvian.mods.itemfilters.net.ItemFiltersNet;
import dev.latvian.mods.itemfilters.net.MessageClearDisplayCache;
import net.fabricmc.api.EnvType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemFilters {
	public static final String MOD_ID = "itemfilters";

	public static ItemFiltersCommon proxy;

	public void setup() {
		RegistrarManager registrar = RegistrarManager.get(MOD_ID);

		proxy = EnvExecutor.getEnvSpecific(() -> ItemFiltersClient::new, () -> ItemFiltersCommon::new);

		ItemFiltersItems.CREATIVE_TAB = registrar.get(Registries.CREATIVE_MODE_TAB)
				.register(new ResourceLocation(MOD_ID, "default"),
						() -> CreativeTabRegistry.create(Component.translatable("itemGroup.itemfilters.main"), () -> new ItemStack(ItemFiltersItems.ALWAYS_TRUE.get())));

		InventoryFilterMenu.TYPE = registrar.get(Registries.MENU)
				.register(new ResourceLocation(MOD_ID, "inventory_filter"), () -> MenuRegistry.ofExtended(InventoryFilterMenu::new));

		ItemFiltersItems.init();
		ItemFiltersNet.init();
		EnvExecutor.runInEnv(EnvType.CLIENT, () -> proxy::setup);

		ReloadListenerRegistry.register(PackType.SERVER_DATA, new TagReloadListener());
	}

	private static class TagReloadListener implements ResourceManagerReloadListener {
		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			// need to clear all cached items since we can't know for sure if a tag filter has been used
			// (it could be arbitrarily deeply nested inside other filters)
			DisplayStacksCache.clear();

			// notify all non-local players that the cache needs to be cleared (local player doesn't need notifying
			// since the displayed items cache is static and shared between integrated server and client)
			MinecraftServer server = GameInstance.getServer();
			if (server != null) {
				MessageClearDisplayCache msg = new MessageClearDisplayCache();
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					if (!server.isSingleplayerOwner(player.getGameProfile())) {
						ItemFiltersNet.MAIN.sendToPlayer(player, msg);
					}
				}
			}
		}
	}
}