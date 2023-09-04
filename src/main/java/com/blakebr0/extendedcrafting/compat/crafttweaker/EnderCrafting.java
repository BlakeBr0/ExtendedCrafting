package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapedEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.extendedcrafting.EnderCrafting")
@ZenRegister
public final class EnderCrafting implements IRecipeManager<IEnderCrafterRecipe> {
	private static final EnderCrafting INSTANCE = new EnderCrafting();

	@Override
	public RecipeType<IEnderCrafterRecipe> getRecipeType() {
		return ModRecipeTypes.ENDER_CRAFTER.get();
	}

	@ZenCodeType.Method
	public static void addShaped(String name, IItemStack output, IIngredient[][] inputs) {
		addShaped(name, output, inputs, ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get());
	}

	@ZenCodeType.Method
	public static void addShaped(String name, IItemStack output, IIngredient[][] inputs, int time) {
		var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));

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

		var recipe = new ShapedEnderCrafterRecipe(id, width, height, ingredients, output.getInternal(), time);

		CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
	}

	@ZenCodeType.Method
	public static void addShapeless(String name, IItemStack output, IIngredient[] inputs) {
		addShapeless(name, output, inputs, ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get());
	}

	@ZenCodeType.Method
	public static void addShapeless(String name, IItemStack output, IIngredient[] inputs, int time) {
		var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
		var recipe = new ShapelessEnderCrafterRecipe(id, toIngredientsList(inputs), output.getInternal(), time);

		CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
	}

	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		CraftTweakerAPI.apply(new ActionRemoveRecipe<>(INSTANCE, recipe -> recipe.getResultItem(RegistryAccess.EMPTY).is(stack.getInternal().getItem())));
	}

	private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
		return Arrays.stream(ingredients)
				.map(IIngredient::asVanillaIngredient)
				.collect(Collectors.toCollection(NonNullList::create));
	}
}