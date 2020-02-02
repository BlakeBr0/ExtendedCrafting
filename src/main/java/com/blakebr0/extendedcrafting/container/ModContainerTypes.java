package com.blakebr0.extendedcrafting.container;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.AdvancedTableScreen;
import com.blakebr0.extendedcrafting.client.screen.BasicTableScreen;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
import com.blakebr0.extendedcrafting.client.screen.CraftingCoreScreen;
import com.blakebr0.extendedcrafting.client.screen.EliteTableScreen;
import com.blakebr0.extendedcrafting.client.screen.EnderCrafterScreen;
import com.blakebr0.extendedcrafting.client.screen.UltimateTableScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModContainerTypes {
    public static final List<Supplier<? extends ContainerType<?>>> ENTRIES = new ArrayList<>();

    public static final RegistryObject<ContainerType<CraftingCoreContainer>> CRAFTING_CORE = register("crafting_core", () -> new ContainerType<>(CraftingCoreContainer::create));
    public static final RegistryObject<ContainerType<BasicTableContainer>> BASIC_TABLE = register("basic_table", () -> new ContainerType<>(BasicTableContainer::create));
    public static final RegistryObject<ContainerType<AdvancedTableContainer>> ADVANCED_TABLE = register("advanced_table", () -> new ContainerType<>(AdvancedTableContainer::create));
    public static final RegistryObject<ContainerType<EliteTableContainer>> ELITE_TABLE = register("elite_table", () -> new ContainerType<>(EliteTableContainer::create));
    public static final RegistryObject<ContainerType<UltimateTableContainer>> ULTIMATE_TABLE = register("ultimate_table", () -> new ContainerType<>(UltimateTableContainer::create));
    public static final RegistryObject<ContainerType<CompressorContainer>> COMPRESSOR = register("compressor", () -> new ContainerType<>(CompressorContainer::create));
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
        COMPRESSOR.ifPresent(container -> ScreenManager.registerFactory(container, CompressorScreen::new));
        ENDER_CRAFTER.ifPresent(container -> ScreenManager.registerFactory(container, EnderCrafterScreen::new));
    }

    private static <T extends ContainerType<?>> RegistryObject<T> register(String name, Supplier<? extends ContainerType<?>> container) {
        ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
        ENTRIES.add(() -> container.get().setRegistryName(loc));
        return RegistryObject.of(loc, ForgeRegistries.CONTAINERS);
    }
}
