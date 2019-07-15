package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.IPaintable;
import com.latmod.mods.itemfilters.client.ItemFiltersClient;
import com.latmod.mods.itemfilters.filters.AlwaysTrueItemFilter;
import com.latmod.mods.itemfilters.filters.FilterBase;
import com.latmod.mods.itemfilters.net.ItemFiltersNetHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod(ItemFilters.MOD_ID)
public class ItemFilters
{
	public static final String MOD_ID = "itemfilters";

	public static ItemFiltersCommon PROXY;

	public ItemFilters()
	{
		PROXY = DistExecutor.runForDist(() -> ItemFiltersClient::new, () -> ItemFiltersCommon::new);

		ItemFiltersNetHandler.init();
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, ItemFiltersEventHandler::registerItems);
		FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<FMLLoadCompleteEvent>) event -> {
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
					}, () -> AlwaysTrueItemFilter.INSTANCE);

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
					}, () -> new IPaintable()
					{
						@Override
						public void paint(BlockState paint, Direction facing, boolean all)
						{
						}

						@Override
						public BlockState getPaint()
						{
							return Blocks.AIR.getDefaultState();
						}
					});
					FilterBase.register();
				}
		);
	}
}