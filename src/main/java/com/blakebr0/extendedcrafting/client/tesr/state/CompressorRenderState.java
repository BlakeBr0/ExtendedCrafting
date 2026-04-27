package com.blakebr0.extendedcrafting.client.tesr.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

public class CompressorRenderState extends BlockEntityRenderState {
    public ItemStack itemStack;
    public ItemStackRenderState itemRenderState = new ItemStackRenderState();
}
