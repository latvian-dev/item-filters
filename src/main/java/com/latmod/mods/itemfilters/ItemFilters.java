package com.latmod.mods.itemfilters;

import com.latmod.mods.itemfilters.api.IItemFilter;
import com.latmod.mods.itemfilters.api.ItemFiltersAPI;
import com.latmod.mods.itemfilters.filters.ANDFilter;
import com.latmod.mods.itemfilters.filters.AlwaysTrueItemFilter;
import com.latmod.mods.itemfilters.filters.CreativeTabFilter;
import com.latmod.mods.itemfilters.filters.ModFilter;
import com.latmod.mods.itemfilters.filters.NOTFilter;
import com.latmod.mods.itemfilters.filters.ORFilter;
import com.latmod.mods.itemfilters.filters.OreDictionaryFilter;
import com.latmod.mods.itemfilters.filters.XORFilter;
import com.latmod.mods.itemfilters.net.ItemFiltersNetHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

		ItemFiltersNetHandler.init();

		ItemFiltersAPI.register("always_true", () -> AlwaysTrueItemFilter.INSTANCE);
		ItemFiltersAPI.register("or", ORFilter::new);
		ItemFiltersAPI.register("and", ANDFilter::new);
		ItemFiltersAPI.register("not", NOTFilter::new);
		ItemFiltersAPI.register("xor", XORFilter::new);
		ItemFiltersAPI.register("ore", OreDictionaryFilter::new);
		ItemFiltersAPI.register("mod", ModFilter::new);
		ItemFiltersAPI.register("creative_tab", CreativeTabFilter::new);
	}
}