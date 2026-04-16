package com.blakebr0.extendedcrafting.client.handler;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.tints.RainbowTintSource;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

public final class TintSourceHandler {
    @SubscribeEvent
    public void onRegisterBlockTintSources(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(
                List.of(_ -> getCurrentRainbowColor()),
                ModBlocks.THE_ULTIMATE_BLOCK.get()
        );
    }

    @SubscribeEvent
    public void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ExtendedCrafting.resource("rainbow"), RainbowTintSource.MAP_CODEC);
    }

    public static int getCurrentRainbowColor() {
        var hue = (System.currentTimeMillis() % 18000) / 18000F;
        return ColorHelper.hsbToRGB(hue, 1, 1);
    }
}
