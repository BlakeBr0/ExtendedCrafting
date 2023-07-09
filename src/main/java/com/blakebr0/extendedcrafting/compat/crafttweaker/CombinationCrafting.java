package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
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
import net.minecraftforge.server.ServerLifecycleHooks;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.extendedcrafting.CombinationCrafting")
@ZenRegister
public final class CombinationCrafting {
	@ZenCodeType.Method
	public static void addRecipe(String id, IItemStack output, int cost, IIngredient[] inputs) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				var recipe = new CombinationRecipe(new ResourceLocation("crafttweaker", id), toIngredientsList(inputs), output.getInternal(), cost);
				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Combination Crafting recipe for " + output.getCommandString();
			}

			@Override
			public String systemName() {
				return ExtendedCrafting.MOD_ID;
			}
		});
	}

	@ZenCodeType.Method
	public static void addRecipe(String id, IItemStack output, int cost, IIngredient[] inputs, int perTick) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				var recipe = new CombinationRecipe(new ResourceLocation("crafttweaker", id), toIngredientsList(inputs), output.getInternal(), cost, perTick);
				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Combination Crafting recipe for " + output.getCommandString();
			}

			@Override
			public String systemName() {
				return ExtendedCrafting.MOD_ID;
			}
		});
	}

	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				var access = ServerLifecycleHooks.getCurrentServer().registryAccess();
				var recipes = RecipeHelper.getRecipes()
                        .getOrDefault(ModRecipeTypes.COMBINATION.get(), new HashMap<>())
                        .values().stream()
                        .filter(r -> r.getResultItem(access).sameItem(stack.getInternal()))
                        .map(Recipe::getId)
                        .toList();

				recipes.forEach(r -> {
					RecipeHelper.getRecipes().get(ModRecipeTypes.COMBINATION.get()).remove(r);
				});
			}

			@Override
			public String describe() {
				return "Removing Combination Crafting recipes for " + stack.getCommandString();
			}

			@Override
			public String systemName() {
				return ExtendedCrafting.MOD_ID;
			}
		});
	}

	private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
		return Arrays.stream(ingredients)
				.map(IIngredient::asVanillaIngredient)
				.collect(Collectors.toCollection(NonNullList::create));
	}
}
