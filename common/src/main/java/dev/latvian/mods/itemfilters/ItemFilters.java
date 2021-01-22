package dev.latvian.mods.itemfilters;

import dev.latvian.mods.itemfilters.api.ItemFiltersItems;
import dev.latvian.mods.itemfilters.client.ItemFiltersClient;
import dev.latvian.mods.itemfilters.gui.InventoryFilterContainer;
import dev.latvian.mods.itemfilters.item.ANDFilterItem;
import dev.latvian.mods.itemfilters.item.AlwaysFalseFilterItem;
import dev.latvian.mods.itemfilters.item.AlwaysTrueFilterItem;
import dev.latvian.mods.itemfilters.item.BlockFilterItem;
import dev.latvian.mods.itemfilters.item.DamageFilterItem;
import dev.latvian.mods.itemfilters.item.ItemGroupFilterItem;
import dev.latvian.mods.itemfilters.item.MaxCountFilterItem;
import dev.latvian.mods.itemfilters.item.ModFilterItem;
import dev.latvian.mods.itemfilters.item.NOTFilterItem;
import dev.latvian.mods.itemfilters.item.ORFilterItem;
import dev.latvian.mods.itemfilters.item.RegExFilterItem;
import dev.latvian.mods.itemfilters.item.TagFilterItem;
import dev.latvian.mods.itemfilters.item.XORFilterItem;
import dev.latvian.mods.itemfilters.net.ItemFiltersNet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;

public class ItemFilters
{
	public static final String MOD_ID = "itemfilters";

	public static ItemFilters instance;
	public ItemFiltersCommon proxy;
	public CreativeModeTab group;

	public ItemFilters()
	{
		instance = this;
		proxy = DistExecutor.safeRunForDist(() -> ItemFiltersClient::new, () -> ItemFiltersCommon::new);
		group = new CreativeModeTab(MOD_ID)
		{
			@Override
			@Environment(EnvType.CLIENT)
			public ItemStack createIcon()
			{
				return new ItemStack(ItemFiltersItems.ALWAYS_TRUE);
			}
		};

		ItemFiltersNet.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(proxy::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainers);
	}

	private void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new AlwaysTrueFilterItem().setRegistryName("always_true"),
				new AlwaysFalseFilterItem().setRegistryName("always_false"),
				new ORFilterItem().setRegistryName("or"),
				new ANDFilterItem().setRegistryName("and"),
				new NOTFilterItem().setRegistryName("not"),
				new XORFilterItem().setRegistryName("xor"),
				new TagFilterItem().setRegistryName("tag"),
				new ModFilterItem().setRegistryName("mod"),
				new ItemGroupFilterItem().setRegistryName("item_group"),
				new RegExFilterItem().setRegistryName("id_regex"),
				new DamageFilterItem().setRegistryName("damage"),
				new BlockFilterItem().setRegistryName("block"),
				new MaxCountFilterItem().setRegistryName("max_count")
		);
	}

	private void registerContainers(RegistryEvent.Register<ContainerType<?>> event)
	{
		InventoryFilterContainer.TYPE = new MenuType<>();

		event.getRegistry().register(new ContainerType<>((IContainerFactory<InventoryFilterContainer>) InventoryFilterContainer::new).setRegistryName("inventory_filter"));
	}
}