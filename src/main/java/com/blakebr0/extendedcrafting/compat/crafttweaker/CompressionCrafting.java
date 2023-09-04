package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("mods.extendedcrafting.CompressionCrafting")
@ZenRegister
public final class CompressionCrafting implements IRecipeManager<ICompressorRecipe> {
	private static final CompressionCrafting INSTANCE = new CompressionCrafting();

	@Override
	public RecipeType<ICompressorRecipe> getRecipeType() {
		return ModRecipeTypes.COMPRESSOR.get();
	}

	@ZenCodeType.Method
	public static void addRecipe(String name, IIngredient input, IItemStack output, int inputCount, IIngredient catalyst, int powerCost) {
		var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
		var recipe = new CompressorRecipe(id, input.asVanillaIngredient(), output.getInternal(), inputCount, catalyst.asVanillaIngredient(), powerCost);

		CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
	}

	@ZenCodeType.Method
	public static void addRecipe(String name, IIngredient input, IItemStack output, int inputCount, IIngredient catalyst, int powerCost, int powerRate) {
		var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
		var recipe = new CompressorRecipe(id, input.asVanillaIngredient(), output.getInternal(), inputCount, catalyst.asVanillaIngredient(), powerCost, powerRate);

		CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
	}

	@ZenCodeType.Method
	public static void remove(IItemStack stack) {
		CraftTweakerAPI.apply(new ActionRemoveRecipe<>(INSTANCE, recipe -> recipe.getResultItem(RegistryAccess.EMPTY).is(stack.getInternal().getItem())));
	}
}