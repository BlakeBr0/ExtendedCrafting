package com.blakebr0.extendedcrafting.handler;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.extendedcrafting.init.ModItems;
import net.minecraftforge.client.event.ColorHandlerEvent;

public final class ColorHandler {
    public void onItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register(new IColored.ItemColors(), ModItems.SINGULARITY.get());
    }
}
