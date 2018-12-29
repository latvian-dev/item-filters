package com.latmod.mods.itemfilters.block;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class PipeItem implements INBTSerializable<NBTTagCompound>
{
	public ItemStack stack = ItemStack.EMPTY;
	public int pos = 0;
	public int speed = 1;
	public long created = 0L;
	public int lifespan = -1;
	private double scale = -1D;

	public int getLifespan(World world)
	{
		if (lifespan == -1)
		{
			lifespan = stack.getItem().getEntityLifespan(stack, world);
		}

		return lifespan;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = stack.serializeNBT();
		nbt.setLong("created", created);
		nbt.setByte("pos", (byte) pos);

		if (speed > 1)
		{
			nbt.setByte("speed", (byte) speed);
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		stack = new ItemStack(nbt);
		created = nbt.getLong("created");
		pos = nbt.getByte("pos");
		speed = MathHelper.clamp(nbt.getByte("speed"), 1, 20);
	}

	@SideOnly(Side.CLIENT)
	public void render(RenderItem renderItem)
	{
		renderItem.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
	}

	@SideOnly(Side.CLIENT)
	public double getScale(RenderItem renderItem)
	{
		if (scale <= 0D)
		{
			scale = 0.4D;

			if (renderItem.shouldRenderItemIn3D(stack))
			{
				scale = 0.749D;
			}
		}

		return scale;
	}
}