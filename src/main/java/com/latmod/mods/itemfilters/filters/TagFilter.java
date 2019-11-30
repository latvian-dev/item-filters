package com.latmod.mods.itemfilters.filters;

import com.latmod.mods.itemfilters.api.StringValueFilterVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TagFilter extends StringValueFilter
{
	@Override
	public String getID()
	{
		return "tag";
	}

	@Override
	public boolean filter(ItemStack stack)
	{
		return stack.getItem().getTags().contains(new ResourceLocation(getValue()));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants()
	{
		List<StringValueFilterVariant> list = new ArrayList<>();

		for (ResourceLocation id : ItemTags.getCollection().getRegisteredTags())
		{
			Tag<Item> tag = ItemTags.getCollection().get(id);

			if (tag != null && !tag.getAllElements().isEmpty())
			{
				StringValueFilterVariant variant = new StringValueFilterVariant(id.toString());
				variant.icon = new ItemStack(tag.getAllElements().iterator().next());
				list.add(variant);
			}
		}

		return list;
	}

	@Override
	public void getValidItems(List<ItemStack> list)
	{
		if (cachedItems == null)
		{
			Tag<Item> items = ItemTags.getCollection().get(new ResourceLocation(getValue()));

			if (items == null || items.getAllElements().isEmpty())
			{
				return;
			}

			NonNullList<ItemStack> list1 = NonNullList.create();

			for (Item item : items.getAllElements())
			{
				item.fillItemGroup(ItemGroup.SEARCH, list1);
			}

			cachedItems = new ArrayList<>(list1);
		}

		if (!cachedItems.isEmpty())
		{
			list.addAll(cachedItems);
		}
	}
}