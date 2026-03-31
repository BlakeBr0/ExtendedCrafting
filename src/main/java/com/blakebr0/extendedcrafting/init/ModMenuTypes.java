package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, ExtendedCrafting.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CraftingCoreContainer>> CRAFTING_CORE = REGISTRY.register("crafting_core", () -> new MenuType<>((IContainerFactory<CraftingCoreContainer>) CraftingCoreContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<BasicTableContainer>> BASIC_TABLE = REGISTRY.register("basic_table", () -> new MenuType<>((IContainerFactory<BasicTableContainer>) BasicTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<AdvancedTableContainer>> ADVANCED_TABLE = REGISTRY.register("advanced_table", () -> new MenuType<>((IContainerFactory<AdvancedTableContainer>) AdvancedTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<EliteTableContainer>> ELITE_TABLE = REGISTRY.register("elite_table", () -> new MenuType<>((IContainerFactory<EliteTableContainer>) EliteTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<UltimateTableContainer>> ULTIMATE_TABLE = REGISTRY.register("ultimate_table", () -> new MenuType<>((IContainerFactory<UltimateTableContainer>) UltimateTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<BasicAutoTableContainer>> BASIC_AUTO_TABLE = REGISTRY.register("basic_auto_table", () -> new MenuType<>((IContainerFactory<BasicAutoTableContainer>) BasicAutoTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<AdvancedAutoTableContainer>> ADVANCED_AUTO_TABLE = REGISTRY.register("advanced_auto_table", () -> new MenuType<>((IContainerFactory<AdvancedAutoTableContainer>) AdvancedAutoTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<EliteAutoTableContainer>> ELITE_AUTO_TABLE = REGISTRY.register("elite_auto_table", () -> new MenuType<>((IContainerFactory<EliteAutoTableContainer>) EliteAutoTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<UltimateAutoTableContainer>> ULTIMATE_AUTO_TABLE = REGISTRY.register("ultimate_auto_table", () -> new MenuType<>((IContainerFactory<UltimateAutoTableContainer>) UltimateAutoTableContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<CompressorContainer>> COMPRESSOR = REGISTRY.register("compressor", () -> new MenuType<>((IContainerFactory<CompressorContainer>) CompressorContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<EnderCrafterContainer>> ENDER_CRAFTER = REGISTRY.register("ender_crafter", () -> new MenuType<>((IContainerFactory<EnderCrafterContainer>) EnderCrafterContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<AutoEnderCrafterContainer>> AUTO_ENDER_CRAFTER = REGISTRY.register("auto_ender_crafter", () -> new MenuType<>((IContainerFactory<AutoEnderCrafterContainer>) AutoEnderCrafterContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<FluxAlternatorContainer>> FLUX_ALTERNATOR = REGISTRY.register("flux_alternator", () -> new MenuType<>((IContainerFactory<FluxAlternatorContainer>) FluxAlternatorContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<FluxCrafterContainer>> FLUX_CRAFTER = REGISTRY.register("flux_crafter", () -> new MenuType<>((IContainerFactory<FluxCrafterContainer>) FluxCrafterContainer::new, FeatureFlagSet.of()));
    public static final DeferredHolder<MenuType<?>, MenuType<AutoFluxCrafterContainer>> AUTO_FLUX_CRAFTER = REGISTRY.register("auto_flux_crafter", () -> new MenuType<>((IContainerFactory<AutoFluxCrafterContainer>) AutoFluxCrafterContainer::new, FeatureFlagSet.of()));
}
