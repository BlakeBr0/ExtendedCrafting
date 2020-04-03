package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class SingularityUtils {
    public static Singularity loadFromJson(ResourceLocation id, JsonObject json) {
        String name = JSONUtils.getString(json, "name");
        JsonArray colors = JSONUtils.getJsonArray(json, "colors");
        int materialCount = JSONUtils.getInt(json, "materialCount", ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get());

        int overlayColor = Integer.parseInt(colors.get(0).getAsString(), 16);
        int underlayColor = Integer.parseInt(colors.get(1).getAsString(), 16);

        JsonObject ing = JSONUtils.getJsonObject(json, "ingredient");
        if (ing.has("tag")) {
            String tag = ing.get("tag").getAsString();
            return new Singularity(id, name, new int[] { overlayColor, underlayColor }, tag, materialCount);
        } else {
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            return new Singularity(id, name, new int[] { overlayColor, underlayColor }, ingredient, materialCount);
        }
    }

    public static JsonObject writeToJson(Singularity singularity) {
        JsonObject json = new JsonObject();
        json.addProperty("name", singularity.getName());
        JsonArray colors = new JsonArray();
        colors.add(Integer.toString(singularity.getOverlayColor(), 16));
        colors.add(Integer.toString(singularity.getUnderlayColor(), 16));
        json.add("colors", colors);
        JsonElement ingredient = singularity.getIngredient().serialize();
        json.add("ingredient", ingredient);

        return json;
    }

    public static CompoundNBT makeTag(Singularity singularity) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", singularity.getId().toString());
        return nbt;
    }

    public static ItemStack getItemForSingularity(Singularity singularity) {
        CompoundNBT nbt = makeTag(singularity);
        ItemStack stack = new ItemStack(ModItems.SINGULARITY.get());
        stack.setTag(nbt);
        return stack;
    }

    public static Singularity getSingularity(ItemStack stack) {
        String id = NBTHelper.getString(stack, "Id");
        if (!id.isEmpty()) {
            return SingularityRegistry.getInstance().getSingularityById(new ResourceLocation(id));
        }

        return null;
    }
}
