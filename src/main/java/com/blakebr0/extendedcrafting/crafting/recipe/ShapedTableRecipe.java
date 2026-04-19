package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ShapedRecipePatternCodecs;
import com.blakebr0.extendedcrafting.api.TableCraftingInput;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.Optional;

public class ShapedTableRecipe implements ITableRecipe {
    public static final MapCodec<ShapedTableRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ShapedRecipePatternCodecs.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    Codec.INT.optionalFieldOf("tier", 0).forGetter(recipe -> recipe.tier)
            ).apply(builder, ShapedTableRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedTableRecipe> STREAM_CODEC = StreamCodec.of(
            ShapedTableRecipe::toNetwork, ShapedTableRecipe::fromNetwork
    );
    public static final RecipeSerializer<ShapedTableRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;
    private final int tier;
    private TriFunction<Integer, Integer, ItemStack, ItemStack> transformer;

    public ShapedTableRecipe(ShapedRecipePattern pattern, ItemStackTemplate result, int tier) {
        this.pattern = pattern;
        this.result = result;
        this.tier = tier;
    }

    @Override
    public boolean matches(TableCraftingInput inventory, Level level) {
        if (this.tier != 0 && this.tier != inventory.tier())
            return false;

        return this.pattern.matches(inventory);
    }

    @Override
    public ItemStack assemble(TableCraftingInput inventory) {
        return this.result.create();
    }

    @Override
    public RecipeSerializer<ShapedTableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<ITableRecipe> getType() {
        return ModRecipeTypes.TABLE.get();
    }

    @Override
    public List<Optional<Ingredient>> getPositionedIngredients() {
        return this.pattern.ingredients();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(TableCraftingInput inventory) {
        var remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < remaining.size(); ++i) {
            var item = inventory.getItem(i);
            var remainder = item.getCraftingRemainder();
            if (remainder != null) {
                remaining.set(i, remainder.create());
            }
        }

        if (this.transformer != null) {
            var width = this.pattern.width();
            var height = this.pattern.height();

            if (inventory.width() != width && inventory.height() != height)
                return remaining;

            if (this.matches(inventory, true)) {
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int index = width - j - 1 + i * width;
                        var stack = inventory.getItem(j, i);

                        remaining.set(index, this.transformer.apply(j, i, stack));
                    }
                }
            } else if (this.matches(inventory, false)) {
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int index = j + i * width;
                        var stack = inventory.getItem(j, i);

                        remaining.set(index, this.transformer.apply(j, i, stack));
                    }
                }
            }
        }

        return remaining;
    }

    @Override
    public int getTier() {
        if (this.tier > 0) return this.tier;

        var width = this.pattern.width();
        var height = this.pattern.height();

        return width < 4 && height < 4 ? 1
                : width < 6 && height < 6 ? 2
                : width < 8 && height < 8 ? 3
                : 4;
    }

    @Override
    public boolean hasRequiredTier() {
        return this.tier > 0;
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    private boolean matches(TableCraftingInput inventory, boolean symmetrical) {
        var width = this.pattern.width();
        var height = this.pattern.height();
        var ingredients = this.pattern.ingredients();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Optional<Ingredient> ingredient;
                if (symmetrical) {
                    ingredient = ingredients.get(width - j - 1 + i * width);
                } else {
                    ingredient = ingredients.get(j + i * width);
                }

                var stack = inventory.getItem(j, i);
                if (ingredient.isEmpty() || !ingredient.get().test(stack)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setTransformer(TriFunction<Integer, Integer, ItemStack, ItemStack> transformer) {
        this.transformer = transformer;
    }

    private static ShapedTableRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
        var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
        int tier = buffer.readVarInt();

        return new ShapedTableRecipe(pattern, result, tier);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapedTableRecipe recipe) {
        ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
        buffer.writeVarInt(recipe.tier);
    }
}