package com.blakebr0.extendedcrafting.client.handler;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.init.ModItems;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ColorHandler {
    @SubscribeEvent
    public void onBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, index) -> getCurrentRainbowColor(),
                ModBlocks.THE_ULTIMATE_BLOCK.get()
        );
    }

    @SubscribeEvent
    public void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(new IColored.ItemColors(), ModItems.SINGULARITY.get());
        event.register(
                (stack, index) -> getCurrentRainbowColor(),
                ModBlocks.THE_ULTIMATE_BLOCK.get(),
                ModItems.ULTIMATE_SINGULARITY.get(),
                ModItems.THE_ULTIMATE_INGOT.get(),
                ModItems.THE_ULTIMATE_NUGGET.get()
        );
        event.register(
                (stack, index) -> index == 1 ? getCurrentRainbowColor() : -1,
                ModItems.THE_ULTIMATE_COMPONENT.get(),
                ModItems.THE_ULTIMATE_CATALYST.get()
        );
    }

    public static int getCurrentRainbowColor() {
        var hue = (System.currentTimeMillis() % 18000) / 18000F;
        return ColorHelper.hsbToRGB(hue, 1, 1);
    }
}
