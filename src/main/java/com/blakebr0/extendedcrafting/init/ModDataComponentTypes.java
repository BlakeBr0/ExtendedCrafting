package com.blakebr0.extendedcrafting.init;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.api.component.RecipeMakerComponent;
import com.blakebr0.extendedcrafting.api.component.TableRecipeStorageComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ExtendedCrafting.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RecipeMakerComponent>> RECIPE_MAKER = REGISTRY.register("recipe_maker",
            () -> DataComponentType.<RecipeMakerComponent>builder().persistent(RecipeMakerComponent.CODEC).networkSynchronized(RecipeMakerComponent.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Identifier>> SINGULARITY_ID = REGISTRY.register("singularity_id",
            () -> DataComponentType.<Identifier>builder().persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TableRecipeStorageComponent>> TABLE_RECIPE_STORAGE = REGISTRY.register("table_recipe_storage",
            () -> DataComponentType.<TableRecipeStorageComponent>builder().persistent(TableRecipeStorageComponent.CODEC).networkSynchronized(TableRecipeStorageComponent.STREAM_CODEC).build());
}
