package com.blakebr0.extendedcrafting.client.tesr.renderer;

import com.blakebr0.extendedcrafting.client.tesr.state.CompressorRenderState;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CompressorRenderer implements BlockEntityRenderer<CompressorTileEntity, CompressorRenderState> {
    private final ItemModelResolver itemModelResolver;

    public CompressorRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CompressorRenderState createRenderState() {
        return new CompressorRenderState();
    }

    @Override
    public void extractRenderState(CompressorTileEntity tile, CompressorRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);

        var recipe = tile.getActiveClientRecipe();
        if (recipe != null) {
            state.itemStack = recipe.assemble(CraftingInput.EMPTY);
        } else {
            state.itemStack = ItemStack.EMPTY;
        }

        int seed = HashCommon.long2int(state.blockPos.asLong());

        this.itemModelResolver.updateForTopItem(state.itemRenderState,  state.itemStack, ItemDisplayContext.FIXED, tile.getLevel(), null, seed);
    }

    @Override
    public void submit(CompressorRenderState state, PoseStack matrix, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!ModConfigs.ENABLE_COMPRESSOR_RENDERER.get())
            return;

        if (!state.itemStack.isEmpty()) {
            matrix.pushPose();
            matrix.translate(0.5D, 1.3D, 0.5D);
            float scale = state.itemStack.getItem() instanceof BlockItem blockItem && !(blockItem.getBlock() instanceof CropBlock) ? 0.55F : 0.35F;
            matrix.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            matrix.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            matrix.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            state.itemRenderState.submit(matrix, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            matrix.popPose();
        }
    }
}
