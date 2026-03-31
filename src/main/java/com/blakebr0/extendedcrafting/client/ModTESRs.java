package com.blakebr0.extendedcrafting.client;

import com.blakebr0.extendedcrafting.client.tesr.renderer.CompressorRenderer;
import com.blakebr0.extendedcrafting.client.tesr.renderer.CraftingCoreRenderer;
import com.blakebr0.extendedcrafting.client.tesr.renderer.PedestalRenderer;
import com.blakebr0.extendedcrafting.client.tesr.renderer.TheUltimateBlockRenderer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class ModTESRs {
    @SubscribeEvent
    public void onRegisterBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModTileEntities.THE_ULTIMATE_BLOCK.get(), TheUltimateBlockRenderer::new);
        event.registerBlockEntityRenderer(ModTileEntities.PEDESTAL.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(ModTileEntities.CRAFTING_CORE.get(), CraftingCoreRenderer::new);
        event.registerBlockEntityRenderer(ModTileEntities.COMPRESSOR.get(), CompressorRenderer::new);
    }
}
