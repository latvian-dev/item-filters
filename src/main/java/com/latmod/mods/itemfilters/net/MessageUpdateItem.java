package com.latmod.mods.itemfilters.net;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class MessageUpdateItem implements IMessage
{
	public static class Handler implements IMessageHandler<MessageUpdateItem, IMessage>
	{
		@Override
		@Nullable
		public IMessage onMessage(MessageUpdateItem message, MessageContext ctx)
		{
			if (message.nbt != null)
			{
				EntityPlayerMP player = ctx.getServerHandler().player;
				player.server.addScheduledTask(() -> {
					ItemStack stack = player.getHeldItem(message.hand);
					IItemFilter filter = ItemFiltersAPI.getFilter(stack);

					if (filter instanceof INBTSerializable)
					{
						((INBTSerializable) filter).deserializeNBT(message.nbt.getTag("x"));
					}
				});
			}

			return null;
		}
	}

	private EnumHand hand;
	private NBTTagCompound nbt;

	public MessageUpdateItem()
	{
	}

	public MessageUpdateItem(EnumHand h, IItemFilter filter)
	{
		hand = h;

		if (filter instanceof INBTSerializable)
		{
			NBTBase nbt1 = ((INBTSerializable) filter).serializeNBT();

			if (nbt1 != null)
			{
				nbt = new NBTTagCompound();
				nbt.setTag("x", nbt1);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		hand = buf.readBoolean() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(hand != EnumHand.MAIN_HAND);
		ByteBufUtils.writeTag(buf, nbt);
	}

	public void send()
	{
		ItemFiltersNetHandler.NET.sendToServer(this);
	}
}