package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessEnderCrafterRecipe;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.extendedcrafting.EnderCrafting")
@ZenRegister
public final class EnderCrafting implements IRecipeManager {
	@ZenCodeType.Method
	public static void addShaped(String id, IItemStack output, IIngredient[][] inputs) {
		addShaped(id, output, inputs, ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get());
	}

	@ZenCodeType.Method
	public static void addShaped(String id, IItemStack output, IIngredient[][] inputs, int time) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				int height = inputs.length;
				int width = 0;
				for (IIngredient[] row : inputs) {
					if (width < row.length) {
						width = row.length;
					}
				}

				NonNullList<Ingredient> ingredients = NonNullList.withSize(height * width, Ingredient.EMPTY);

				for (int a = 0; a < height; a++) {
					for (int b = 0; b < inputs[a].length; b++) {
						Ingredient ing = inputs[a][b].asVanillaIngredient();
						int i = a * width + b;
						ingredients.set(i, ing);
					}
				}

				ShapedEnderCrafterRecipe recipe = new ShapedEnderCrafterRecipe(new ResourceLocation("crafttweaker", id), width, height, ingredients, output.getInternal(), time);
				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Shaped Ender Crafting recipe for " + output.getCommandString();
			}
		});
	}

	@ZenCodeType.Method
	public static void addShapeless(String id, IItemStack output, IIngredient[] inputs) {
		addShapeless(id, output, inputs, ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get());
	}

	@ZenCodeType.Method
	public static void addShapeless(String id, IItemStack output, IIngredient[] inputs, int time) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				ShapelessEnderCrafterRecipe recipe = new ShapelessEnderCrafterRecipe(new ResourceLocation("crafttweaker", id), toIngredientsList(inputs), output.getInternal(), time);
				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Shapeless Ender Crafter recipe for " + output.getCommandString();
			}
		});
	}

	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				List<ResourceLocation> recipes = RecipeHelper.getRecipes()
						.getOrDefault(RecipeTypes.ENDER_CRAFTER, new HashMap<>())
						.values().stream()
						.filter(r -> r.getResultItem().sameItem(stack.getInternal()))
						.map(IRecipe::getId)
						.collect(Collectors.toList());

				recipes.forEach(r -> {
					RecipeHelper.getRecipes().get(RecipeTypes.ENDER_CRAFTER).remove(r);
				});
			}

			@Override
			public String describe() {
				return "Removing Ender Crafter recipes for " + stack.getCommandString();
			}
		});
	}

	@Override
	public IRecipeType<?> getRecipeType() {
		return RecipeTypes.ENDER_CRAFTER;
	}

	private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
		return Arrays.stream(ingredients)
				.map(IIngredient::asVanillaIngredient)
				.collect(Collectors.toCollection(NonNullList::create));
	}
}