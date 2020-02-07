package com.blakebr0.extendedcrafting.client;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class ModelHandler {
    public static void onClientSetup() {
        RenderTypeLookup.setRenderLayer(ModBlocks.COMPRESSOR.get(), RenderType.cutoutMipped());
    }
}
