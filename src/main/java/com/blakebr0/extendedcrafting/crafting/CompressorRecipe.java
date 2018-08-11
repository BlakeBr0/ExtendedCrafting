package com.blakebr0.extendedcrafting.crafting;

import java.util.List;

import com.blakebr0.extendedcrafting.config.ModConfig;

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
	protected int powerRate;
	
	public CompressorRecipe(ItemStack output, Object input, int inputCount, ItemStack catalyst, boolean consumeCatalyst, int powerCost) {
		this(output, input, inputCount, catalyst, consumeCatalyst, powerCost, ModConfig.confCompressorRFRate);
	}

	public CompressorRecipe(ItemStack output, Object input, int inputCount, ItemStack catalyst, boolean consumeCatalyst, int powerCost, int powerRate) {
		this.output = output.copy();
		
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
		this.catalyst = catalyst.copy();
		this.consumeCatalyst = consumeCatalyst;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
	}

	public ItemStack getOutput() {
		return this.output;
	}

	public Object getInput() {
		return this.input;
	}

	public int getInputCount() {
		return this.inputCount;
	}

	public ItemStack getCatalyst() {
		return this.catalyst;
	}

	public boolean consumeCatalyst() {
		return this.consumeCatalyst;
	}

	public int getPowerCost() {
		return this.powerCost;
	}
	
	public int getPowerRate() {
		return this.powerRate;
	}
}
