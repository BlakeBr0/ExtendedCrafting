package com.blakebr0.extendedcrafting.container;

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
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
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

public class ModContainerTypes {
    public static final List<Supplier<? extends ContainerType<?>>> ENTRIES = new ArrayList<>();

    public static final RegistryObject<ContainerType<CraftingCoreContainer>> CRAFTING_CORE = register("crafting_core", () -> new ContainerType<>((IContainerFactory<CraftingCoreContainer>) CraftingCoreContainer::create));
    public static final RegistryObject<ContainerType<BasicTableContainer>> BASIC_TABLE = register("basic_table", () -> new ContainerType<>(BasicTableContainer::create));
    public static final RegistryObject<ContainerType<AdvancedTableContainer>> ADVANCED_TABLE = register("advanced_table", () -> new ContainerType<>(AdvancedTableContainer::create));
    public static final RegistryObject<ContainerType<EliteTableContainer>> ELITE_TABLE = register("elite_table", () -> new ContainerType<>(EliteTableContainer::create));
    public static final RegistryObject<ContainerType<UltimateTableContainer>> ULTIMATE_TABLE = register("ultimate_table", () -> new ContainerType<>(UltimateTableContainer::create));
    public static final RegistryObject<ContainerType<BasicAutoTableContainer>> BASIC_AUTO_TABLE = register("basic_auto_table", () -> new ContainerType<>(BasicAutoTableContainer::create));
    public static final RegistryObject<ContainerType<AdvancedAutoTableContainer>> ADVANCED_AUTO_TABLE = register("advanced_auto_table", () -> new ContainerType<>(AdvancedAutoTableContainer::create));
    public static final RegistryObject<ContainerType<EliteAutoTableContainer>> ELITE_AUTO_TABLE = register("elite_auto_table", () -> new ContainerType<>(EliteAutoTableContainer::create));
    public static final RegistryObject<ContainerType<UltimateAutoTableContainer>> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", () -> new ContainerType<>(UltimateAutoTableContainer::create));
    public static final RegistryObject<ContainerType<CompressorContainer>> COMPRESSOR = register("compressor", () -> new ContainerType<>((IContainerFactory<CompressorContainer>) CompressorContainer::create));
    public static final RegistryObject<ContainerType<EnderCrafterContainer>> ENDER_CRAFTER = register("ender_crafter", () -> new ContainerType<>(EnderCrafterContainer::create));

    @SubscribeEvent
    public void onRegisterContainerTypes(RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup() {
        CRAFTING_CORE.ifPresent(container -> ScreenManager.registerFactory(container, CraftingCoreScreen::new));
        BASIC_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, BasicTableScreen::new));
        ADVANCED_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, AdvancedTableScreen::new));
        ELITE_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, EliteTableScreen::new));
        ULTIMATE_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, UltimateTableScreen::new));
        BASIC_AUTO_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, BasicAutoTableScreen::new));
        ADVANCED_AUTO_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, AdvancedAutoTableScreen::new));
        ELITE_AUTO_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, EliteAutoTableScreen::new));
        ULTIMATE_AUTO_TABLE.ifPresent(container -> ScreenManager.registerFactory(container, UltimateAutoTableScreen::new));
        COMPRESSOR.ifPresent(container -> ScreenManager.registerFactory(container, CompressorScreen::new));
        ENDER_CRAFTER.ifPresent(container -> ScreenManager.registerFactory(container, EnderCrafterScreen::new));
    }

    private static <T extends ContainerType<?>> RegistryObject<T> register(String name, Supplier<? extends ContainerType<?>> container) {
        ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
        ENTRIES.add(() -> container.get().setRegistryName(loc));
        return RegistryObject.of(loc, ForgeRegistries.CONTAINERS);
    }
}
