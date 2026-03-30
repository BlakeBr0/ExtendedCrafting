package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Singularity {
    public static final StreamCodec<RegistryFriendlyByteBuf, Singularity> STREAM_CODEC = StreamCodec.of(
            Singularity::encode, Singularity::read
    );

    private final Identifier id;
    private final String name;
    private final int[] colors;
    private final String tag;
    private final int ingredientCount;
    private final boolean inUltimateSingularity;
    @Nullable
    private Ingredient ingredient;
    private boolean enabled = true;

    public Singularity(Identifier id, String name, int[] colors, @Nullable Ingredient ingredient, int ingredientCount, boolean inUltimateSingularity) {
        this.id = id;
        this.name = name;
        this.colors = Arrays.stream(colors).map(c -> FastColor.ARGB32.color(255, c)).toArray();
        this.ingredient = ingredient;
        this.tag = null;
        this.ingredientCount = ingredientCount;
        this.inUltimateSingularity = inUltimateSingularity;
    }

    public Singularity(Identifier id, String name, int[] colors, @Nullable Ingredient ingredient) {
        this(id, name, colors, ingredient, -1, true);
    }

    public Singularity(Identifier id, String name, int[] colors, String tag, int ingredientCount, boolean inUltimateSingularity) {
        this.id = id;
        this.name = name;
        this.colors = Arrays.stream(colors).map(c -> FastColor.ARGB32.color(255, c)).toArray();
        this.ingredient = null;
        this.tag = tag;
        this.ingredientCount = ingredientCount;
        this.inUltimateSingularity = inUltimateSingularity;
    }

    public Singularity(Identifier id, String name, int[] colors, String tag) {
        this(id, name, colors, tag, -1, true);
    }

    public Identifier getId() {
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
        if (this.tag != null && this.ingredient == null) {
            var tag = ItemTags.create(Identifier.parse(this.tag));
            if (BuiltInRegistries.ITEM.getTag(tag).isPresent()) {
                this.ingredient = Ingredient.of(tag);
            } else {
                this.ingredient = Ingredient.EMPTY;
            }
        }

        return this.ingredient != null ? this.ingredient : Ingredient.EMPTY;
    }

    public int getIngredientCount() {
        if (this.ingredientCount == -1) {
            return ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get();
        }

        return this.ingredientCount;
    }

    public Component getDisplayName() {
        return Localizable.of(this.name).build();
    }

    public boolean isInUltimateSingularity() {
        return this.inUltimateSingularity;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeIdentifier(this.id);
        buffer.writeUtf(this.name);
        buffer.writeVarIntArray(this.colors);
        buffer.writeBoolean(this.tag != null);

        if (this.tag != null) {
            buffer.writeUtf(this.tag);
        } else {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, this.ingredient != null ? this.ingredient : Ingredient.EMPTY);
        }

        buffer.writeVarInt(this.getIngredientCount());
        buffer.writeBoolean(this.inUltimateSingularity);
        buffer.writeBoolean(this.enabled);
    }

    public static void encode(RegistryFriendlyByteBuf buffer, Singularity singularity) {
        singularity.write(buffer);
    }

    public static Singularity read(RegistryFriendlyByteBuf buffer) {
        var id = buffer.readIdentifier();
        var name = buffer.readUtf();
        int[] colors = buffer.readVarIntArray();
        var isTagIngredient = buffer.readBoolean();

        String tag = null;
        var ingredient = Ingredient.EMPTY;

        if (isTagIngredient) {
            tag = buffer.readUtf();
        } else {
            ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        }

        int ingredientCount = buffer.readVarInt();
        var isInUltimateSingularity = buffer.readBoolean();

        Singularity singularity;
        if (isTagIngredient) {
            singularity = new Singularity(id, name, colors, tag, ingredientCount, isInUltimateSingularity);
        } else {
            singularity = new Singularity(id, name, colors, ingredient, ingredientCount, isInUltimateSingularity);
        }

        singularity.enabled = buffer.readBoolean();

        return singularity;
    }
}
