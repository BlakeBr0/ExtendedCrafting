package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.AdvancedAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.AdvancedTableScreen;
import com.blakebr0.extendedcrafting.client.screen.AutoEnderCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.AutoFluxCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicTableScreen;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.client.screen.CraftingCoreScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EnderCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.FluxAlternatorScreen;
import com.blakebr0.extendedcrafting.client.screen.FluxCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateAutoTableScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateTableScreen;
import com.blakebr0.extendedcrafting.container.AdvancedAutoTableContainer;
import com.blakebr0.extendedcrafting.container.AdvancedTableContainer;
import com.blakebr0.extendedcrafting.container.AutoEnderCrafterContainer;
import com.blakebr0.extendedcrafting.container.AutoFluxCrafterContainer;
import com.blakebr0.extendedcrafting.container.BasicAutoTableContainer;
import com.blakebr0.extendedcrafting.container.BasicTableContainer;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.container.EliteAutoTableContainer;
import com.blakebr0.extendedcrafting.container.EliteTableContainer;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.container.FluxAlternatorContainer;
import com.blakebr0.extendedcrafting.container.FluxCrafterContainer;
import com.blakebr0.extendedcrafting.container.UltimateAutoTableContainer;
import com.blakebr0.extendedcrafting.container.UltimateTableContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModContainerTypes {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExtendedCrafting.MOD_ID);

    public static final RegistryObject<MenuType<CraftingCoreContainer>> CRAFTING_CORE = register("crafting_core", () -> new MenuType<>((IContainerFactory<CraftingCoreContainer>) CraftingCoreContainer::create));
    public static final RegistryObject<MenuType<BasicTableContainer>> BASIC_TABLE = register("basic_table", () -> new MenuType<>((IContainerFactory<BasicTableContainer>) BasicTableContainer::create));
    public static final RegistryObject<MenuType<AdvancedTableContainer>> ADVANCED_TABLE = register("advanced_table", () -> new MenuType<>((IContainerFactory<AdvancedTableContainer>) AdvancedTableContainer::create));
    public static final RegistryObject<MenuType<EliteTableContainer>> ELITE_TABLE = register("elite_table", () -> new MenuType<>((IContainerFactory<EliteTableContainer>) EliteTableContainer::create));
    public static final RegistryObject<MenuType<UltimateTableContainer>> ULTIMATE_TABLE = register("ultimate_table", () -> new MenuType<>((IContainerFactory<UltimateTableContainer>) UltimateTableContainer::create));
    public static final RegistryObject<MenuType<BasicAutoTableContainer>> BASIC_AUTO_TABLE = register("basic_auto_table", () -> new MenuType<>((IContainerFactory<BasicAutoTableContainer>) BasicAutoTableContainer::create));
    public static final RegistryObject<MenuType<AdvancedAutoTableContainer>> ADVANCED_AUTO_TABLE = register("advanced_auto_table", () -> new MenuType<>((IContainerFactory<AdvancedAutoTableContainer>) AdvancedAutoTableContainer::create));
    public static final RegistryObject<MenuType<EliteAutoTableContainer>> ELITE_AUTO_TABLE = register("elite_auto_table", () -> new MenuType<>((IContainerFactory<EliteAutoTableContainer>) EliteAutoTableContainer::create));
    public static final RegistryObject<MenuType<UltimateAutoTableContainer>> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", () -> new MenuType<>((IContainerFactory<UltimateAutoTableContainer>) UltimateAutoTableContainer::create));
    public static final RegistryObject<MenuType<CompressorContainer>> COMPRESSOR = register("compressor", () -> new MenuType<>((IContainerFactory<CompressorContainer>) CompressorContainer::create));
    public static final RegistryObject<MenuType<EnderCrafterContainer>> ENDER_CRAFTER = register("ender_crafter", () -> new MenuType<>((IContainerFactory<EnderCrafterContainer>) EnderCrafterContainer::create));
    public static final RegistryObject<MenuType<AutoEnderCrafterContainer>> AUTO_ENDER_CRAFTER = register("auto_ender_crafter", () -> new MenuType<>((IContainerFactory<AutoEnderCrafterContainer>) AutoEnderCrafterContainer::create));
    public static final RegistryObject<MenuType<FluxAlternatorContainer>> FLUX_ALTERNATOR = register("flux_alternator", () -> new MenuType<>((IContainerFactory<FluxAlternatorContainer>) FluxAlternatorContainer::create));
    public static final RegistryObject<MenuType<FluxCrafterContainer>> FLUX_CRAFTER = register("flux_crafter", () -> new MenuType<>((IContainerFactory<FluxCrafterContainer>) FluxCrafterContainer::create));
    public static final RegistryObject<MenuType<AutoFluxCrafterContainer>> AUTO_FLUX_CRAFTER = register("auto_flux_crafter", () -> new MenuType<>((IContainerFactory<AutoFluxCrafterContainer>) AutoFluxCrafterContainer::create));

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
        AUTO_ENDER_CRAFTER.ifPresent(container -> MenuScreens.register(container, AutoEnderCrafterScreen::new));
        FLUX_ALTERNATOR.ifPresent(container -> MenuScreens.register(container, FluxAlternatorScreen::new));
        FLUX_CRAFTER.ifPresent(container -> MenuScreens.register(container, FluxCrafterScreen::new));
        AUTO_FLUX_CRAFTER.ifPresent(container -> MenuScreens.register(container, AutoFluxCrafterScreen::new));
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, Supplier<? extends MenuType<T>> container) {
        return REGISTRY.register(name, container);
    }
}
