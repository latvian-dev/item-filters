package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.FilterInfo;
import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ItemGroupFilterItem extends StringValueFilterItem
{
	private static Map<String, ItemGroup> groups = null;

	@Nullable
	private static ItemGroup getItemGroup(String v)
	{
		if (groups == null)
		{
			groups = new HashMap<>();

			for (ItemGroup group : ItemGroup.GROUPS)
			{
				groups.put(group.getPath(), group);
			}
		}

		return groups.get(v);
	}

	public static class ItemGroupData extends StringValueData<ItemGroup>
	{
		public ItemGroupData(ItemStack is)
		{
			super(is);
		}

		@Nullable
		@Override
		protected ItemGroup fromString(String s)
		{
			return getItemGroup(s);
		}

		@Override
		protected String toString(ItemGroup value)
		{
			return value.getPath();
		}
	}

	@Override
	public StringValueData createData(ItemStack stack)
	{
		return new ItemGroupData(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public List<StringValueFilterVariant> getValueVariants(ItemStack filter)
	{
		List<StringValueFilterVariant> variants = new ArrayList<>();

		for (ItemGroup t : ItemGroup.GROUPS)
		{
			if (t == ItemGroup.SEARCH || t == ItemGroup.INVENTORY)
			{
				continue;
			}

			StringValueFilterVariant variant = new StringValueFilterVariant(t.getPath());
			variant.title = new TranslationTextComponent(t.getTranslationKey());
			variant.icon = t.getIcon();
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

		for (ItemGroup t : item.getItem().getCreativeTabs())
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

			for (Item item : ForgeRegistries.ITEMS.getValues())
			{
				item.fillItemGroup(data.getValue(), allItems);
			}

			list.addAll(allItems);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInfo(ItemStack filter, FilterInfo info, boolean expanded)
	{
		ItemGroupData data = getStringValueData(filter);

		if (data.getValue() != null)
		{
			info.add(new TranslationTextComponent(data.getValue().getTranslationKey()));
		}
	}
}