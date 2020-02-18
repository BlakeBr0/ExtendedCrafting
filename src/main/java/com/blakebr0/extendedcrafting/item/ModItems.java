package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.item.BaseShinyItem;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.blakebr0.extendedcrafting.ExtendedCrafting.ITEM_GROUP;

public class ModItems {
	public static final List<Supplier<? extends Item>> BLOCK_ENTRIES = new ArrayList<>();
	public static final List<Supplier<? extends Item>> ENTRIES = new ArrayList<>();

	public static final RegistryObject<BaseItem> LUMINESSENCE = register("luminessence");
	public static final RegistryObject<BaseItem> BLACK_IRON_INGOT = register("black_iron_ingot");
	public static final RegistryObject<BaseItem> REDSTONE_INGOT = register("redstone_ingot");
	public static final RegistryObject<BaseItem> ENDER_INGOT = register("ender_ingot");
	public static final RegistryObject<BaseItem> ENHANCED_ENDER_INGOT = register("enhanced_ender_ingot", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> CRYSTALTINE_INGOT = register("crystaltine_ingot", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> THE_ULTIMATE_INGOT = register("the_ultimate_ingot", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<BaseItem> BLACK_IRON_NUGGET = register("black_iron_nugget");
	public static final RegistryObject<BaseItem> REDSTONE_NUGGET = register("redstone_nugget");
	public static final RegistryObject<BaseItem> ENDER_NUGGET = register("ender_nugget");
	public static final RegistryObject<BaseItem> ENHANCED_ENDER_NUGGET = register("enhanced_ender_nugget", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> CRYSTALTINE_NUGGET = register("crystaltine_nugget", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> THE_ULTIMATE_NUGGET = register("the_ultimate_nugget", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<BaseItem> BLACK_IRON_SLATE = register("black_iron_slate");
	public static final RegistryObject<BaseItem> BASIC_CATALYST = register("basic_catalyst");
	public static final RegistryObject<BaseItem> ADVANCED_CATALYST = register("advanced_catalyst");
	public static final RegistryObject<BaseItem> ELITE_CATALYST = register("elite_catalyst");
	public static final RegistryObject<BaseItem> ULTIMATE_CATALYST = register("ultimate_catalyst");
	public static final RegistryObject<BaseItem> REDSTONE_CATALYST = register("redstone_catalyst");
	public static final RegistryObject<BaseItem> ENDER_CATALYST = register("ender_catalyst");
	public static final RegistryObject<BaseItem> ENHANCED_ENDER_CATALYST = register("enhanced_ender_catalyst", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> CRYSTALTINE_CATALYST = register("crystaltine_catalyst", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> THE_ULTIMATE_CATALYST = register("the_ultimate_catalyst", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<BaseItem> BASIC_COMPONENT = register("basic_component");
	public static final RegistryObject<BaseItem> ADVANCED_COMPONENT = register("advanced_component");
	public static final RegistryObject<BaseItem> ELITE_COMPONENT = register("elite_component");
	public static final RegistryObject<BaseItem> ULTIMATE_COMPONENT = register("ultimate_component");
	public static final RegistryObject<BaseItem> REDSTONE_COMPONENT = register("redstone_component");
	public static final RegistryObject<BaseItem> ENDER_COMPONENT = register("ender_component");
	public static final RegistryObject<BaseItem> ENHANCED_ENDER_COMPONENT = register("enhanced_ender_component", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> CRYSTALTINE_COMPONENT = register("crystaltine_component", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BaseItem> THE_ULTIMATE_COMPONENT = register("the_ultimate_component", () -> new BaseItem(p -> p.group(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<BaseItem> ENDER_STAR = register("ender_star", () -> new BaseShinyItem(p -> p.group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<HandheldTableItem> HANDHELD_TABLE = register("handheld_table", () -> new HandheldTableItem(p -> p.group(ITEM_GROUP)));
	public static final RegistryObject<RecipeMakerItem> RECIPE_MAKER = register("recipe_maker", () -> new RecipeMakerItem(p -> p.group(ITEM_GROUP)));
	public static final RegistryObject<SingularityItem> SINGULARITY = register("singularity", () -> new SingularityItem(p -> p.group(ITEM_GROUP)));
	public static final RegistryObject<UltimateSingularityItem> ULTIMATE_SINGULARITY = register("ultimate_singularity", () -> new UltimateSingularityItem(p -> p.group(ITEM_GROUP)));

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		BLOCK_ENTRIES.stream().map(Supplier::get).forEach(registry::register);
		ENTRIES.stream().map(Supplier::get).forEach(registry::register);
	}

	private static <T extends Item> RegistryObject<T> register(String name) {
		return register(name, () -> new BaseItem(p -> p.group(ITEM_GROUP)));
	}

	private static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends Item> item) {
		ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
		ENTRIES.add(() -> item.get().setRegistryName(loc));
		return RegistryObject.of(loc, ForgeRegistries.ITEMS);
	}
}
