package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.crafting.ISpecialRecipeSerializer;
import com.blakebr0.cucumber.crafting.ISpecialRecipeType;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.SpecialRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class CompressorRecipe implements ISpecialRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int inputCount;
	private final Ingredient catalyst;
	private final int powerCost;
	private final int powerRate;
	
	public CompressorRecipe(ResourceLocation recipeId, Ingredient input, ItemStack output, int inputCount, Ingredient catalyst, int powerCost) {
		this(recipeId, input, output, inputCount, catalyst, powerCost, ModConfig.confCompressorRFRate);
	}

	public CompressorRecipe(ResourceLocation recipeId, Ingredient input, ItemStack output, int inputCount, Ingredient catalyst, int powerCost, int powerRate) {
		this.recipeId = recipeId;
		this.inputs = NonNullList.from(Ingredient.EMPTY, input);
		this.output = output;
		this.inputCount = inputCount;
		this.catalyst = catalyst;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeId;
	}

	@Override
	public ISpecialRecipeSerializer<?> getSerializer() {
		return null;
	}

	@Override
	public ISpecialRecipeType<?> getType() {
		return SpecialRecipeTypes.COMPRESSOR;
	}

	@Override
	public boolean matches(IItemHandler inventory, int startIndex, int endIndex) {
		ItemStack input = inventory.getStackInSlot(0);
		ItemStack catalyst = inventory.getStackInSlot(2);
		return this.inputs.get(0).test(input) && this.catalyst.test(catalyst);
	}

	public int getInputCount() {
		return this.inputCount;
	}

	public Ingredient getCatalyst() {
		return this.catalyst;
	}

	public int getPowerCost() {
		return this.powerCost;
	}
	
	public int getPowerRate() {
		return this.powerRate;
	}
}
