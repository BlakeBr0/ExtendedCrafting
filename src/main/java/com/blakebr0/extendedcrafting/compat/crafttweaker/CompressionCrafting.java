//package com.blakebr0.extendedcrafting.compat.crafttweaker;
//
//import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
//import com.blakebr0.extendedcrafting.config.ModConfigs;
//import com.blakebr0.extendedcrafting.crafting.recipe.CompressorRecipe;
//import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
//import com.blamejared.crafttweaker.api.CraftTweakerAPI;
//import com.blamejared.crafttweaker.api.CraftTweakerConstants;
//import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
//import com.blamejared.crafttweaker.api.annotation.ZenRegister;
//import com.blamejared.crafttweaker.api.ingredient.IIngredient;
//import com.blamejared.crafttweaker.api.item.IItemStack;
//import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
//import net.minecraft.core.NonNullList;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.item.crafting.RecipeHolder;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.neoforged.neoforge.common.crafting.SizedIngredient;
//import org.openzen.zencode.java.ZenCodeType;
//
//@ZenCodeType.Name("mods.extendedcrafting.CompressionCrafting")
//@ZenRegister
//public final class CompressionCrafting implements IRecipeManager<ICompressorRecipe> {
//	@Override
//	public RecipeType<ICompressorRecipe> getRecipeType() {
//		return ModRecipeTypes.COMPRESSOR.get();
//	}
//
//	@ZenCodeType.Method
//	public void addRecipe(String name, IItemStack output, IIngredient input, int inputCount, IIngredient catalyst, int powerCost) {
//		addRecipe(name, output, input, inputCount, catalyst, powerCost, ModConfigs.COMPRESSOR_POWER_RATE.get());
//	}
//
//	@ZenCodeType.Method
//	public void addRecipe(String name, IItemStack output, IIngredient input, int inputCount, IIngredient catalyst, int powerCost, int powerRate) {
//		var id = CraftTweakerConstants.rl(this.fixRecipeName(name));
//		var recipe = new CompressorRecipe(new SizedIngredient(input.asVanillaIngredient(), inputCount), output.getInternal(), catalyst.asVanillaIngredient(), powerCost, powerRate);
//
//		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, id), recipe)));
//	}
//}