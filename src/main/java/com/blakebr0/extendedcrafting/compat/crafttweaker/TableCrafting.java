package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.extendedcrafting.TableCrafting")
@ZenRegister
public final class TableCrafting {
	@ZenCodeType.Method
	public static void addShaped(String id, IItemStack output, IIngredient[][] inputs) {
		addShaped(id, 0, output, inputs);
	}

	@ZenCodeType.Method
	public static void addShaped(String id, int tier, IItemStack output, IIngredient[][] inputs) {
		if (tier > 4 || tier < 0) {
			CraftTweakerAPI.LOGGER.error("Unable to assign a tier to the Table Recipe for stack " + output.getCommandString() + ". Tier cannot be greater than 4 or less than 0.");
		}

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
				Map<Integer, Function<ItemStack, ItemStack>> transformers = new HashMap<>();

				for (int a = 0; a < height; a++) {
					for (int b = 0; b < inputs[a].length; b++) {
						var iing = inputs[a][b];
						var ing = iing.asVanillaIngredient();
						int i = a * width + b;
						ingredients.set(i, ing);

						if (ing != Ingredient.EMPTY) {
							transformers.put(i, stack -> {
								var istack = iing.getRemainingItem(new MCItemStack(stack));
								return istack.getInternal();
							});
						}
					}
				}

				var recipe = new ShapedTableRecipe(new ResourceLocation("crafttweaker", id), width, height, ingredients, output.getInternal(), clampTier(tier));

				recipe.setTransformers(transformers);

				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Shaped Table recipe for " + output.getCommandString();
			}
		});
	}

	@ZenCodeType.Method
	public static void addShapeless(String id, IItemStack output, IIngredient[] inputs) {
		addShapeless(id, 0, output, inputs);
	}

	@ZenCodeType.Method
	public static void addShapeless(String id, int tier, IItemStack output, IIngredient[] inputs) {
		if (tier > 4 || tier < 0) {
			CraftTweakerAPI.LOGGER.error("Unable to assign a tier to the Table Recipe for stack " + output.getCommandString() + ". Tier cannot be greater than 4 or less than 0.");
		}

		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				Map<Integer, Function<ItemStack, ItemStack>> transformers = new HashMap<>();

				for (int i = 0; i < inputs.length; i++) {
					var iing = inputs[i];
					var ing = iing.asVanillaIngredient();

					if (ing != Ingredient.EMPTY) {
						transformers.put(i, stack -> {
							var istack = iing.getRemainingItem(new MCItemStack(stack));
							return istack.getInternal();
						});
					}
				}

				var recipe = new ShapelessTableRecipe(new ResourceLocation("crafttweaker", id), toIngredientsList(inputs), output.getInternal(), clampTier(tier));

				recipe.setTransformers(transformers);

				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Shapeless Table Crafting recipe for " + output.getCommandString();
			}
		});
	}

	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				var recipes = RecipeHelper.getRecipes()
                        .getOrDefault(ModRecipeTypes.TABLE.get(), new HashMap<>())
                        .values().stream()
                        .filter(r -> r.getResultItem().sameItem(stack.getInternal()))
                        .map(Recipe::getId)
                        .toList();

				recipes.forEach(r -> {
					RecipeHelper.getRecipes().get(ModRecipeTypes.TABLE.get()).remove(r);
				});
			}

			@Override
			public String describe() {
				return "Removing Table Crafting recipes for " + stack.getCommandString();
			}
		});
	}

	private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
		return Arrays.stream(ingredients)
				.map(IIngredient::asVanillaIngredient)
				.collect(Collectors.toCollection(NonNullList::create));
	}

	private static int clampTier(int tier) {
		return tier > 4 || tier < 0 ? 0 : tier;
	}
}