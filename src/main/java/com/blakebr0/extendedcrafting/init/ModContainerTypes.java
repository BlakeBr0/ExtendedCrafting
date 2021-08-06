package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.AdvancedAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.AdvancedTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicTableScreen;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.client.screen.CraftingCoreScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EnderCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateTableScreen;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ModContainerTypes {
    public static final List<Supplier<? extends MenuType<?>>> ENTRIES = new ArrayList<>();

    public static final RegistryObject<MenuType<CraftingCoreContainer>> CRAFTING_CORE = register("crafting_core", () -> new MenuType<>((IContainerFactory<CraftingCoreContainer>) CraftingCoreContainer::create));
    public static final RegistryObject<MenuType<BasicTableContainer>> BASIC_TABLE = register("basic_table", () -> new MenuType<>(BasicTableContainer::create));
    public static final RegistryObject<MenuType<AdvancedTableContainer>> ADVANCED_TABLE = register("advanced_table", () -> new MenuType<>(AdvancedTableContainer::create));
    public static final RegistryObject<MenuType<EliteTableContainer>> ELITE_TABLE = register("elite_table", () -> new MenuType<>(EliteTableContainer::create));
    public static final RegistryObject<MenuType<UltimateTableContainer>> ULTIMATE_TABLE = register("ultimate_table", () -> new MenuType<>(UltimateTableContainer::create));
    public static final RegistryObject<MenuType<BasicAutoTableContainer>> BASIC_AUTO_TABLE = register("basic_auto_table", () -> new MenuType<>((IContainerFactory<BasicAutoTableContainer>) BasicAutoTableContainer::create));
    public static final RegistryObject<MenuType<AdvancedAutoTableContainer>> ADVANCED_AUTO_TABLE = register("advanced_auto_table", () -> new MenuType<>((IContainerFactory<AdvancedAutoTableContainer>) AdvancedAutoTableContainer::create));
    public static final RegistryObject<MenuType<EliteAutoTableContainer>> ELITE_AUTO_TABLE = register("elite_auto_table", () -> new MenuType<>((IContainerFactory<EliteAutoTableContainer>) EliteAutoTableContainer::create));
    public static final RegistryObject<MenuType<UltimateAutoTableContainer>> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", () -> new MenuType<>((IContainerFactory<UltimateAutoTableContainer>) UltimateAutoTableContainer::create));
    public static final RegistryObject<MenuType<CompressorContainer>> COMPRESSOR = register("compressor", () -> new MenuType<>((IContainerFactory<CompressorContainer>) CompressorContainer::create));
    public static final RegistryObject<MenuType<EnderCrafterContainer>> ENDER_CRAFTER = register("ender_crafter", () -> new MenuType<>((IContainerFactory<EnderCrafterContainer>) EnderCrafterContainer::create));

    @SubscribeEvent
    public void onRegisterContainerTypes(RegistryEvent.Register<MenuType<?>> event) {
        IForgeRegistry<MenuType<?>> registry = event.getRegistry();

        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup() {
        CRAFTING_CORE.ifPresent(container -> MenuScreens.register(container, CraftingCoreScreen::new));
        BASIC_TABLE.ifPresent(container -> MenuScreens.register(container, BasicTableScreen::new));
        ADVANCED_TABLE.ifPresent(container -> MenuScreens.register(container, AdvancedTableScreen::new));
        ELITE_TABLE.ifPresent(container -> MenuScreens.register(container, EliteTableScreen::new));
        ULTIMATE_TABLE.ifPresent(container -> MenuScreens.register(container, UltimateTableScreen::new));
        BASIC_AUTO_TABLE.ifPresent(container -> MenuScreens.register(container, BasicAutoTableScreen::new));
        ADVANCED_AUTO_TABLE.ifPresent(container -> MenuScreens.register(container, AdvancedAutoTableScreen::new));
        ELITE_AUTO_TABLE.ifPresent(container -> MenuScreens.register(container, EliteAutoTableScreen::new));
        ULTIMATE_AUTO_TABLE.ifPresent(container -> MenuScreens.register(container, UltimateAutoTableScreen::new));
        COMPRESSOR.ifPresent(container -> MenuScreens.register(container, CompressorScreen::new));
        ENDER_CRAFTER.ifPresent(container -> MenuScreens.register(container, EnderCrafterScreen::new));
    }

    private static <T extends MenuType<?>> RegistryObject<T> register(String name, Supplier<? extends MenuType<?>> container) {
        ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
        ENTRIES.add(() -> container.get().setRegistryName(loc));
        return RegistryObject.of(loc, ForgeRegistries.CONTAINERS);
    }
}
