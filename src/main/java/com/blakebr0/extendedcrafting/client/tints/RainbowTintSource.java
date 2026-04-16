package com.blakebr0.extendedcrafting.client.tints;

import com.blakebr0.extendedcrafting.client.handler.TintSourceHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record RainbowTintSource() implements ItemTintSource {
    public static final MapCodec<RainbowTintSource> MAP_CODEC = MapCodec.unit(RainbowTintSource::new);

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        return TintSourceHandler.getCurrentRainbowColor();
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
