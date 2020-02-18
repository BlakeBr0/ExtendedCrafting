package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.cucumber.lib.Localizable;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class Singularity {
    private final ResourceLocation id;
    private final String name;
    private final int[] colors;
    private final Ingredient ingredient;
    private final int ingredientCount;

    public Singularity(ResourceLocation id, String name, int[] colors, Ingredient ingredient, int ingredientCount) {
        this.id = id;
        this.name = name;
        this.colors = colors;
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOverlayColor() {
        return this.colors[0];
    }

    public int getUnderlayColor() {
        return this.colors[1];
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    public ITextComponent getDisplayName() {
        return Localizable.of(this.name).build();
    }
}
