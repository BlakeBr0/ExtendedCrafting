package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class SingularityUtils {
    public static Singularity loadFromJson(ResourceLocation id, JsonObject json) {
        String name = GsonHelper.getAsString(json, "name");
        JsonArray colors = GsonHelper.getAsJsonArray(json, "colors");
        int materialCount = GsonHelper.getAsInt(json, "materialCount", ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get());

        int overlayColor = Integer.parseInt(colors.get(0).getAsString(), 16);
        int underlayColor = Integer.parseInt(colors.get(1).getAsString(), 16);

        boolean inUltimateSingularity = GsonHelper.getAsBoolean(json, "inUltimateSingularity", true);

        if (!json.has("ingredient")) {
            return new Singularity(id, name, new int[] { overlayColor, underlayColor }, Ingredient.EMPTY, materialCount, inUltimateSingularity);
        }

        JsonObject ing = GsonHelper.getAsJsonObject(json, "ingredient");
        if (ing.has("tag")) {
            String tag = ing.get("tag").getAsString();
            return new Singularity(id, name, new int[] { overlayColor, underlayColor }, tag, materialCount, inUltimateSingularity);
        } else {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            return new Singularity(id, name, new int[] { overlayColor, underlayColor }, ingredient, materialCount, inUltimateSingularity);
        }
    }

    public static JsonObject writeToJson(Singularity singularity) {
        JsonObject json = new JsonObject();
        json.addProperty("name", singularity.getName());
        JsonArray colors = new JsonArray();
        colors.add(Integer.toString(singularity.getOverlayColor(), 16));
        colors.add(Integer.toString(singularity.getUnderlayColor(), 16));
        json.add("colors", colors);

        JsonElement ingredient;
        if (singularity.getTag() != null) {
            JsonObject obj = new JsonObject();
            obj.addProperty("tag", singularity.getTag());
            ingredient = obj;
        } else {
            ingredient = singularity.getIngredient().toJson();
        }
        json.add("ingredient", ingredient);

        return json;
    }

    public static CompoundTag makeTag(Singularity singularity) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Id", singularity.getId().toString());
        return nbt;
    }

    public static ItemStack getItemForSingularity(Singularity singularity) {
        CompoundTag nbt = makeTag(singularity);
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
