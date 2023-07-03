package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.openzen.zencode.java.ZenCodeType;

import java.util.HashMap;

@ZenCodeType.Name("mods.extendedcrafting.CompressionCrafting")
@ZenRegister
public final class CompressionCrafting {
	@ZenCodeType.Method
	public static void addRecipe(String id, IIngredient input, IItemStack output, int inputCount, IIngredient catalyst, int powerCost) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				var recipe = new CompressorRecipe(new ResourceLocation("crafttweaker", id), input.asVanillaIngredient(), output.getInternal(), inputCount, catalyst.asVanillaIngredient(), powerCost);

				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Compression Crafting recipe for " + output.getCommandString();
			}

			@Override
			public String systemName() {
				return ExtendedCrafting.MOD_ID;
			}
		});
	}

	@ZenCodeType.Method
	public static void addRecipe(String id, IIngredient input, IItemStack output, int inputCount, IIngredient catalyst, int powerCost, int powerRate) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				var recipe = new CompressorRecipe(new ResourceLocation("crafttweaker", id), input.asVanillaIngredient(), output.getInternal(), inputCount, catalyst.asVanillaIngredient(), powerCost, powerRate);

				RecipeHelper.addRecipe(recipe);
			}

			@Override
			public String describe() {
				return "Adding Compression Crafting recipe for " + output.getCommandString();
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
                        .getOrDefault(ModRecipeTypes.COMPRESSOR.get(), new HashMap<>())
                        .values().stream()
                        .filter(r -> r.getResultItem(access).is(stack.getInternal().getItem()))
                        .map(Recipe::getId)
                        .toList();

				recipes.forEach(r -> {
					RecipeHelper.getRecipes().get(ModRecipeTypes.COMPRESSOR.get()).remove(r);
				});
			}

			@Override
			public String describe() {
				return "Removing Compression Crafting recipes for " + stack.getCommandString();
			}

			@Override
			public String systemName() {
				return ExtendedCrafting.MOD_ID;
			}
		});
	}
}