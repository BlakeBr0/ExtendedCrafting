package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.TableCraftingInput;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.init.ModItems;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class UltimateSingularityRecipe implements ITableRecipe {
    private static final Object2BooleanOpenHashMap<UltimateSingularityRecipe> INGREDIENTS_LOADED = new Object2BooleanOpenHashMap<>();

    public static final MapCodec<UltimateSingularityRecipe> MAP_CODEC = MapCodec.unit(new UltimateSingularityRecipe());
    public static final StreamCodec<RegistryFriendlyByteBuf, UltimateSingularityRecipe> STREAM_CODEC = StreamCodec.unit(new UltimateSingularityRecipe());
    public static final RecipeSerializer<UltimateSingularityRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final List<Ingredient> ingredients;

    public UltimateSingularityRecipe() {
        this.ingredients = new ArrayList<>();
    }

    @Override
    public boolean matches(TableCraftingInput inventory, Level level) {
        // ensure the ingredients list is initialized
        var ingredients = this.getIngredients();

        if (ingredients.isEmpty())
            return false;

        if (inventory.tier() != 4)
            return false;

        if (ingredients.size() != inventory.ingredientCount())
            return false;

        var inputs = NonNullList.<ItemStack>create();

        for (var i = 0; i < inventory.size(); i++) {
            var item = inventory.getItem(i);
            if (!item.isEmpty()) {
                inputs.add(item);
            }
        }

        return RecipeMatcher.findMatches(inputs, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(TableCraftingInput input) {
        return new ItemStack(ModItems.ULTIMATE_SINGULARITY.get());
    }

    @Override
    public RecipeSerializer<UltimateSingularityRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<TableCraftingInput>> getType() {
        return ModRecipeTypes.TABLE.get();
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean hasRequiredTier() {
        return true;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(TableCraftingInput input) {
        return CraftingRecipe.defaultCraftingReminder(input);
    }

    public List<Ingredient> getIngredients() {
        if (!INGREDIENTS_LOADED.getOrDefault(this, false)) {
            this.getIngredients().clear();

            SingularityRegistry.getInstance().getSingularities()
                    .stream()
                    .filter(singularity -> singularity.isInUltimateSingularity() && singularity.getIngredient() != null)
                    .limit(81)
                    .map(SingularityUtils::getItemForSingularity)
                    .map(stack -> DataComponentIngredient.of(false, stack.getComponentsPatch().split().added(), stack.getItem()))
                    .forEach(this.getIngredients()::add);

            INGREDIENTS_LOADED.put(this, true);
        }

        return this.ingredients;
    }

    public static void invalidate() {
        INGREDIENTS_LOADED.clear();
    }
}
