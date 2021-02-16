package dev.latvian.mods.itemfilters.item;

import dev.latvian.mods.itemfilters.api.StringValueFilterVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TagFilterItem extends StringValueFilterItem
{
	public static class TagData extends StringValueData<ResourceLocation>
	{
		public TagData(ItemStack is)
		{
			super(is);
		}

		@Nullable
		@Override
		public ResourceLocation fromString(String s)
		{
			if (s.isEmpty())
			{
				return null;
			}

			ResourceLocation id = new ResourceLocation(s);
			return ItemTags.getAllTags().getTag(id) == null ? null : id;
		}

		@Override
		public String toString(ResourceLocation value)
		{
			return value == null ? "" : value.toString();
		}
	}

	@Override
	public StringValueData createData(ItemStack stack)
	{
		return new TagData(stack);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return false;
		}

		Tag<Item> tag = ItemTags.getAllTags().getTag(new ResourceLocation(getValue(filter)));

		if (tag != null && !tag.getValues().isEmpty())
		{
			return tag.contains(stack.getItem());
		}

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Collection<StringValueFilterVariant> getValueVariants(ItemStack filter)
	{
		List<StringValueFilterVariant> list = new ArrayList<>();

		for (ResourceLocation id : ItemTags.getAllTags().getAvailableTags())
		{
			Tag<Item> tag = ItemTags.getAllTags().getTag(id);

			if (tag != null && !tag.getValues().isEmpty())
			{
				StringValueFilterVariant variant = new StringValueFilterVariant(id.toString());
				variant.icon = new ItemStack(tag.getValues().iterator().next());
				list.add(variant);
			}
		}

		return list;
	}

	@Override
	public void getValidFilterItems(ItemStack filter, List<ItemStack> list)
	{
		Tag<Item> items = ItemTags.getAllTags().getTag(new ResourceLocation(getValue(filter)));

		if (items == null || items.getValues().isEmpty())
		{
			return;
		}

		NonNullList<ItemStack> list1 = NonNullList.create();

		for (Item item : items.getValues())
		{
			item.fillItemCategory(CreativeModeTab.TAB_SEARCH, list1);
		}

		list.addAll(list1);
	}
}