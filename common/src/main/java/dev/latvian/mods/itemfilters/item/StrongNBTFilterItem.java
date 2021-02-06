package dev.latvian.mods.itemfilters.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class StrongNBTFilterItem extends StringValueFilterItem
{
	public static class NBTData extends StringValueData<CompoundTag>
	{
		public NBTData(ItemStack is)
		{
			super(is);
		}

		@Nullable
		@Override
		protected CompoundTag fromString(String s)
		{
			try
			{
				return TagParser.parseTag(s);
			}
			catch (Exception ex)
			{
				return null;
			}
		}

		@Override
		protected String toString(CompoundTag value)
		{
			return value.toString();
		}

		@Override
		@Nullable
		public CompoundTag getValue()
		{
			if (load)
			{
				load = false;
				value = null;

				if (filter.hasTag() && filter.getTag().contains("value"))
				{
					value = filter.getTag().getCompound("value");
				}
			}

			return value;
		}

		@Override
		public void setValue(@Nullable CompoundTag v)
		{
			value = v;
			load = false;

			if (value == null)
			{
				filter.removeTagKey("value");
			}
			else
			{
				filter.addTagElement("value", value);
			}
		}
	}

	@Override
	public StringValueData createData(ItemStack stack)
	{
		return new NBTData(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		if (player.isCrouching() && hand == InteractionHand.MAIN_HAND)
		{
			return super.use(world, player, hand);
		}

		if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty() && player.getOffhandItem().hasTag())
		{
			NBTData data = getStringValueData(player.getMainHandItem());
			data.setValue(player.getOffhandItem().getTag().copy());
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getMainHandItem());
		}

		return super.use(world, player, hand);
	}

	@Override
	public boolean filter(ItemStack filter, ItemStack item)
	{
		NBTData data = getStringValueData(filter);
		CompoundTag tag1 = data.getValue();
		CompoundTag tag2 = item.getTag();
		return Objects.equals(tag1, tag2);
	}
}