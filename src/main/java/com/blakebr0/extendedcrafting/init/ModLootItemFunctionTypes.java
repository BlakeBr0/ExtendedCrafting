package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.item.loot.SaveRecipeStorageItemFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModLootItemFunctionTypes {
    public static final DeferredRegister<MapCodec<? extends LootItemFunction>> REGISTRY = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ExtendedCrafting.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends LootItemFunction>, MapCodec<SaveRecipeStorageItemFunction>> SAVE_RECIPE_STORAGE = REGISTRY.register("save_recipe_storage", () -> SaveRecipeStorageItemFunction.MAP_CODEC);
}
