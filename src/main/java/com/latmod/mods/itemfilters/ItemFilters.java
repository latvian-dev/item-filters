package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.gui.ItemFiltersGuiHandler;
import com.latmod.mods.itemfilters.item.ItemFiltersItems;
import com.latmod.mods.itemfilters.item.filters.BasicItemFilter;
import com.latmod.mods.itemfilters.net.ItemFiltersNetHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

@Mod(
		modid = ItemFilters.MOD_ID,
		name = ItemFilters.MOD_NAME,
		version = ItemFilters.VERSION
)
public class ItemFilters
{
	public static final String MOD_ID = "itemfilters";
	public static final String MOD_NAME = "Item Filters";
	public static final String VERSION = "0.0.0.itemfilters";

	@Mod.Instance(MOD_ID)
	public static ItemFilters INSTANCE;

	public static final CreativeTabs TAB = new CreativeTabs(ItemFilters.MOD_ID)
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ItemFiltersItems.BASIC);
		}
	};

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
		}, BasicItemFilter::new);

		ItemFiltersNetHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, ItemFiltersGuiHandler.INSTANCE);
	}
}