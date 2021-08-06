package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.cucumber.util.Localizable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

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
            Tag<Item> tag = SerializationTags.getInstance().getItems().getTag(new ResourceLocation(this.tag));
            if (tag != null) {
                this.ingredient = Ingredient.of(tag);
            }
        }

        return this.ingredient;
    }

    public int getIngredientCount() {
        return this.ingredientCount;
    }

    public Component getDisplayName() {
        return Localizable.of(this.name).build();
    }

    public boolean isInUltimateSingularity() {
        return this.inUltimateSingularity;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.id);
        buffer.writeUtf(this.name);
        buffer.writeVarIntArray(this.colors);
        buffer.writeBoolean(this.tag != null);

        if (this.tag != null) {
            buffer.writeUtf(this.tag);
        } else {
            this.ingredient.toNetwork(buffer);
        }

        buffer.writeVarInt(this.ingredientCount);
        buffer.writeBoolean(this.inUltimateSingularity);
    }

    public static Singularity read(FriendlyByteBuf buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        String name = buffer.readUtf();
        int[] colors = buffer.readVarIntArray();
        boolean isTagIngredient = buffer.readBoolean();

        String tag = null;
        Ingredient ingredient = Ingredient.EMPTY;

        if (isTagIngredient) {
            tag = buffer.readUtf();
        } else {
            ingredient = Ingredient.fromNetwork(buffer);
        }

        int ingredientCount = buffer.readVarInt();
        boolean isInUltimateSingularity = buffer.readBoolean();

        if (isTagIngredient) {
            return new Singularity(id, name, colors, tag, ingredientCount, isInUltimateSingularity);
        } else {
            return new Singularity(id, name, colors, ingredient, ingredientCount, isInUltimateSingularity);
        }
    }
}
