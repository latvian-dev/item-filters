package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.IPaintable;
import com.latmod.mods.itemfilters.filters.AlwaysTrueItemFilter;
import com.latmod.mods.itemfilters.filters.FilterBase;
import com.latmod.mods.itemfilters.net.ItemFiltersNetHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;

@Mod(
		modid = ItemFilters.MOD_ID,
		name = ItemFilters.MOD_NAME,
		version = ItemFilters.VERSION,
		dependencies = "after:forestry"
)
public class ItemFilters
{
	public static final String MOD_ID = "itemfilters";
	public static final String MOD_NAME = "Item Filters";
	public static final String VERSION = "0.0.0.itemfilters";

	@Mod.Instance(MOD_ID)
	public static ItemFilters INSTANCE;

	@SidedProxy(serverSide = "com.latmod.mods.itemfilters.ItemFiltersCommon", clientSide = "com.latmod.mods.itemfilters.client.ItemFiltersClient")
	public static ItemFiltersCommon PROXY;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		CapabilityManager.INSTANCE.register(IItemFilter.class, new Capability.IStorage<IItemFilter>()
		{
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IItemFilter> capability, IItemFilter instance, EnumFacing side)
			{
				return instance instanceof INBTSerializable ? ((INBTSerializable) instance).serializeNBT() : null;
			}

			@Override
			public void readNBT(Capability<IItemFilter> capability, IItemFilter instance, EnumFacing side, NBTBase nbt)
			{
				if (nbt != null && instance instanceof INBTSerializable)
				{
					((INBTSerializable) instance).deserializeNBT(nbt);
				}
			}
		}, () -> AlwaysTrueItemFilter.INSTANCE);

		CapabilityManager.INSTANCE.register(IPaintable.class, new Capability.IStorage<IPaintable>()
		{
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IPaintable> capability, IPaintable instance, EnumFacing side)
			{
				return instance instanceof INBTSerializable ? ((INBTSerializable) instance).serializeNBT() : null;
			}

			@Override
			public void readNBT(Capability<IPaintable> capability, IPaintable instance, EnumFacing side, NBTBase nbt)
			{
				if (nbt != null && instance instanceof INBTSerializable)
				{
					((INBTSerializable) instance).deserializeNBT(nbt);
				}
			}
		}, () -> new IPaintable()
		{
			@Override
			public void paint(IBlockState paint, EnumFacing facing, boolean all)
			{
			}

			@Override
			public IBlockState getPaint()
			{
				return Blocks.AIR.getDefaultState();
			}
		});

		ItemFiltersNetHandler.init();
		FilterBase.register();
	}
}