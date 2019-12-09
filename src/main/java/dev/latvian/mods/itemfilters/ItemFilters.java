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
import dev.latvian.mods.itemfilters.item.ItemInventory;
import dev.latvian.mods.itemfilters.item.MaxCountFilterItem;
import dev.latvian.mods.itemfilters.item.ModFilterItem;
import dev.latvian.mods.itemfilters.item.NOTFilterItem;
import dev.latvian.mods.itemfilters.item.ORFilterItem;
import dev.latvian.mods.itemfilters.item.RegExFilterItem;
import dev.latvian.mods.itemfilters.item.StringValueData;
import dev.latvian.mods.itemfilters.item.TagFilterItem;
import dev.latvian.mods.itemfilters.item.XORFilterItem;
import dev.latvian.mods.itemfilters.net.ItemFiltersNetHandler;
import dev.latvian.mods.itemfilters.util.EmptyStorage;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;

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
				return new ItemStack(ItemFiltersItems.ALWAYS_TRUE);
			}
		};

		ItemFiltersNetHandler.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(proxy::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainers);
	}

	private void setup(FMLCommonSetupEvent event)
	{
		CapabilityManager.INSTANCE.register(ItemInventory.class, new EmptyStorage<>(), () -> null);
		CapabilityManager.INSTANCE.register(StringValueData.class, new EmptyStorage<>(), () -> null);
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
		event.getRegistry().register(new ContainerType<>((IContainerFactory<InventoryFilterContainer>) InventoryFilterContainer::new).setRegistryName("inventory_filter"));
	}
}