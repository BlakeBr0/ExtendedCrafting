package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.cucumber.crafting.TagMapper;
import com.blakebr0.cucumber.lib.Localizable;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class Singularity {
    private final ResourceLocation id;
    private final String name;
    private final int[] colors;
    private final String tag;
    private final int ingredientCount;
    private Ingredient ingredient;

    public Singularity(ResourceLocation id, String name, int[] colors, Ingredient ingredient, int ingredientCount) {
        this.id = id;
        this.name = name;
        this.colors = colors;
        this.ingredient = ingredient;
        this.tag = null;
        this.ingredientCount = ingredientCount;
    }

    public Singularity(ResourceLocation id, String name, int[] colors, String tag, int ingredientCount) {
        this.id = id;
        this.name = name;
        this.colors = colors;
        this.ingredient = Ingredient.EMPTY;
        this.tag = tag;
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
        if (this.tag != null && this.ingredient == Ingredient.EMPTY) {
            Item item = TagMapper.getItemForTag(this.tag);
            this.ingredient = Ingredient.fromItems(item);
        }

        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    public ITextComponent getDisplayName() {
        return Localizable.of(this.name).build();
    }
}
