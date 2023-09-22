package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.item.loot.SaveRecipeStorageItemFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModLootItemFunctionTypes {
    public static final DeferredRegister<LootItemFunctionType> REGISTRY = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ExtendedCrafting.MOD_ID);

    public static final RegistryObject<LootItemFunctionType> AUTO_TABLE = REGISTRY.register("save_recipe_storage", () -> new LootItemFunctionType(new SaveRecipeStorageItemFunction.Serializer()));
}
