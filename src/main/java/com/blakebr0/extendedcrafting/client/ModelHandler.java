package com.blakebr0.extendedcrafting.client;

import com.blakebr0.extendedcrafting.init.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public final class ModelHandler {
    public static void onClientSetup() {
        RenderTypeLookup.setRenderLayer(ModBlocks.FRAME.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(ModBlocks.COMPRESSOR.get(), RenderType.getCutoutMipped());
    }
}
