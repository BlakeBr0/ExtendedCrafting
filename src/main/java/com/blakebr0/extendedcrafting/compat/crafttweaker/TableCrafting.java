//package com.blakebr0.extendedcrafting.compat.crafttweaker;
//
//import com.blakebr0.extendedcrafting.ExtendedCrafting;
//import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
//import com.blakebr0.extendedcrafting.crafting.recipe.ShapedTableRecipe;
//import com.blakebr0.extendedcrafting.crafting.recipe.ShapelessTableRecipe;
//import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
//import com.blamejared.crafttweaker.api.CraftTweakerAPI;
//import com.blamejared.crafttweaker.api.CraftTweakerConstants;
//import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
//import com.blamejared.crafttweaker.api.annotation.ZenRegister;
//import com.blamejared.crafttweaker.api.ingredient.IIngredient;
//import com.blamejared.crafttweaker.api.item.IItemStack;
//import com.blamejared.crafttweaker.api.item.MCItemStack;
//import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
//import net.minecraft.core.NonNullList;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.RecipeHolder;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.item.crafting.ShapedRecipePattern;
//import org.openzen.zencode.java.ZenCodeType;
//
//import java.util.Arrays;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@ZenCodeType.Name("mods.extendedcrafting.TableCrafting")
//@ZenRegister
//public final class TableCrafting implements IRecipeManager<ITableRecipe> {
//	@Override
//	public RecipeType<ITableRecipe> getRecipeType() {
//		return ModRecipeTypes.TABLE.get();
//	}
//
//	@ZenCodeType.Method
//	public void addShaped(String name, IItemStack output, IIngredient[][] inputs) {
//		addShaped(name, 0, output, inputs);
//	}
//
//	@ZenCodeType.Method
//	public void addShaped(String name, int tier, IItemStack output, IIngredient[][] inputs) {
//		var id = CraftTweakerConstants.rl(this.fixRecipeName(name));
//
//		if (tier > 4 || tier < 0) {
//			tier = 0;
//
//            CraftTweakerAPI.getLogger(ExtendedCrafting.MOD_ID).error("Unable to assign a tier to the shaped Table Recipe for stack {}. Tier cannot be greater than 4 or less than 0.", output.getCommandString());
//		}
//
//		int height = inputs.length;
//		int width = 0;
//		for (var row : inputs) {
//			if (width < row.length) {
//				width = row.length;
//			}
//		}
//
//		var ingredients = NonNullList.withSize(height * width, Ingredient.EMPTY);
//
//		for (int a = 0; a < height; a++) {
//			for (int b = 0; b < inputs[a].length; b++) {
//				var iing = inputs[a][b];
//				var ing = iing.asVanillaIngredient();
//				int i = a * width + b;
//
//				ingredients.set(i, ing);
//			}
//		}
//
//		var pattern = new ShapedRecipePattern(width, height, ingredients, Optional.empty());
//		var recipe = new ShapedTableRecipe(pattern, output.getInternal(), tier);
//
//		recipe.setTransformer((x, y, stack) -> inputs[y][x].getRemainingItem(new MCItemStack(stack)).getInternal());
//
//		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new RecipeHolder<>(id, recipe)));
//	}
//
//	@ZenCodeType.Method
//	public void addShapeless(String name, IItemStack output, IIngredient[] inputs) {
//		addShapeless(name, 0, output, inputs);
//	}
//
//	@ZenCodeType.Method
//	public void addShapeless(String name, int tier, IItemStack output, IIngredient[] inputs) {
//		var id = CraftTweakerConstants.rl(this.fixRecipeName(name));
//
//		if (tier > 4 || tier < 0) {
//			tier = 0;
//
//            CraftTweakerAPI.getLogger(ExtendedCrafting.MOD_ID).error("Unable to assign a tier to the shapeless Table Recipe for stack {}. Tier cannot be greater than 4 or less than 0.", output.getCommandString());
//		}
//
//		var recipe = new ShapelessTableRecipe(toIngredientsList(inputs), output.getInternal(), tier);
//
//		recipe.setTransformer((slot, stack) -> inputs[slot].getRemainingItem(new MCItemStack(stack)).getInternal());
//
//		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new RecipeHolder<>(id, recipe)));
//	}
//
//	private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
//		return Arrays.stream(ingredients)
//				.map(IIngredient::asVanillaIngredient)
//				.collect(Collectors.toCollection(NonNullList::create));
//	}
//}