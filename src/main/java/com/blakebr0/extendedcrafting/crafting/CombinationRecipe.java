package com.blakebr0.extendedcrafting.crafting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.config.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CombinationRecipe {

	protected ItemStack output;
	protected long cost;
	protected long perTick;
	protected ItemStack input;
	protected ArrayList<Object> pedestals = new ArrayList<>();
	protected ArrayList<String> inputList = new ArrayList<>();
	
	public CombinationRecipe(ItemStack output, long cost, ItemStack input, Object... pedestals) {
		this(output, cost, ModConfig.confCraftingCoreRFRate, input, pedestals);
	}

	public CombinationRecipe(ItemStack output, long cost, long perTick, ItemStack input, Object... pedestals) {
		this.output = output;
		this.cost = cost;
		this.perTick = perTick;
		this.input = input;
		
		Map<String, Integer> ingredients = new LinkedHashMap<>();
		
		ingredients.put(input.getDisplayName(), 1);
		for (Object obj : pedestals) {
			if (obj instanceof ItemStack) {
				ItemStack stack = ((ItemStack) obj).copy();
				this.pedestals.add(stack);
				this.putIngredient(ingredients, stack.getDisplayName());
			} else if (obj instanceof Item) {
				ItemStack stack = new ItemStack((Item) obj);
				this.pedestals.add(stack);
				this.putIngredient(ingredients, stack.getDisplayName());
			} else if (obj instanceof Block) {
				ItemStack stack = new ItemStack((Block) obj);
				this.pedestals.add(stack);
				this.putIngredient(ingredients, stack.getDisplayName());
			} else if (obj instanceof String) {
				String ore = (String) obj;
				this.pedestals.add(OreDictionary.getOres(ore));
				this.putIngredient(ingredients, ore + " (oredict)");
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
		
		this.inputList.add(Utils.localize("tooltip.ec.items_required"));
		ingredients.forEach((s, i) -> this.inputList.add(" " + i + "x " + s));
	}

	public ItemStack getOutput() {
		return this.output;
	}

	public long getCost() {
		return this.cost;
	}

	public long getPerTick() {
		return this.perTick;
	}

	public ItemStack getInput() {
		return this.input;
	}

	public ArrayList<Object> getPedestalItems() {
		return this.pedestals;
	}
	
	public ArrayList<String> getInputList() {
		return this.inputList;
	}
	
	private void putIngredient(Map<String, Integer> ingredients, String name) {
		if (ingredients.containsKey(name)) {
			int current = ingredients.get(name);
			ingredients.replace(name, current + 1);
		} else {
			ingredients.put(name, 1);
		}
	}
}
