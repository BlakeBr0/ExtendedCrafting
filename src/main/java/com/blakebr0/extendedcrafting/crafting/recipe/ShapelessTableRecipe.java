package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Iterator;

public class ShapelessTableRecipe implements ISpecialRecipe, ITableRecipe {
	private final ResourceLocation id;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int tier;

	public ShapelessTableRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output) {
		this(id, inputs, output, 0);
	}

	public ShapelessTableRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output, int tier) {
		this.id = id;
		this.inputs = inputs;
		this.output = output;
		this.tier = tier;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inventory) {
		return this.output.copy();
	}

	@Override
	public boolean matches(IInventory inventory, World world) {
		NonNullList<Ingredient> required = NonNullList.create();
		required.addAll(this.inputs);

		if (this.tier != 0 && this.tier != this.getTierFromSize(inventory.getSizeInventory()))
			return false;

		for (int x = 0; x < inventory.getSizeInventory(); x++) {
			ItemStack slot = inventory.getStackInSlot(x);

			if (!slot.isEmpty()) {
				boolean inRecipe = false;
				Iterator<Ingredient> req = required.iterator();

				while (req.hasNext()) {
					Ingredient target = req.next();
					if (target.test(slot)) {
						if (target.getMatchingStacks().length == 0) {
							inRecipe = true;
							req.remove();
							break;
						}
						
						for (ItemStack stack : target.getMatchingStacks()) {
							if (StackHelper.compareTags(stack, slot)) {
								inRecipe = true;
								req.remove();
								break;
							}
						}
						
						if (inRecipe) break;
					}
				}

				if (!inRecipe) return false;
			}
		}

		return required.isEmpty();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeTypes.TABLE;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.inputs.size();
	}

	private int getTierFromSize(int size) {
		return size < 10 ? 1
				 : size < 26 ? 2
				 : size < 50 ? 3
				 : 4;
	}

	@Override
	public int getTier() {
		if (this.tier > 0) return this.tier;

		return this.inputs.size() < 10 ? 1
				 : this.inputs.size() < 26 ? 2
				 : this.inputs.size() < 50 ? 3
				 : 4;
	}

	@Override
	public boolean matches(IItemHandler grid) {
		NonNullList<Ingredient> required = NonNullList.create();
		required.addAll(this.inputs);

		if (this.tier != 0 && this.tier != this.getTierFromSize(grid.getSlots()))
			return false;

		for (int x = 0; x < grid.getSlots(); x++) {
			ItemStack slot = grid.getStackInSlot(x);

			if (!slot.isEmpty()) {
				boolean inRecipe = false;
				Iterator<Ingredient> req = required.iterator();

				while (req.hasNext()) {
					if (req.next().test(slot)) {
						inRecipe = true;
						req.remove();
						break;
					}
				}

				if (!inRecipe) return false;
			}
		}

		return required.isEmpty();
	}
	
	public boolean requiresTier() {
		return this.tier > 0;
	}

	@Override
	public ItemStack getCraftingResult(IItemHandler iItemHandler) {
		return null;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessTableRecipe> {
		@Override
		public ShapelessTableRecipe read(ResourceLocation recipeId, JsonObject json) {
			return null;
		}

		@Override
		public ShapelessTableRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return null;
		}

		@Override
		public void write(PacketBuffer buffer, ShapelessTableRecipe recipe) {

		}
	}
}