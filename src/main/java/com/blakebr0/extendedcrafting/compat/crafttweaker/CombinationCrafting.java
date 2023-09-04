package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
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

@ZenCodeType.Name("mods.extendedcrafting.CombinationCrafting")
@ZenRegister
public final class CombinationCrafting implements IRecipeManager<ICombinationRecipe> {
	private static final CombinationCrafting INSTANCE = new CombinationCrafting();

	@Override
	public RecipeType<ICombinationRecipe> getRecipeType() {
		return ModRecipeTypes.COMBINATION.get();
	}

	@ZenCodeType.Method
	public static void addRecipe(String name, IItemStack output, int cost, IIngredient[] inputs) {
		var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
		var recipe = new CombinationRecipe(id, toIngredientsList(inputs), output.getInternal(), cost);

		CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
	}

	@ZenCodeType.Method
	public static void addRecipe(String name, IItemStack output, int cost, IIngredient[] inputs, int perTick) {
		var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
		var recipe = new CombinationRecipe(id, toIngredientsList(inputs), output.getInternal(), cost, perTick);

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
