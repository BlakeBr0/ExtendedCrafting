package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.DynamicRecipeManager;
import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ZenCodeType.Name("mods.extendedcrafting.CompressionCrafting")
@ZenRegister
public final class CompressionCrafting {
	@ZenCodeType.Method
	public static void addRecipe(String id, IIngredient input, IItemStack output, int inputCount, IIngredient catalyst, int powerCost) {
		CraftTweakerAPI.apply(new IRuntimeAction() {
			@Override
			public void apply() {
				CompressorRecipe recipe = new CompressorRecipe(new ResourceLocation("crafttweaker", id), input.asVanillaIngredient(), output.getInternal(), inputCount, catalyst.asVanillaIngredient(), powerCost);
				DynamicRecipeManager.getRecipeManager().recipes
						.computeIfAbsent(RecipeTypes.COMPRESSOR, t -> new HashMap<>())
						.put(recipe.getId(), recipe);
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
				DynamicRecipeManager.getRecipeManager().recipes
						.computeIfAbsent(RecipeTypes.COMPRESSOR, t -> new HashMap<>())
						.put(recipe.getId(), recipe);
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
				List<ResourceLocation> recipes = DynamicRecipeManager.getRecipeManager().recipes
						.getOrDefault(RecipeTypes.COMPRESSOR, new HashMap<>())
						.values().stream()
						.filter(r -> r.getRecipeOutput().isItemEqual(stack.getInternal()))
						.map(IRecipe::getId)
						.collect(Collectors.toList());

				recipes.forEach(r -> {
					DynamicRecipeManager.getRecipeManager().recipes.get(RecipeTypes.COMPRESSOR).remove(r);
				});
			}

			@Override
			public String describe() {
				return "Removing Compression Crafting recipes for " + stack.getCommandString();
			}
		});
	}
}