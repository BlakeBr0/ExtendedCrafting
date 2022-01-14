package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.openzen.zencode.java.ZenCodeType;

import java.util.HashMap;
import java.util.List;

@ZenCodeType.Name("mods.extendedcrafting.CompressionCrafting")
@ZenRegister
public final class CompressionCrafting {
	@ZenCodeType.Method
	public static void addRecipe(String id, IIngredient input, IItemStack output, int inputCount, IIngredient catalyst, int powerCost) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				CompressorRecipe recipe = new CompressorRecipe(new ResourceLocation("crafttweaker", id), input.asVanillaIngredient(), output.getInternal(), inputCount, catalyst.asVanillaIngredient(), powerCost);
				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Compression Crafting recipe for " + output.getCommandString();
			}
		});
	}

	@ZenCodeType.Method
	public static void addRecipe(String id, IIngredient input, IItemStack output, int inputCount, IIngredient catalyst, int powerCost, int powerRate) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				CompressorRecipe recipe = new CompressorRecipe(new ResourceLocation("crafttweaker", id), input.asVanillaIngredient(), output.getInternal(), inputCount, catalyst.asVanillaIngredient(), powerCost, powerRate);
				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Compression Crafting recipe for " + output.getCommandString();
			}
		});
	}

	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				List<ResourceLocation> recipes = RecipeHelper.getRecipes()
                        .getOrDefault(RecipeTypes.COMPRESSOR, new HashMap<>())
                        .values().stream()
                        .filter(r -> r.getResultItem().sameItem(stack.getInternal()))
                        .map(Recipe::getId)
                        .toList();

				recipes.forEach(r -> {
					RecipeHelper.getRecipes().get(RecipeTypes.COMPRESSOR).remove(r);
				});
			}

			@Override
			public String describe() {
				return "Removing Compression Crafting recipes for " + stack.getCommandString();
			}
		});
	}
}