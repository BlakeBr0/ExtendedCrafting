package com.blakebr0.extendedcrafting.client.handler;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.extendedcrafting.init.ModBlocks;
import com.blakebr0.extendedcrafting.init.ModItems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

public final class ColorHandler {
    @SubscribeEvent
    public void onBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(
                List.of(_ -> getCurrentRainbowColor()),
                ModBlocks.THE_ULTIMATE_BLOCK.get()
        );
    }

//    @SubscribeEvent
//    public void onItemColors(RegisterColorHandlersEvent.Item event) {
//        TODO stack colors
//        event.register(new IColored.ItemColors(), ModItems.SINGULARITY.get());
//        event.register(
//                (stack, index) -> getCurrentRainbowColor(),
//                ModBlocks.THE_ULTIMATE_BLOCK.get(),
//                ModItems.ULTIMATE_SINGULARITY.get(),
//                ModItems.THE_ULTIMATE_INGOT.get(),
//                ModItems.THE_ULTIMATE_NUGGET.get()
//        );
//        event.register(
//                (stack, index) -> index == 1 ? getCurrentRainbowColor() : -1,
//                ModItems.THE_ULTIMATE_COMPONENT.get(),
//                ModItems.THE_ULTIMATE_CATALYST.get()
//        );
//    }

    public static int getCurrentRainbowColor() {
        var hue = (System.currentTimeMillis() % 18000) / 18000F;
        return ColorHelper.hsbToRGB(hue, 1, 1);
    }
}
