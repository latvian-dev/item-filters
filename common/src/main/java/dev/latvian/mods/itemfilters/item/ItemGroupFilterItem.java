package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.FilterInfo;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ItemGroupFilterItem extends StringValueFilterItem
{
	private static Map<String, CreativeModeTab> groups = null;

	@Nullable
	private static CreativeModeTab getItemGroup(String v)
	{
		if (groups == null)
		{
			groups = new HashMap<>();

			for (CreativeModeTab group : CreativeModeTab.TABS)
			{
				groups.put(group.getRecipeFolderName(), group);
			}
		}

		return groups.get(v);
	}

	public static class ItemGroupData extends StringValueData<CreativeModeTab>
	{
		public ItemGroupData(ItemStack is)
		{
			super(is);
		}

		@Nullable
		@Override
		protected CreativeModeTab fromString(String s)
		{
			return getItemGroup(s);
		}

		@Override
		protected String toString(CreativeModeTab value)
		{
			return value.getRecipeFolderName();
		}
	}

	@Override
	public StringValueData createData(ItemStack stack)
	{
		return new ItemGroupData(stack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public List<StringValueFilterVariant> getValueVariants(ItemStack filter)
	{
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (CreativeModeTab t : CreativeModeTab.TABS)
		{
			if (t == CreativeModeTab.TAB_SEARCH || t == CreativeModeTab.TAB_INVENTORY)
			{
				continue;
			}

			StringValueFilterVariant variant = new StringValueFilterVariant(t.getRecipeFolderName());
			variant.title = t.getDisplayName();
			variant.icon = t.getIconItem();
			variants.add(variant);
		}

		return variants;
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		ItemGroupData data = getStringValueData(filter);

		if (data.getValue() == null)
		{
			return false;
		}

		for (CreativeModeTab t : Collections.singleton(item.getItem().getItemCategory())) // TODO: Forge impl
		{
			if (t == data.getValue())
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
		ItemGroupData data = getStringValueData(filter);

		if (data.getValue() != null)
		{
			NonNullList<ItemStack> allItems = NonNullList.create();

			for (Item item : Registry.ITEM)
			{
				try
				{
					item.fillItemCategory(data.getValue(), allItems);
				}
				catch (Throwable ex)
				{
				}
			}

			list.addAll(allItems);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void addInfo(ItemStack filter, FilterInfo info, boolean expanded)
	{
		ItemGroupData data = getStringValueData(filter);

		if (data != null && data.getValue() != null)
		{
			info.add(data.getValue().getDisplayName());
		}
	}
}