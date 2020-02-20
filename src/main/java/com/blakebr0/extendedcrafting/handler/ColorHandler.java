package com.blakebr0.extendedcrafting.handler;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;

public class ColorHandler {
    public static void onClientSetup() {
        Minecraft minecraft = Minecraft.getInstance();
        ItemColors itemColors = minecraft.getItemColors();

        onItemColors(itemColors);
    }

    private static void onItemColors(ItemColors colors) {
        colors.register(new IColored.ItemColors(), ModItems.SINGULARITY.get());
    }
}
