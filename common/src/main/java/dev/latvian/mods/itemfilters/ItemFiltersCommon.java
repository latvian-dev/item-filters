package dev.latvian.mods.itemfilters;

import dev.architectury.utils.GameInstance;
import dev.latvian.mods.itemfilters.api.IStringValueFilter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @author LatvianModder
 */
public class ItemFiltersCommon {
	public void setup() {
	}

	public void openStringValueFilterScreen(IStringValueFilter filter, ItemStack stack, InteractionHand hand) {
	}

	public Optional<RegistryAccess> registryAccess() {
		if (GameInstance.getServer() != null) {
			ServerLevel overworld = GameInstance.getServer().getLevel(Level.OVERWORLD);
			if (overworld != null) {
				return Optional.of(overworld.registryAccess());
			}
		}
		return Optional.empty();
	}
}