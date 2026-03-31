package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.config.ModConfigs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

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

    private boolean loadedIngredient = false;

    public Singularity(Identifier id, String name, int[] colors, @Nullable Ingredient ingredient, int ingredientCount, boolean inUltimateSingularity) {
        this.id = id;
        this.name = name;
        this.colors = Arrays.stream(colors).map(c -> ARGB.color(255, c)).toArray();
        this.ingredient = ingredient;
        this.tag = null;
        this.ingredientCount = ingredientCount;
        this.inUltimateSingularity = inUltimateSingularity;
        this.loadedIngredient = true;
    }

    public Singularity(Identifier id, String name, int[] colors, @Nullable Ingredient ingredient) {
        this(id, name, colors, ingredient, -1, true);
    }

    public Singularity(Identifier id, String name, int[] colors, String tag, int ingredientCount, boolean inUltimateSingularity) {
        this.id = id;
        this.name = name;
        this.colors = Arrays.stream(colors).map(c -> ARGB.color(255, c)).toArray();
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

    public @Nullable Ingredient getIngredient() {
        if (!this.loadedIngredient) {
            var tag = ItemTags.create(Identifier.parse(this.tag));
            var items = BuiltInRegistries.ITEM.getOrThrow(tag);

            if (items.isBound()) {
                this.ingredient = Ingredient.of(items);
            } else {
                this.ingredient = null;
            }

            this.loadedIngredient = true;
        }

        return this.ingredient;
    }

    public int getIngredientCount() {
        if (this.ingredientCount == -1) {
            return ModConfigs.SINGULARITY_MATERIALS_REQUIRED.get();
        }

        return this.ingredientCount;
    }

    public Component getDisplayName() {
        return Component.translatable(this.name);
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
        buffer.writeBoolean(this.ingredient != null);

        if (this.tag != null) {
            buffer.writeUtf(this.tag);
        } else if (this.ingredient != null) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, this.ingredient);
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
        var hasIngredient = buffer.readBoolean();

        String tag = null;
        Ingredient ingredient = null;

        if (isTagIngredient) {
            tag = buffer.readUtf();
        } else if (hasIngredient) {
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
