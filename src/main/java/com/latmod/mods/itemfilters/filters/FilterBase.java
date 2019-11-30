package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.IRegisteredItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.integration.forestry.ForestryIntegration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class FilterBase implements IRegisteredItemFilter
{
	protected List<ItemStack> cachedItems = null;

	protected static List<ItemStack> compress(Collection<ItemStack> items)
	{
		return items.isEmpty() ? Collections.emptyList() : items.size() == 1 ? Collections.singletonList(items.iterator().next()) : Arrays.asList(items.toArray(new ItemStack[0]));
	}

	public static void register()
	{
		ItemFiltersAPI.register("always_true", () -> AlwaysTrueItemFilter.INSTANCE);
		ItemFiltersAPI.register("or", ORFilter::new);
		ItemFiltersAPI.register("and", ANDFilter::new);
		ItemFiltersAPI.register("not", NOTFilter::new);
		ItemFiltersAPI.register("xor", XORFilter::new);
		ItemFiltersAPI.register("tag", TagFilter::new);
		ItemFiltersAPI.register("mod", ModFilter::new);
		ItemFiltersAPI.register("creative_tab", ItemGroupFilter::new);

		if (ModList.get().isLoaded("forestry"))
		{
			ForestryIntegration.init();
		}
	}

	@Override
	public void getValidItems(List<ItemStack> list)
	{
		if (cachedItems == null)
		{
			NonNullList<ItemStack> allItems = NonNullList.create();

			for (Item item : ForgeRegistries.ITEMS.getValues())
			{
				item.fillItemGroup(ItemGroup.SEARCH, allItems);
			}

			cachedItems = new ArrayList<>();

			for (ItemStack stack : allItems)
			{
				if (filter(stack))
				{
					cachedItems.add(stack);
				}
			}

			cachedItems = compress(cachedItems);
		}

		if (!cachedItems.isEmpty())
		{
			list.addAll(cachedItems);
		}
	}

	@Override
	public void clearCache()
	{
		cachedItems = null;
	}
}