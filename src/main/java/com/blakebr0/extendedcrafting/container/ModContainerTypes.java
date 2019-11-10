package com.blakebr0.extendedcrafting.container;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.screen.CompressorScreen;
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
    public static final List<Supplier<ContainerType<?>>> ENTRIES = new ArrayList<>();

    public static final RegistryObject<ContainerType<CompressorContainer>> COMPRESSOR = register("compressor", () -> new ContainerType<>(CompressorContainer::create));

    @SubscribeEvent
    public void onRegisterContainerTypes(RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup() {
        COMPRESSOR.ifPresent(container -> ScreenManager.registerFactory(container, CompressorScreen::new));
    }

    private static <T extends ContainerType<?>> RegistryObject<T> register(String name, Supplier<? extends ContainerType<?>> container) {
        ResourceLocation loc = new ResourceLocation(ExtendedCrafting.MOD_ID, name);
        ENTRIES.add(() -> container.get().setRegistryName(loc));
        return RegistryObject.of(loc, ForgeRegistries.CONTAINERS);
    }
}
