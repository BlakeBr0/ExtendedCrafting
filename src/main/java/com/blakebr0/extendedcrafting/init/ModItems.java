package com.blakebr0.extendedcrafting.init;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.item.BaseShinyItem;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.item.HandheldTableItem;
import com.blakebr0.extendedcrafting.item.RecipeMakerItem;
import com.blakebr0.extendedcrafting.item.SingularityItem;
import com.blakebr0.extendedcrafting.item.UltimateSingularityItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.blakebr0.extendedcrafting.ExtendedCrafting.ITEM_GROUP;

public final class ModItems {
	public static final List<Supplier<Item>> BLOCK_ENTRIES = new ArrayList<>();
	public static final Map<RegistryObject<Item>, Supplier<Item>> ENTRIES = new LinkedHashMap<>();

	public static final RegistryObject<Item> LUMINESSENCE = register("luminessence");
	public static final RegistryObject<Item> BLACK_IRON_INGOT = register("black_iron_ingot");
	public static final RegistryObject<Item> REDSTONE_INGOT = register("redstone_ingot");
	public static final RegistryObject<Item> ENDER_INGOT = register("ender_ingot");
	public static final RegistryObject<Item> ENHANCED_ENDER_INGOT = register("enhanced_ender_ingot", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_INGOT = register("crystaltine_ingot", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_INGOT = register("the_ultimate_ingot", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BLACK_IRON_NUGGET = register("black_iron_nugget");
	public static final RegistryObject<Item> REDSTONE_NUGGET = register("redstone_nugget");
	public static final RegistryObject<Item> ENDER_NUGGET = register("ender_nugget");
	public static final RegistryObject<Item> ENHANCED_ENDER_NUGGET = register("enhanced_ender_nugget", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_NUGGET = register("crystaltine_nugget", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_NUGGET = register("the_ultimate_nugget", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BLACK_IRON_SLATE = register("black_iron_slate");
	public static final RegistryObject<Item> BASIC_CATALYST = register("basic_catalyst");
	public static final RegistryObject<Item> ADVANCED_CATALYST = register("advanced_catalyst");
	public static final RegistryObject<Item> ELITE_CATALYST = register("elite_catalyst");
	public static final RegistryObject<Item> ULTIMATE_CATALYST = register("ultimate_catalyst");
	public static final RegistryObject<Item> REDSTONE_CATALYST = register("redstone_catalyst");
	public static final RegistryObject<Item> ENDER_CATALYST = register("ender_catalyst");
	public static final RegistryObject<Item> ENHANCED_ENDER_CATALYST = register("enhanced_ender_catalyst", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_CATALYST = register("crystaltine_catalyst", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_CATALYST = register("the_ultimate_catalyst", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BASIC_COMPONENT = register("basic_component");
	public static final RegistryObject<Item> ADVANCED_COMPONENT = register("advanced_component");
	public static final RegistryObject<Item> ELITE_COMPONENT = register("elite_component");
	public static final RegistryObject<Item> ULTIMATE_COMPONENT = register("ultimate_component");
	public static final RegistryObject<Item> REDSTONE_COMPONENT = register("redstone_component");
	public static final RegistryObject<Item> ENDER_COMPONENT = register("ender_component");
	public static final RegistryObject<Item> ENHANCED_ENDER_COMPONENT = register("enhanced_ender_component", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_COMPONENT = register("crystaltine_component", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_COMPONENT = register("the_ultimate_component", () -> new BaseItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> ENDER_STAR = register("ender_star", () -> new BaseShinyItem(p -> p.tab(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HANDHELD_TABLE = register("handheld_table", () -> new HandheldTableItem(p -> p.tab(ITEM_GROUP)));
	public static final RegistryObject<Item> RECIPE_MAKER = register("recipe_maker", () -> new RecipeMakerItem(p -> p.tab(ITEM_GROUP)));
	public static final RegistryObject<Item> SINGULARITY = register("singularity", () -> new SingularityItem(p -> p.tab(ITEM_GROUP)));
	public static final RegistryObject<Item> ULTIMATE_SINGULARITY = register("ultimate_singularity", () -> new UltimateSingularityItem(p -> p.tab(ITEM_GROUP)));

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		BLOCK_ENTRIES.stream().map(Supplier::get).forEach(registry::register);
		ENTRIES.forEach((reg, item) -> {
			registry.register(item.get());
			reg.updateReference(registry);
		});
	}

	private static RegistryObject<Item> register(String name) {
		return register(name, () -> new BaseItem(p -> p.tab(ITEM_GROUP)));
	}

	private static RegistryObject<Item> register(String name, Supplier<Item> item) {
		ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
		RegistryObject<Item> reg = RegistryObject.of(loc, ForgeRegistries.ITEMS);
		ENTRIES.put(reg, () -> item.get().setRegistryName(loc));
		return reg;
	}
}
