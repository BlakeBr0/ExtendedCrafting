package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.cucumber.util.Localizable;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class Singularity {
    private final ResourceLocation id;
    private final String name;
    private final int[] colors;
    private final String tag;
    private final int ingredientCount;
    private final boolean inUltimateSingularity;
    private Ingredient ingredient;

    public Singularity(ResourceLocation id, String name, int[] colors, Ingredient ingredient, int ingredientCount, boolean inUltimateSingularity) {
        this.id = id;
        this.name = name;
        this.colors = colors;
        this.ingredient = ingredient;
        this.tag = null;
        this.ingredientCount = ingredientCount;
        this.inUltimateSingularity = inUltimateSingularity;
    }

    public Singularity(ResourceLocation id, String name, int[] colors, String tag, int ingredientCount, boolean inUltimateSingularity) {
        this.id = id;
        this.name = name;
        this.colors = colors;
        this.ingredient = Ingredient.EMPTY;
        this.tag = tag;
        this.ingredientCount = ingredientCount;
        this.inUltimateSingularity = inUltimateSingularity;
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

    public String getTag() {
        return this.tag;
    }

    public Ingredient getIngredient() {
        if (this.tag != null && this.ingredient == Ingredient.EMPTY) {
            ITag<Item> tag = TagCollectionManager.getManager().getItemTags().get(new ResourceLocation(this.tag));
            if (tag != null) {
                this.ingredient = Ingredient.fromTag(tag);
            }
        }

        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    public ITextComponent getDisplayName() {
        return Localizable.of(this.name).build();
    }

    public boolean isInUltimateSingularity() {
        return this.inUltimateSingularity;
    }
}
