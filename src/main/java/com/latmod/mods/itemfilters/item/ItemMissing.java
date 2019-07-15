package com.latmod.mods.itemfilters.item;

import com.latmod.mods.itemfilters.util.NBTUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemMissing extends Item
{
	public ItemMissing()
	{
		super(new Item.Properties().maxStackSize(1));
	}

	private static ItemStack getContainedStack(ItemStack stack)
	{
		return stack.hasTag() ? ItemStackSerializer.read(stack.getChildTag("item")) : ItemStack.EMPTY;
	}

	public static ItemStack read(@Nullable INBT nbt)
	{
		if (NBTUtil.isNullOrEmpty(nbt))
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack = ItemStackSerializer.read(nbt);

		if (stack.getItem() == ItemFiltersItems.MISSING)
		{
			ItemStack stack1 = getContainedStack(stack);

			if (!stack1.isEmpty())
			{
				return stack1;
			}
		}
		else if (stack.isEmpty())
		{
			ItemStack stack1 = new ItemStack(ItemFiltersItems.MISSING);
			stack1.setTagInfo("item", nbt.copy());
			return stack1;
		}

		return stack;
	}

	public static INBT write(ItemStack stack, boolean forceCompound)
	{
		if (stack.getItem() == ItemFiltersItems.MISSING)
		{
			INBT base = stack.hasTag() ? stack.getChildTag("item") : null;

			if (forceCompound)
			{
				CompoundNBT nbt = new CompoundNBT();

				if (!NBTUtil.isNullOrEmpty(base))
				{
					nbt.put("item", base);
				}

				return nbt;
			}

			return NBTUtil.isNullOrEmpty(base) ? new StringNBT("") : base;
		}

		return ItemStackSerializer.write(stack, forceCompound);
	}


	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected)
	{
		if (!(entity instanceof PlayerEntity) || world.getWorldInfo().getGameTime() % 100L != 65L)
		{
			return;
		}

		ItemStack stack1 = getContainedStack(stack);

		if (!stack1.isEmpty())
		{
			if (!world.isRemote)
			{
				ItemHandlerHelper.giveItemToPlayer((PlayerEntity) entity, stack1, slot);
			}

			stack.shrink(1);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return !getContainedStack(stack).isEmpty();
	}

	@Override
	public Rarity getRarity(ItemStack stack)
	{
		return Rarity.EPIC;
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			return super.getDisplayName(stack);
		}

		INBT nbt = stack.getChildTag("item");

		if (NBTUtil.isNullOrEmpty(nbt))
		{
			return super.getDisplayName(stack);
		}

		ItemStack stack1 = ItemStackSerializer.read(nbt);
		ResourceLocation name;
		int count = 1;

		if (!stack1.isEmpty())
		{
			name = stack1.getItem().getRegistryName();
			count = stack1.getCount();
		}
		else
		{
			CompoundNBT nbt1;

			if (nbt instanceof StringNBT)
			{
				nbt1 = new CompoundNBT();
				nbt1.put("item", nbt);
			}
			else
			{
				nbt1 = (CompoundNBT) nbt;
			}

			if (nbt1.contains("item", Constants.NBT.TAG_STRING))
			{
				String[] sa = nbt1.getString("item").split(" ", 4);
				name = new ResourceLocation(sa[0]);

				if (sa.length >= 2)
				{
					count = MathHelper.getInt(sa[1], 1);
				}
			}
			else
			{
				name = new ResourceLocation(nbt1.getString("id"));
				count = nbt1.getByte("Count");
			}
		}

		StringBuilder out = new StringBuilder();

		if (count > 1)
		{
			out.append(TextFormatting.YELLOW);
			out.append(count);
			out.append(TextFormatting.DARK_GRAY);
			out.append('x');
		}

		out.append(TextFormatting.AQUA);
		out.append(name.getNamespace());
		out.append(TextFormatting.DARK_GRAY);
		out.append(':');
		out.append(TextFormatting.GOLD);
		out.append(name.getPath());

		return new StringTextComponent(out.toString());
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (stack.hasTag() && stack.getChildTag("item") != null)
		{
			tooltip.add(super.getDisplayName(stack).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
		}
	}
}