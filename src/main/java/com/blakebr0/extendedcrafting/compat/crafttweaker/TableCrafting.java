package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
			CraftTweakerAPI.logError("Unable to assign a tier to the Table Recipe for stack " + output.getCommandString() + ". Tier cannot be greater than 4 or less than 0.");
		}

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
				Map<Integer, Function<ItemStack, ItemStack>> transformers = new HashMap<>();

				for (int a = 0; a < height; a++) {
					for (int b = 0; b < inputs[a].length; b++) {
						IIngredient iing = inputs[a][b];
						Ingredient ing = iing.asVanillaIngredient();
						int i = a * width + b;
						ingredients.set(i, ing);

						if (ing != Ingredient.EMPTY) {
							transformers.put(i, stack -> {
								IItemStack istack = iing.getRemainingItem(new MCItemStack(stack));
								return istack.getInternal();
							});
						}
					}
				}

				ShapedTableRecipe recipe = new ShapedTableRecipe(new ResourceLocation("crafttweaker", id), width, height, ingredients, output.getInternal(), clampTier(tier));

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
			CraftTweakerAPI.logError("Unable to assign a tier to the Table Recipe for stack " + output.getCommandString() + ". Tier cannot be greater than 4 or less than 0.");
		}

		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				Map<Integer, Function<ItemStack, ItemStack>> transformers = new HashMap<>();

				for (int i = 0; i < inputs.length; i++) {
					IIngredient iing = inputs[i];
					Ingredient ing = iing.asVanillaIngredient();
					if (ing != Ingredient.EMPTY) {
						transformers.put(i, stack -> {
							IItemStack istack = iing.getRemainingItem(new MCItemStack(stack));
							return istack.getInternal();
						});
					}
				}

				ShapelessTableRecipe recipe = new ShapelessTableRecipe(new ResourceLocation("crafttweaker", id), toIngredientsList(inputs), output.getInternal(), clampTier(tier));

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
				List<ResourceLocation> recipes = RecipeHelper.getRecipes()
						.getOrDefault(RecipeTypes.TABLE, new HashMap<>())
						.values().stream()
						.filter(r -> r.getResultItem().sameItem(stack.getInternal()))
						.map(IRecipe::getId)
						.collect(Collectors.toList());

				recipes.forEach(r -> {
					RecipeHelper.getRecipes().get(RecipeTypes.TABLE).remove(r);
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