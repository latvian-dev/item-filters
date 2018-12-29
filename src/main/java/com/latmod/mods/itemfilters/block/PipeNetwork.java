package com.latmod.mods.itemfilters.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class PipeNetwork implements ICapabilityProvider
{
	@CapabilityInject(PipeNetwork.class)
	public static Capability<PipeNetwork> CAP;

	public static PipeNetwork get(World world)
	{
		return world.getCapability(CAP, null);
	}

	public final World world;
	public final List<TilePipe> pipes = new ArrayList<>();
	public boolean refresh = true;

	public PipeNetwork(World w)
	{
		world = w;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CAP;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CAP ? (T) this : null;
	}

	public void refresh()
	{
		refresh = true;
	}

	public void tick()
	{
		if (refresh)
		{
			pipes.clear();

			for (TileEntity tileEntity : world.loadedTileEntityList)
			{
				if (!tileEntity.isInvalid() && tileEntity instanceof TilePipe)
				{
					pipes.add((TilePipe) tileEntity);
				}
			}

			refresh = false;
		}

		for (TilePipe pipe : pipes)
		{
			pipe.moveItems();
		}

		for (TilePipe pipe : pipes)
		{
			pipe.tick();
		}
	}

	public void render(float partialTicks)
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		double renderDistanceSq = 64 * 64;
		EnumFacing.Axis axis;
		double pos, scale, rx, ry, rz;
		float rotX, rotY;
		double px = TileEntityRendererDispatcher.staticPlayerX;
		double py = TileEntityRendererDispatcher.staticPlayerY;
		double pz = TileEntityRendererDispatcher.staticPlayerZ;
		Frustum frustum = new Frustum();
		frustum.setPosition(px, py, pz);
		GlStateManager.pushMatrix();
		GlStateManager.translate(-px, -py, -pz);
		GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();

		for (TilePipe pipe : pipes)
		{
			if (pipe.fromNegative.items.isEmpty() && pipe.fromPositive.items.isEmpty())
			{
				continue;
			}

			BlockPos pipePos = pipe.getPos();
			double x = pipePos.getX() + 0.5D;
			double y = pipePos.getY() + 0.5D;
			double z = pipePos.getZ() + 0.5D;

			if (((px - x) * (px - x) + (py - y) * (py - y) + (pz - z) * (pz - z)) > renderDistanceSq || !frustum.isBoxInFrustum(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D))
			{
				continue;
			}

			axis = pipe.getAxis();
			GlStateManager.pushMatrix();
			GlStateManager.translate(pipePos.getX(), pipePos.getY(), pipePos.getZ());

			for (PipeItem item : pipe.fromNegative.items)
			{
				pos = (item.pos - item.speed + item.speed * partialTicks) / 20D;

				if (axis == EnumFacing.Axis.X)
				{
					rx = pos;
					ry = 0.5D;
					rz = 0.5D;
					rotX = 0F;
					rotY = 270F;
				}
				else if (axis == EnumFacing.Axis.Z)
				{
					rx = 0.5D;
					ry = 0.5D;
					rz = pos;
					rotX = 0F;
					rotY = 180F;
				}
				else
				{
					rx = 0.5D;
					ry = pos;
					rz = 0.5D;
					rotX = 90F;
					rotY = 0F;
				}

				scale = item.getScale(renderItem);

				GlStateManager.pushMatrix();
				GlStateManager.translate(rx, ry, rz);
				GlStateManager.rotate(rotY, 0F, 1F, 0F);
				GlStateManager.rotate(rotX, 1F, 0F, 0F);
				GlStateManager.scale(scale, scale, scale);
				item.render(renderItem);
				GlStateManager.popMatrix();
			}

			for (PipeItem item : pipe.fromPositive.items)
			{
				pos = 1D - (item.pos - item.speed + item.speed * partialTicks) / 20D;

				if (axis == EnumFacing.Axis.X)
				{
					rx = pos;
					ry = 0.5D;
					rz = 0.5D;
					rotX = 0F;
					rotY = 90F;
				}
				else if (axis == EnumFacing.Axis.Z)
				{
					rx = 0.5D;
					ry = 0.5D;
					rz = pos;
					rotX = 0F;
					rotY = 0F;
				}
				else
				{
					rx = 0.5D;
					ry = pos;
					rz = 0.5D;
					rotX = 270F;
					rotY = 180F;
				}

				scale = item.getScale(renderItem);

				GlStateManager.pushMatrix();
				GlStateManager.translate(rx, ry, rz);
				GlStateManager.rotate(rotY, 0F, 1F, 0F);
				GlStateManager.rotate(rotX, 1F, 0F, 0F);
				GlStateManager.scale(scale, scale, scale);
				item.render(renderItem);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}

		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}