package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModDataComponentTypes;
import com.blakebr0.extendedcrafting.init.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class SingularityUtils {
    public static Singularity loadFromJson(Identifier id, JsonObject json) {
        var name = GsonHelper.getAsString(json, "name");
        var colors = GsonHelper.getAsJsonArray(json, "colors");
        int materialCount = GsonHelper.getAsInt(json, "materialCount", ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get());

        int overlayColor = Integer.parseInt(colors.get(0).getAsString(), 16);
        int underlayColor = Integer.parseInt(colors.get(1).getAsString(), 16);

        var inUltimateSingularity = GsonHelper.getAsBoolean(json, "inUltimateSingularity", true);

        Singularity singularity;
        var ing = GsonHelper.getAsJsonObject(json, "ingredient", null);

        if (ing == null) {
            singularity = new Singularity(id, name, new int[] { overlayColor, underlayColor }, (Ingredient) null, materialCount, inUltimateSingularity);
        } else if (ing.has("tag")) {
            var tag = ing.get("tag").getAsString();
            singularity = new Singularity(id, name, new int[] { overlayColor, underlayColor }, tag, materialCount, inUltimateSingularity);
        } else {
            var ingredient = Ingredient.CODEC.decode(JsonOps.INSTANCE, json.get("ingredient")).getOrThrow().getFirst();
            singularity = new Singularity(id, name, new int[] { overlayColor, underlayColor }, ingredient, materialCount, inUltimateSingularity);
        }

        var enabled = GsonHelper.getAsBoolean(json, "enabled", true);

        singularity.setEnabled(enabled);

        return singularity;
    }

    public static JsonObject writeToJson(Singularity singularity) {
        var json = new JsonObject();

        json.addProperty("name", singularity.getName());

        var colors = new JsonArray();

        colors.add(Integer.toHexString(singularity.getOverlayColor() & 0x00FFFFFF));
        colors.add(Integer.toHexString(singularity.getUnderlayColor() & 0x00FFFFFF));

        json.add("colors", colors);

        JsonElement ingredient;
        if (singularity.getTag() != null) {
            var obj = new JsonObject();
            obj.addProperty("tag", singularity.getTag());
            ingredient = obj;
        } else {
            ingredient = Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, singularity.getIngredient()).getOrThrow();
        }

        json.add("ingredient", ingredient);

        if (!singularity.isEnabled()) {
            json.addProperty("enabled", false);
        }

        return json;
    }

    public static ItemStack getItemForSingularity(Singularity singularity) {
        var stack = new ItemStack(ModItems.SINGULARITY.get());
        stack.set(ModDataComponentTypes.SINGULARITY_ID, singularity.getId());
        return stack;
    }

    public static Singularity getSingularity(ItemStack stack) {
        var id = stack.get(ModDataComponentTypes.SINGULARITY_ID);
        if (id != null) {
            return SingularityRegistry.getInstance().getSingularityById(id);
        }

        return null;
    }
}
