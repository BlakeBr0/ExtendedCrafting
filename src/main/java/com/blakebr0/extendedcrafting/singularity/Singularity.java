package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public class Singularity {
    public static final MapCodec<Singularity> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codec.STRING.fieldOf("name").forGetter(singularity -> singularity.name),
                    Codec.INT.fieldOf("overlay_color").forGetter(singularity -> singularity.overlayColor),
                    Codec.INT.fieldOf("underlay_color").forGetter(singularity -> singularity.underlayColor),
                    Ingredient.CODEC.optionalFieldOf("ingredient").forGetter(singularity -> singularity.ingredient),
                    Codec.INT.optionalFieldOf("ingredient_count", ModConfigs.SINGULARITY_INGREDIENTS_REQUIRED.get()).forGetter(singularity -> singularity.ingredientCount),
                    Codec.BOOL.optionalFieldOf("in_ultimate_singularity", true).forGetter(singularity -> singularity.inUltimateSingularity),
                    Codec.BOOL.optionalFieldOf("enabled", true).forGetter(singularity -> singularity.enabled)
            ).apply(builder, Singularity::new)
    );
    public static final Codec<Singularity> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Singularity> STREAM_CODEC = StreamCodec.of(
            Singularity::toNetwork, Singularity::fromNetwork
    );

    private final String name;
    private final int overlayColor;
    private final int underlayColor;
    private final int ingredientCount;
    private final boolean inUltimateSingularity;
    private final Optional<Ingredient> ingredient;
    private final boolean enabled;

    private Identifier id;

    public Singularity(String name, int overlayColor, int underlayColor, Optional<Ingredient> ingredient, int ingredientCount, boolean inUltimateSingularity, boolean enabled) {
        this.name = name;
        this.overlayColor = ARGB.opaque(overlayColor);
        this.underlayColor = ARGB.opaque(underlayColor);
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
        this.inUltimateSingularity = inUltimateSingularity;
        this.enabled = enabled;
    }

    public Identifier getId() {
        return this.id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getOverlayColor() {
        return this.overlayColor;
    }

    public int getUnderlayColor() {
        return this.underlayColor;
    }

    public Optional<Ingredient> getIngredient() {
        return this.ingredient;
    }

    public int getIngredientCount() {
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

    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeIdentifier(this.id);
        buffer.writeUtf(this.name);
        buffer.writeVarInt(this.overlayColor);
        buffer.writeVarInt(this.underlayColor);
        Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.encode(buffer, this.ingredient);
        buffer.writeVarInt(this.ingredientCount);
        buffer.writeBoolean(this.inUltimateSingularity);
        buffer.writeBoolean(this.enabled);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, Singularity singularity) {
        singularity.write(buffer);
    }

    public static Singularity fromNetwork(RegistryFriendlyByteBuf buffer) {
        var id = buffer.readIdentifier();
        var name = buffer.readUtf();
        var overlayColor = buffer.readVarInt();
        var underlayColor = buffer.readVarInt();
        var ingredient = Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.decode(buffer);
        int ingredientCount = buffer.readVarInt();
        var isInUltimateSingularity = buffer.readBoolean();
        var enabled = buffer.readBoolean();

        var singularity = new Singularity(name, overlayColor, underlayColor, ingredient, ingredientCount, isInUltimateSingularity, enabled);

        singularity.setId(id);

        return singularity;
    }
}
