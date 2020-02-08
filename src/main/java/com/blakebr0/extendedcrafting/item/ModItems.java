package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	public static final RegistryObject<BaseItem> BLACK_IRON_INGOT = register("black_iron");
	public static final RegistryObject<BaseItem> REDSTONE_INGOT = register("redstone_ingot");
	public static final RegistryObject<BaseItem> ENDER_INGOT = register("ender_ingot");
	public static final RegistryObject<BaseItem> CRYSTALTINE_INGOT = register("crystaltine_ingot");
	public static final RegistryObject<BaseItem> THE_ULTIMATE_INGOT = register("the_ultimate_ingot");
	public static final RegistryObject<BaseItem> BLACK_IRON_NUGGET = register("black_iron_nugget");
	public static final RegistryObject<BaseItem> REDSTONE_NUGGET = register("redstone_nugget");
	public static final RegistryObject<BaseItem> ENDER_NUGGET = register("ender_nugget");
	public static final RegistryObject<BaseItem> CRYSTALTINE_NUGGET = register("crystaltine_nugget");
	public static final RegistryObject<BaseItem> THE_ULTIMATE_NUGGET = register("the_ultimate_nugget");



	public static ItemStack itemBlackIronSlate;
	public static ItemStack itemBlackIronRod;

	public static ItemStack itemBasicCatalyst;
	public static ItemStack itemAdvancedCatalyst;
	public static ItemStack itemEliteCatalyst;
	public static ItemStack itemUltimateCatalyst;
	public static ItemStack itemCrystaltineCatalyst;
	public static ItemStack itemTheUltimateCatalyst;

	public static ItemStack itemBasicComponent;
	public static ItemStack itemAdvancedComponent;
	public static ItemStack itemEliteComponent;
	public static ItemStack itemUltimateComponent;
	public static ItemStack itemCrystaltineComponent;
	public static ItemStack itemTheUltimateComponent;

	public static ItemStack itemEnderStar;
	public static ItemStack itemEnderStarNugget;

	public static ItemStack itemEnhancedEnderIngot;
	public static ItemStack itemEnhancedEnderNugget;

	public static ItemStack itemDiamondNugget;
	public static ItemStack itemEmeraldNugget;
	public static ItemStack itemNetherStarNugget;

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
