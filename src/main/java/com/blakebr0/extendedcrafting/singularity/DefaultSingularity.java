package com.blakebr0.extendedcrafting.singularity;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;

public record DefaultSingularity(Identifier id, String name, int overlayColor, int underlayColor, String ingredient) {
    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("name", this.name);
        json.addProperty("overlay_color", this.overlayColor);
        json.addProperty("underlay_color", this.underlayColor);
        json.addProperty("ingredient", this.ingredient);
        return json;
    }
}
