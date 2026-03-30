package com.blakebr0.extendedcrafting.item.loot;

import com.blakebr0.extendedcrafting.api.component.TableRecipeStorageComponent;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModDataComponentTypes;
import com.blakebr0.extendedcrafting.init.ModLootItemFunctionTypes;
import com.blakebr0.extendedcrafting.tileentity.AutoEnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoFluxCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class SaveRecipeStorageItemFunction implements LootItemFunction {
    public static final MapCodec<SaveRecipeStorageItemFunction> CODEC = MapCodec.unit(new SaveRecipeStorageItemFunction());

    @Override
    public LootItemFunctionType<SaveRecipeStorageItemFunction> getType() {
        return ModLootItemFunctionTypes.SAVE_RECIPE_STORAGE.get();
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        var tile = context.getParam(LootContextParams.BLOCK_ENTITY);
        var level = context.getLevel();

        if (tile instanceof AutoTableTileEntity table) {
            var storage = table.getRecipeStorage();

            if (storage.hasRecipes()) {
                save(stack, storage, level);
            }
        }

        if (tile instanceof AutoEnderCrafterTileEntity crafter) {
            var storage = crafter.getRecipeStorage();

            if (storage.hasRecipes()) {
                save(stack, storage, level);
            }
        }

        if (tile instanceof AutoFluxCrafterTileEntity crafter) {
            var storage = crafter.getRecipeStorage();

            if (storage.hasRecipes()) {
                save(stack, storage, level);
            }
        }

        return stack;
    }

    private static void save(ItemStack stack, TableRecipeStorage storage, Level level) {
        var count = storage.getRecipeCount();
        var data = storage.serialize(level.registryAccess());
        stack.set(ModDataComponentTypes.TABLE_RECIPE_STORAGE, new TableRecipeStorageComponent(count, data));
    }
}
