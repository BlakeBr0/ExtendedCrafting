package com.blakebr0.extendedcrafting.item.loot;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.extendedcrafting.init.ModLootItemFunctionTypes;
import com.blakebr0.extendedcrafting.tileentity.AutoEnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoFluxCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class SaveRecipeStorageItemFunction implements LootItemFunction {
    @Override
    public LootItemFunctionType getType() {
        return ModLootItemFunctionTypes.AUTO_TABLE.get();
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        var tile = context.getParam(LootContextParams.BLOCK_ENTITY);

        if (tile instanceof AutoTableTileEntity table) {
            var storage = table.getRecipeStorage();

            if (storage.hasRecipes()) {
                NBTHelper.setInt(stack, "RecipeCount", storage.getRecipeCount());
                NBTHelper.setTag(stack, "RecipeStorage", storage.serializeNBT());
            }
        }

        if (tile instanceof AutoEnderCrafterTileEntity crafter) {
            var storage = crafter.getRecipeStorage();

            if (storage.hasRecipes()) {
                NBTHelper.setInt(stack, "RecipeCount", storage.getRecipeCount());
                NBTHelper.setTag(stack, "RecipeStorage", storage.serializeNBT());
            }
        }

        if (tile instanceof AutoFluxCrafterTileEntity crafter) {
            var storage = crafter.getRecipeStorage();

            if (storage.hasRecipes()) {
                NBTHelper.setInt(stack, "RecipeCount", storage.getRecipeCount());
                NBTHelper.setTag(stack, "RecipeStorage", storage.serializeNBT());
            }
        }

        return stack;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SaveRecipeStorageItemFunction> {
        @Override
        public void serialize(JsonObject json, SaveRecipeStorageItemFunction function, JsonSerializationContext context) { }

        @Override
        public SaveRecipeStorageItemFunction deserialize(JsonObject json, JsonDeserializationContext context) {
            return new SaveRecipeStorageItemFunction();
        }
    }
}
