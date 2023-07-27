package dev.latvian.mods.itemfilters;

import dev.latvian.mods.itemfilters.api.IItemFilter;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * LRU cache to improve the performance of {@link dev.latvian.mods.itemfilters.api.IItemFilter#getDisplayItemStacks(ItemStack, List)}
 * (and thus {@link dev.latvian.mods.itemfilters.api.ItemFiltersAPI#getDisplayItemStacks(ItemStack, List)}
 */
public class DisplayStacksCache {
    private static final int MAX_CACHE_SIZE = 1024;
    private static final Object2ObjectLinkedOpenHashMap<CacheKey, List<ItemStack>> cache = new Object2ObjectLinkedOpenHashMap<>(MAX_CACHE_SIZE);

    @NotNull
    public static List<ItemStack> getCachedDisplayStacks(ItemStack filterStack) {
        CacheKey key = new CacheKey(filterStack);

        List<ItemStack> result = cache.getAndMoveToFirst(key);
        if (result == null) {
            result = computeMatchingStacks(filterStack);
            cache.put(key, result);
            if (cache.size() >= MAX_CACHE_SIZE) {
                cache.removeLast();
            }
        }

        return result;
    }

    private static List<ItemStack> computeMatchingStacks(ItemStack filterStack) {
        IItemFilter f = (IItemFilter) filterStack.getItem();

        ItemFilters.proxy.registryAccess().ifPresent(ra -> CreativeModeTabs.tryRebuildTabContents(FeatureFlags.DEFAULT_FLAGS, true, ra));

        return CreativeModeTabs.searchTab().getSearchTabDisplayItems().stream()
                .filter(candidate -> f.filter(filterStack, candidate))
                .toList();
    }

    public static void clear() {
        cache.clear();
    }

    private static class CacheKey {
        private final int key;

        private CacheKey(ItemStack filterStack) {
            key = Objects.hash(BuiltInRegistries.ITEM.getId(filterStack.getItem()), filterStack.hasTag() ? filterStack.getTag().hashCode() : 0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return key == cacheKey.key;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(key);
        }
    }
}
