package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.extendedcrafting.CombinationCrafting")
@ZenRegister
public final class CombinationCrafting implements IRecipeManager<ICombinationRecipe> {
	@Override
	public RecipeType<ICombinationRecipe> getRecipeType() {
		return ModRecipeTypes.COMBINATION.get();
	}

	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient input, IIngredient[] inputs, int cost) {
		addRecipe(name, output, input, inputs, cost, ModConfigs.CRAFTING_CORE_POWER_RATE.get());
	}

	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient input, IIngredient[] inputs, int cost, int perTick) {
		var id = CraftTweakerConstants.rl(this.fixRecipeName(name));
		var recipe = new CombinationRecipe(input.asVanillaIngredient(), toIngredientsList(inputs), output.getInternal(), cost, perTick);

        recipe.setTransformer((slot, stack) -> slot == 0
                ? input.getRemainingItem(new MCItemStack(stack)).getInternal()
                : inputs[slot - 1].getRemainingItem(new MCItemStack(stack)).getInternal());

		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new RecipeHolder<>(id, recipe)));
	}

	private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
		return Arrays.stream(ingredients)
				.map(IIngredient::asVanillaIngredient)
				.collect(Collectors.toCollection(NonNullList::create));
	}
}
