package dev.latvian.mods.itemfilters;

import dev.latvian.mods.itemfilters.api.IItemFilter;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * LRU cache to improve the performance of {@link dev.latvian.mods.itemfilters.api.IItemFilter#getDisplayItemStacks(ItemStack, List)}
 * (and thus {@link dev.latvian.mods.itemfilters.api.ItemFiltersAPI#getDisplayItemStacks(ItemStack, List)}
 */
public class DisplayStacksCache {
    private static final int MAX_CACHE_SIZE = 1024;
    private static final Object2ObjectLinkedOpenHashMap<CacheKey, List<ItemStack>> cache = new Object2ObjectLinkedOpenHashMap<>(MAX_CACHE_SIZE);
    private static final NonNullList<ItemStack> allKnownStacks = NonNullList.create();

    @Nonnull
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
        if (allKnownStacks.isEmpty()) {
            for (Item item : Registry.ITEM) {
                try {
                    // all items appear in the creative search tab
                    item.fillItemCategory(CreativeModeTab.TAB_SEARCH, allKnownStacks);
                } catch (Throwable ignored) {
                }
            }
        }

        IItemFilter f = (IItemFilter) filterStack.getItem();
        return allKnownStacks.stream().filter(candidate -> f.filter(filterStack, candidate)).toList();
    }

    public static void clear() {
        cache.clear();
    }

    private static class CacheKey {
        private final int key;

        private CacheKey(ItemStack filterStack) {
            key = Objects.hash(Registry.ITEM.getId(filterStack.getItem()), filterStack.hasTag() ? filterStack.getTag().hashCode() : 0);
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
