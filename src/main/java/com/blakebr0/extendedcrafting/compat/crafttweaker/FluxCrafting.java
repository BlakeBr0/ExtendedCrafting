package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.extendedcrafting.FluxCrafting")
@ZenRegister
public final class FluxCrafting {
    @ZenCodeType.Method
    public static void addShaped(String id, IItemStack output, IIngredient[][] inputs, int powerRequired) {
        addShaped(id, output, inputs, powerRequired, ModConfigs.FLUX_CRAFTER_POWER_RATE.get());
    }

    @ZenCodeType.Method
    public static void addShaped(String id, IItemStack output, IIngredient[][] inputs, int powerRequired, int powerRate) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                int height = inputs.length;
                int width = 0;
                for (var row : inputs) {
                    if (width < row.length) {
                        width = row.length;
                    }
                }

                var ingredients = NonNullList.withSize(height * width, Ingredient.EMPTY);

                for (int a = 0; a < height; a++) {
                    for (int b = 0; b < inputs[a].length; b++) {
                        var ing = inputs[a][b].asVanillaIngredient();
                        int i = a * width + b;
                        ingredients.set(i, ing);
                    }
                }

                var recipe = new ShapedFluxCrafterRecipe(new ResourceLocation("crafttweaker", id), width, height, ingredients, output.getInternal(), powerRequired, powerRate);

                RecipeHelper.addRecipe(recipe);
            }

            @Override
            public String describe() {
                return "Adding Shaped Flux Crafting recipe for " + output.getCommandString();
            }
        });
    }

    @ZenCodeType.Method
    public static void addShapeless(String id, IItemStack output, IIngredient[] inputs, int powerRequired) {
        addShapeless(id, output, inputs, powerRequired, ModConfigs.FLUX_CRAFTER_POWER_RATE.get());
    }

    @ZenCodeType.Method
    public static void addShapeless(String id, IItemStack output, IIngredient[] inputs, int powerRequired, int powerRate) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                var recipe = new ShapelessFluxCrafterRecipe(new ResourceLocation("crafttweaker", id), toIngredientsList(inputs), output.getInternal(), powerRequired, powerRate);

                RecipeHelper.addRecipe(recipe);
            }

            @Override
            public String describe() {
                return "Adding Shapeless Flux Crafter recipe for " + output.getCommandString();
            }
        });
    }

    @ZenCodeType.Method
    public static void remove(IItemStack stack) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                var recipes = RecipeHelper.getRecipes()
                        .getOrDefault(ModRecipeTypes.FLUX_CRAFTER.get(), new HashMap<>())
                        .values().stream()
                        .filter(r -> r.getResultItem().sameItem(stack.getInternal()))
                        .map(Recipe::getId)
                        .toList();

                recipes.forEach(r -> {
                    RecipeHelper.getRecipes().get(ModRecipeTypes.FLUX_CRAFTER.get()).remove(r);
                });
            }

            @Override
            public String describe() {
                return "Removing Flux Crafter recipes for " + stack.getCommandString();
            }
        });
    }

    private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
        return Arrays.stream(ingredients)
                .map(IIngredient::asVanillaIngredient)
                .collect(Collectors.toCollection(NonNullList::create));
    }
}