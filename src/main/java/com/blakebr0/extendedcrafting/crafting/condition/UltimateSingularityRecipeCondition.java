package com.blakebr0.extendedcrafting.crafting.condition;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class UltimateSingularityRecipeCondition implements ICondition {
    private static final ResourceLocation ID = new ResourceLocation(ExtendedCrafting.MOD_ID, "ultimate_singularity_recipe");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        return ModConfigs.SINGULARITY_ULTIMATE_RECIPE.get();
    }

    public static class Serializer implements IConditionSerializer<UltimateSingularityRecipeCondition> {
        public static final UltimateSingularityRecipeCondition.Serializer INSTANCE = new UltimateSingularityRecipeCondition.Serializer();

        public void write(JsonObject json, UltimateSingularityRecipeCondition value) {

        }

        public UltimateSingularityRecipeCondition read(JsonObject json) {
            return new UltimateSingularityRecipeCondition();
        }

        public ResourceLocation getID() {
            return UltimateSingularityRecipeCondition.ID;
        }
    }
}
