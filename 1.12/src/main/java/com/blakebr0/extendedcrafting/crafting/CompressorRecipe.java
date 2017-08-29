package com.blakebr0.extendedcrafting.crafting;

import net.minecraft.item.ItemStack;

public class CompressorRecipe {

	protected ItemStack output;
	protected ItemStack input;
	protected int inputCount;
	protected ItemStack catalyst;
	protected boolean consumeCatalyst;
	protected int powerCost;

	public CompressorRecipe(ItemStack output, ItemStack input, int inputCount, ItemStack catalyst,
			boolean consumeCatalyst, int powerCost) {
		this.output = output;
		this.input = input;
		this.inputCount = inputCount;
		this.catalyst = catalyst;
		this.consumeCatalyst = consumeCatalyst;
		this.powerCost = powerCost;
	}

	public ItemStack getOutput() {
		return this.output.copy();
	}

	public ItemStack getInput() {
		return this.input.copy();
	}

	public int getInputCount() {
		return this.inputCount;
	}

	public ItemStack getCatalyst() {
		return this.catalyst.copy();
	}

	public boolean consumeCatalyst() {
		return this.consumeCatalyst;
	}

	public int getPowerCost() {
		return this.powerCost;
	}
}
