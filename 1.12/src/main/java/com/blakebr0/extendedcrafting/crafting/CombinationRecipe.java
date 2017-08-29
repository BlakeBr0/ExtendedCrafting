package com.blakebr0.extendedcrafting.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CombinationRecipe {

	protected ItemStack output;
	protected int cost;
	protected int perTick;
	protected ItemStack input;
	protected ArrayList<Object> pedestals = new ArrayList<Object>();

	public CombinationRecipe(ItemStack output, int cost, int perTick, ItemStack input, Object... pedestals) {
		this.output = output;
		this.cost = cost;
		this.perTick = perTick;
		this.input = input;
		for (Object obj : pedestals) {
			if (obj instanceof ItemStack) {
				this.pedestals.add(((ItemStack) obj).copy());
			} else if (obj instanceof Item) {
				this.pedestals.add(new ItemStack((Item) obj));
			} else if (obj instanceof Block) {
				this.pedestals.add(new ItemStack((Block) obj));
			} else if (obj instanceof String) {
				this.pedestals.add(OreDictionary.getOres((String) obj));
			} else if (obj instanceof List) {
				this.pedestals.add(obj);
			} else {
				String ret = "Invalid combination crafting recipe: ";
				for (Object tmp : pedestals) {
					ret += tmp + ", ";
				}
				ret += this.output;
				throw new RuntimeException(ret);
			}
		}
	}

	public ItemStack getOutput() {
		return this.output;
	}

	public int getCost() {
		return this.cost;
	}

	public int getPerTick() {
		return this.perTick;
	}

	public ItemStack getInput() {
		return this.input;
	}

	public ArrayList<Object> getPedestalItems() {
		return this.pedestals;
	}
}
