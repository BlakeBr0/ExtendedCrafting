package com.blakebr0.extendedcrafting.compat.crafttweaker;

import java.util.Arrays;
import java.util.List;

import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.extendedcrafting.CompressionCrafting")
public class CompressionCrafting {

	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input, int inputCount, IItemStack catalyst, int powerCost) {
		CraftTweakerAPI.apply(new Add(new CompressorRecipe(toStack(output), toStack(input), inputCount, toStack(catalyst), false, powerCost)));
	}
	
	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input, int inputCount, IItemStack catalyst, int powerCost, int powerRate) {
		CraftTweakerAPI.apply(new Add(new CompressorRecipe(toStack(output), toStack(input), inputCount, toStack(catalyst), false, powerCost, powerRate)));
	}

	@ZenMethod
	public static void remove(IItemStack target) {
		CraftTweakerAPI.apply(new Remove(toStack(target)));
	}

	private static class Add implements IAction {
		CompressorRecipe recipe;

		public Add(CompressorRecipe add) {
			this.recipe = add;
		}

		@Override
		public void apply() {
			CompressorRecipeManager.getInstance().getRecipes().add(this.recipe);
		}

		@Override
		public String describe() {
			return "Adding a Compression Crafting recipe for " + this.recipe.getOutput().getDisplayName();
		}
	}

	private static class Remove implements IAction {
		ItemStack remove;

		public Remove(ItemStack remove) {
			this.remove = remove;
		}

		@Override
		public void apply() {
			CompressorRecipeManager.getInstance().removeRecipes(this.remove);
		}

		@Override
		public String describe() {
			return "Removing all Compression Crafting recipes for " + this.remove.getDisplayName();
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
				return OreDictionary.getOres(toString((IOreDictEntry) ingredient));
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