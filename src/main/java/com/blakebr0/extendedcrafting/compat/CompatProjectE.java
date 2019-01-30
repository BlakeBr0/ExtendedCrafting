package com.blakebr0.extendedcrafting.compat;

import java.util.ArrayList;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.google.common.collect.ImmutableMap;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.proxy.IConversionProxy;
import moze_intel.projecte.emc.IngredientMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreIngredient;

public class CompatProjectE {
	static IConversionProxy conversion_proxy;
	
	public static void loadConversions() {
		conversion_proxy = ProjectEAPI.getConversionProxy();
		
		for (CompressorRecipe recipe : CompressorRecipeManager.getInstance().getValidRecipes()) {
			Object input = recipe.getInput();
			ItemStack output = recipe.getOutput();
			
			if (recipe.getInput() instanceof NonNullList<?>) {
				Object obj = new Object();
				
				for (Object ingredient : (NonNullList<?>) recipe.getInput()) {
					conversion_proxy.addConversion(1, obj, ImmutableMap.of(ingredient, 1));
				}
				
				addConversion(output, obj, recipe.getInputCount());
			} else {
				addConversion(output, input, recipe.getInputCount());
			}
		}
		
		for (CombinationRecipe recipe : CombinationRecipeManager.getInstance().getRecipes()) {
			ItemStack output = recipe.getOutput();
			ItemStack input = recipe.getInput();
			
			ArrayList<Object> pedestals = recipe.getPedestalItems();
			
			IngredientMap<Object> ingredients = new IngredientMap<Object>();
			ingredients.addIngredient(input, 1);
			
			for (Object pedestal : pedestals) {
				if (pedestal instanceof NonNullList<?>) {
					Object obj = new Object();
					
					for (Object ingredient : (NonNullList<?>) pedestal) {
						conversion_proxy.addConversion(1, obj, ImmutableMap.of(ingredient, 1));
					}
					
					ingredients.addIngredient(obj, 1);
				} else {
					ingredients.addIngredient(pedestal, 1);
				}
			}
			
			conversion_proxy.addConversion(output.getCount(), output, ImmutableMap.copyOf(ingredients.getMap()));
		}
		
		for (Object obj : EnderCrafterRecipeManager.getInstance().getRecipes()) {
			IRecipe recipe = (IRecipe) obj;
			if (recipe.getRecipeOutput().isEmpty()) {
				continue;
			}

			addConversion(recipe);
		}
		
		for (Object obj : TableRecipeManager.getInstance().getRecipes()) {
			IRecipe recipe = (IRecipe) obj;
			if (recipe.getRecipeOutput().isEmpty()) {
				continue;
			}

			addConversion(recipe);
		}
		
		conversion_proxy.addConversion(1, Blocks.FURNACE, ImmutableMap.of((Object)Blocks.COBBLESTONE, 9));
	}
	
	private static void addConversion(ItemStack output, Object input, int amount) {
		conversion_proxy.addConversion(output.getCount(), output, ImmutableMap.of(input, amount));
	}
	
	private static void addConversion(IRecipe recipe) {
		ItemStack output = recipe.getRecipeOutput();
		IngredientMap<Object> ingredients = new IngredientMap<Object>();
			
		for (Ingredient ingredient : recipe.getIngredients()) {
			if (ingredient == Ingredient.EMPTY) {
				continue;
			}

			ItemStack[] matching = ingredient.getMatchingStacks();
			if (matching .length <= 0)
				continue;
			
			Object obj = new Object();
			for (ItemStack item : matching) {
				conversion_proxy.addConversion(1, obj, ImmutableMap.of(item, 1));
			}
			
			ingredients.addIngredient(obj, 1);
		}
			
		conversion_proxy.addConversion(output.getCount(), output, ingredients.getMap());
	}
}
 