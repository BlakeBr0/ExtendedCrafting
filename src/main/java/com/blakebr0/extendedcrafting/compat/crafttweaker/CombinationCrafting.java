package com.blakebr0.extendedcrafting.compat.crafttweaker;

import java.util.Arrays;
import java.util.List;

import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.extendedcrafting.CombinationCrafting")
public class CombinationCrafting {
	
	@ZenMethod
	public static void addRecipe(IItemStack output, long cost, IItemStack input, IIngredient[] ingredients) {
		CraftTweakerAPI.apply(new Add(new CombinationRecipe(toStack(output), cost, toStack(input), toObjects(ingredients))));
	}

	@ZenMethod
	public static void addRecipe(IItemStack output, long cost, long perTick, IItemStack input, IIngredient[] ingredients) {
		CraftTweakerAPI.apply(new Add(new CombinationRecipe(toStack(output), cost, perTick, toStack(input), toObjects(ingredients))));
	}

	@ZenMethod
	public static void remove(IItemStack target) {
		CraftTweakerAPI.apply(new Remove(toStack(target)));
	}

	private static class Add implements IAction {
		CombinationRecipe recipe;

		public Add(CombinationRecipe add) {
			this.recipe = add;
		}

		@Override
		public void apply() {
			CombinationRecipeManager.getInstance().getRecipes().add(this.recipe);
		}

		@Override
		public String describe() {
			return "Adding a Combination Crafting recipe for " + this.recipe.getOutput().getDisplayName();
		}
	}

	private static class Remove implements IAction {
		ItemStack remove;

		public Remove(ItemStack remove) {
			this.remove = remove;
		}

		@Override
		public void apply() {
			CombinationRecipeManager.getInstance().removeRecipes(this.remove);
		}

		@Override
		public String describe() {
			return "Removing all Combination Crafting recipes for " + this.remove.getDisplayName();
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
			ingredients[x] = toActualObject(list[x]);
		}
		
		return ingredients;
	}

	private static List toList(IIngredient[] list) {
		return Arrays.asList(toObjects(list));
	}

	private static Object toActualObject(IIngredient ingredient) {
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

	private static String toString(IOreDictEntry entry) {
		return ((IOreDictEntry) entry).getName();
	}
}
