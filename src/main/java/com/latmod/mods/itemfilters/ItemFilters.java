package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.IPaintable;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.client.ItemFiltersClient;
import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import com.latmod.mods.itemfilters.net.ItemFiltersNetHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

@Mod(ItemFilters.MOD_ID)
public class ItemFilters
{
	public static final String MOD_ID = "itemfilters";

	public static ItemFilters instance;
	public ItemFiltersCommon proxy;
	public ItemGroup group;

	public ItemFilters()
	{
		instance = this;
		//noinspection Convert2MethodRef
		proxy = DistExecutor.runForDist(() -> () -> new ItemFiltersClient(), () -> () -> new ItemFiltersCommon());
		group = new ItemGroup(MOD_ID)
		{
			@Override
			@OnlyIn(Dist.CLIENT)
			public ItemStack createIcon()
			{
				return new ItemStack(ItemFiltersAPI.ALWAYS_TRUE);
			}
		};

		ItemFiltersNetHandler.init();
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, ItemFiltersItems::register);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	private void setup(FMLCommonSetupEvent event)
	{
		CapabilityManager.INSTANCE.register(IItemFilter.class, new Capability.IStorage<IItemFilter>()
		{
			@Nullable
			@Override
			public INBT writeNBT(Capability<IItemFilter> capability, IItemFilter instance, Direction side)
			{
				return instance instanceof INBTSerializable ? ((INBTSerializable) instance).serializeNBT() : null;
			}

			@Override
			public void readNBT(Capability<IItemFilter> capability, IItemFilter instance, Direction side, INBT nbt)
			{
				if (nbt != null && instance instanceof INBTSerializable)
				{
					((INBTSerializable) instance).deserializeNBT(nbt);
				}
			}
		}, () -> null);

		CapabilityManager.INSTANCE.register(IPaintable.class, new Capability.IStorage<IPaintable>()
		{
			@Nullable
			@Override
			public INBT writeNBT(Capability<IPaintable> capability, IPaintable instance, Direction side)
			{
				return instance instanceof INBTSerializable ? ((INBTSerializable) instance).serializeNBT() : null;
			}

			@Override
			public void readNBT(Capability<IPaintable> capability, IPaintable instance, Direction side, INBT nbt)
			{
				if (nbt != null && instance instanceof INBTSerializable)
				{
					((INBTSerializable) instance).deserializeNBT(nbt);
				}
			}
		}, () -> null);
	}
}