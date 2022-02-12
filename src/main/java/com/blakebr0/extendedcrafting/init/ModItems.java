package com.blakebr0.extendedcrafting.init;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.item.BaseShinyItem;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.item.HandheldTableItem;
import com.blakebr0.extendedcrafting.item.RecipeMakerItem;
import com.blakebr0.extendedcrafting.item.SingularityItem;
import com.blakebr0.extendedcrafting.item.UltimateSingularityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.blakebr0.extendedcrafting.ExtendedCrafting.CREATIVE_TAB;

public final class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ExtendedCrafting.MOD_ID);

	// register block items here for class load order purposes
	static {
		ModBlocks.BLOCK_ITEMS.forEach(REGISTRY::register);
	}

	public static final RegistryObject<Item> LUMINESSENCE = register("luminessence");
	public static final RegistryObject<Item> BLACK_IRON_INGOT = register("black_iron_ingot");
	public static final RegistryObject<Item> REDSTONE_INGOT = register("redstone_ingot");
	public static final RegistryObject<Item> ENDER_INGOT = register("ender_ingot");
	public static final RegistryObject<Item> ENHANCED_ENDER_INGOT = register("enhanced_ender_ingot", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_INGOT = register("crystaltine_ingot", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_INGOT = register("the_ultimate_ingot", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BLACK_IRON_NUGGET = register("black_iron_nugget");
	public static final RegistryObject<Item> REDSTONE_NUGGET = register("redstone_nugget");
	public static final RegistryObject<Item> ENDER_NUGGET = register("ender_nugget");
	public static final RegistryObject<Item> ENHANCED_ENDER_NUGGET = register("enhanced_ender_nugget", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_NUGGET = register("crystaltine_nugget", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_NUGGET = register("the_ultimate_nugget", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BLACK_IRON_SLATE = register("black_iron_slate");
	public static final RegistryObject<Item> BASIC_CATALYST = register("basic_catalyst");
	public static final RegistryObject<Item> ADVANCED_CATALYST = register("advanced_catalyst");
	public static final RegistryObject<Item> ELITE_CATALYST = register("elite_catalyst");
	public static final RegistryObject<Item> ULTIMATE_CATALYST = register("ultimate_catalyst");
	public static final RegistryObject<Item> REDSTONE_CATALYST = register("redstone_catalyst");
	public static final RegistryObject<Item> ENDER_CATALYST = register("ender_catalyst");
	public static final RegistryObject<Item> ENHANCED_ENDER_CATALYST = register("enhanced_ender_catalyst", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_CATALYST = register("crystaltine_catalyst", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_CATALYST = register("the_ultimate_catalyst", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BASIC_COMPONENT = register("basic_component");
	public static final RegistryObject<Item> ADVANCED_COMPONENT = register("advanced_component");
	public static final RegistryObject<Item> ELITE_COMPONENT = register("elite_component");
	public static final RegistryObject<Item> ULTIMATE_COMPONENT = register("ultimate_component");
	public static final RegistryObject<Item> REDSTONE_COMPONENT = register("redstone_component");
	public static final RegistryObject<Item> ENDER_COMPONENT = register("ender_component");
	public static final RegistryObject<Item> ENHANCED_ENDER_COMPONENT = register("enhanced_ender_component", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRYSTALTINE_COMPONENT = register("crystaltine_component", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THE_ULTIMATE_COMPONENT = register("the_ultimate_component", () -> new BaseItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> ENDER_STAR = register("ender_star", () -> new BaseShinyItem(p -> p.tab(CREATIVE_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HANDHELD_TABLE = register("handheld_table", () -> new HandheldTableItem(p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> RECIPE_MAKER = register("recipe_maker", () -> new RecipeMakerItem(p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> SINGULARITY = register("singularity", () -> new SingularityItem(p -> p.tab(CREATIVE_TAB)));
	public static final RegistryObject<Item> ULTIMATE_SINGULARITY = register("ultimate_singularity", () -> new UltimateSingularityItem(p -> p.tab(CREATIVE_TAB)));

	private static RegistryObject<Item> register(String name) {
		return register(name, () -> new BaseItem(p -> p.tab(CREATIVE_TAB)));
	}

	private static RegistryObject<Item> register(String name, Supplier<Item> item) {
		return REGISTRY.register(name, item);
	}
}
