package com.blakebr0.extendedcrafting.client.tesr.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class CraftingCoreRenderState extends BlockEntityRenderState {
    public ItemResource itemResource;
    public ItemStackRenderState itemRenderState = new ItemStackRenderState();
}
