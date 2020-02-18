package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class SingularityUtils {
    public static Singularity loadFromJson(ResourceLocation id, JsonObject json) {
        String name = JSONUtils.getString(json, "name");
        JsonArray colors = JSONUtils.getJsonArray(json, "colors");
        Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
        int materialCount = JSONUtils.getInt(json, "materialCount", ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get());

        return new Singularity(id, name, new int[] { colors.get(0).getAsInt(), colors.get(1).getAsInt() }, ingredient, materialCount);
    }
}
