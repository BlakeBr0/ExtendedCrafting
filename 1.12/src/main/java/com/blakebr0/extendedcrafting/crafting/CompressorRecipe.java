package com.blakebr0.extendedcrafting.crafting;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CompressorRecipe {

	protected ItemStack output;
	protected Object input;
	protected int inputCount;
	protected ItemStack catalyst;
	protected boolean consumeCatalyst;
	protected int powerCost;

	public CompressorRecipe(ItemStack output, Object input, int inputCount, ItemStack catalyst, boolean consumeCatalyst, int powerCost) {
		this.output = output;
		if (input instanceof ItemStack) {
			this.input = ((ItemStack) input).copy();
		} else if (input instanceof Item) {
			this.input = new ItemStack((Item) input);
		} else if (input instanceof Block) {
			this.input = new ItemStack((Block) input);
		} else if (input instanceof String) {
			this.input = OreDictionary.getOres((String) input);
		} else if (input instanceof List) {
			this.input = input;
		} else {
			throw new RuntimeException("Invalid compressor recipe input: " + input.toString());
		}
		this.inputCount = inputCount;
		this.catalyst = catalyst;
		this.consumeCatalyst = consumeCatalyst;
		this.powerCost = powerCost;
	}

	public ItemStack getOutput() {
		return this.output.copy();
	}

	public Object getInput() {
		return this.input;
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
