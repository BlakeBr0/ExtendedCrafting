package com.blakebr0.extendedcrafting.client;

import com.blakebr0.extendedcrafting.init.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public final class ModelHandler {
    public static void onClientSetup() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FRAME.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COMPRESSOR.get(), RenderType.cutoutMipped());
    }
}
