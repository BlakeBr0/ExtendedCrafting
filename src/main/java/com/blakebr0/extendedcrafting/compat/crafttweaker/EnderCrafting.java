package com.blakebr0.extendedcrafting.compat.crafttweaker;

import java.util.Arrays;
import java.util.List;

import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.extendedcrafting.EnderCrafting")
public class EnderCrafting {
	
	@ZenMethod
	public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
		addShaped(output, ingredients, ModConfigs.confEnderTimeRequired);
	}

	@ZenMethod
	public static void addShaped(IItemStack output, IIngredient[][] ingredients, int seconds) {
		int height = ingredients.length;
		int width = 0;
		for (IIngredient[] row : ingredients) {
			if (width < row.length) {
				width = row.length;
			}
		}
			
		NonNullList<Ingredient> input = NonNullList.withSize(height * width, Ingredient.EMPTY);
		
		int i = 0;
		for (int a = 0; a < height; a++) {
			for (int b = 0; b < ingredients[a].length; b++) {
				Ingredient ing = CraftingHelper.getIngredient(toObject(ingredients[a][b]));
				if (ing == null) {
					ing = Ingredient.EMPTY;
				}
				i = a * width + b;
				input.set(i, ing);
			}
		}
		
		TableRecipeShaped recipe = new TableRecipeShaped(1, toStack(output), width, height, input);
		recipe.enderCrafterRecipeTimeRequired = seconds;
		CraftTweakerAPI.apply(new Add(recipe));	
	}
	
	@ZenMethod
	public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
		addShapeless(output, ingredients, ModConfigs.confEnderTimeRequired);
	}

	@ZenMethod
	public static void addShapeless(IItemStack output, IIngredient[] ingredients, int seconds) {
		TableRecipeShapeless recipe = new TableRecipeShapeless(1, toStack(output), toObjects(ingredients));
		recipe.enderCrafterRecipeTimeRequired = seconds;
		CraftTweakerAPI.apply(new Add(recipe));	
	}

	@ZenMethod
	public static void remove(IItemStack target) {
		CraftTweakerAPI.apply(new Remove(toStack(target)));
	}

	private static class Add implements IAction {
		IRecipe recipe;

		public Add(IRecipe add) {
			this.recipe = add;
		}

		@Override
		public void apply() {
			EnderCrafterRecipeManager.getInstance().getRecipes().add(this.recipe);
		}

		@Override
		public String describe() {
			return "Adding an Ender Crafting recipe for " + this.recipe.getRecipeOutput().getDisplayName();
		}
	}

	private static class Remove implements IAction {
		ItemStack remove;

		public Remove(ItemStack remove) {
			this.remove = remove;
		}

		@Override
		public void apply() {
			EnderCrafterRecipeManager.getInstance().removeRecipes(this.remove);
		}

		@Override
		public String describe() {
			return "Removing all Ender Crafting recipes for " + this.remove.getDisplayName();
		}
	}

	private static ItemStack toStack(IItemStack item) {
		if (item == null) {
			return ItemStack.EMPTY;
		} else {
			Object internal = item.getInternal();
			if (internal == null || !(internal instanceof ItemStack)) {
				CraftTweakerAPI.getLogger().logError("Not a valid item stack: " + item);
			}
			
			return (ItemStack) internal;
		}
	}

	private static Object toObject(IIngredient ingredient) {
		if (ingredient == null) {
			return null;
		} else {
			if (ingredient instanceof IOreDictEntry) {
				return toString((IOreDictEntry) ingredient);
			} else if (ingredient instanceof IItemStack) {
				return toStack((IItemStack) ingredient);
			} else {
				return null;
			}
		}
	}

	private static Object[] toObjects(IIngredient[] list) {
		if (list == null)
			return null;
		
		Object[] ingredients = new Object[list.length];
		for (int x = 0; x < list.length; x++) {
			ingredients[x] = toObject(list[x]);
		}
		
		return ingredients;
	}

	private static List toList(IIngredient[] list) {
		return Arrays.asList(toObjects(list));
	}

	private static String toString(IOreDictEntry entry) {
		return ((IOreDictEntry) entry).getName();
	}
}