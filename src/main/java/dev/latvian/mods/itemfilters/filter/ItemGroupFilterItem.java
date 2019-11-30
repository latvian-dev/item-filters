package dev.latvian.mods.itemfilters.filter;

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
	private ItemGroup getItemGroup(ItemStack stack)
	{
		if (groups == null)
		{
			groups = new HashMap<>();

			for (ItemGroup group : ItemGroup.GROUPS)
			{
				groups.put(group.getPath(), group);
			}
		}

		return groups.get(getValue(stack));
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
		ItemGroup group = getItemGroup(filter);

		if (group == null)
		{
			return false;
		}

		for (ItemGroup t : item.getItem().getCreativeTabs())
		{
			if (t == group)
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
		ItemGroup group = getItemGroup(filter);

		if (group != null)
		{
			NonNullList<ItemStack> allItems = NonNullList.create();

			for (Item item : ForgeRegistries.ITEMS.getValues())
			{
				item.fillItemGroup(group, allItems);
			}

			list.addAll(allItems);
		}
	}
}