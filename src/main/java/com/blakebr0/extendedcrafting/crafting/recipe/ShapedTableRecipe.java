package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.crafting.ModRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ShapedTableRecipe implements ISpecialRecipe, ITableRecipe {
	private final ResourceLocation id;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int width;
	private final int height;
	private final int tier;

	public ShapedTableRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> inputs, ItemStack output) {
		this(id, width, height, inputs, output, 0);
	}

	public ShapedTableRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> inputs, ItemStack output, int tier) {
		this.id = id;
		this.inputs = inputs;
		this.output = output;
		this.width = width;
		this.height = height;
		this.tier = tier;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inventory) {
		return this.output.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public boolean matches(IInventory inv, World world) {
		if (this.tier != 0 && this.tier != this.getTierFromGridSize(inv))
			return false;

		for (int x = 0; x <= inv.getWidth() - this.width; x++) {
			for (int y = 0; y <= inv.getHeight() - this.height; y++) {
				if (this.checkMatch(inv, x, y, false)) {
					return true;
				}

				if (this.mirrored && this.checkMatch(inv, x, y, true)) {
					return true;
				}
			}
		}

		return false;
	}

	protected boolean checkMatch(IInventory inv, int startX, int startY, boolean mirror) {
		for (int x = 0; x < inv.getWidth(); x++) {
			for (int y = 0; y < inv.getHeight(); y++) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;

				if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
					if (mirror) {
						target = this.inputs.get(this.width - subX - 1 + subY * this.width);
					} else {
						target = this.inputs.get(subX + subY * this.width);
					}
				}

				if (!target.test(inv.getStackInRowAndColumn(x, y))) {
					return false;
				} else {
					boolean valid = false;
					if (target.getMatchingStacks().length == 0) valid = true;
					
					for (ItemStack stack : target.getMatchingStacks()) {
						if (StackHelper.compareTags(stack, inv.getStackInRowAndColumn(x, y))) {
							valid = true;
						}
					}
					
					if (!valid) return false;
				}
			}
		}

		return true;
	}
	
	@Override
	public boolean matches(IItemHandlerModifiable inv) {
		if (this.tier != 0 && this.tier != this.getTierFromSize(inv.getSlots()))
			return false;

		int size = (int) Math.sqrt(inv.getSlots());
		for (int x = 0; x <= size - this.width; x++) {
			for (int y = 0; y <= size - this.height; y++) {
				if (this.checkMatch(inv, x, y, false)) {
					return true;
				}
			}
		}

		return false;
	}
	
	protected boolean checkMatch(IItemHandlerModifiable inv, int startX, int startY) {
		int size = (int) Math.sqrt(inv.getSlots());
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;

				if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
					target = this.inputs.get(subX + subY * this.width);
				}

				if (!target.test(inv.getStackInSlot(x + y * size))) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.inputs;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SHAPED_TABLE;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeTypes.TABLE;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width >= this.width && height >= this.height;
	}
	
	private int getTierFromSize(int size) {
		int tier = size < 10 ? 1
				 : size < 26 && size > 9 ? 2
				 : size < 50 && size > 25 ? 3
				 : 4;
				 
		return tier;
	}

	private int getTierFromGridSize(InventoryCrafting inv) {
		int size = inv.getSizeInventory();
		int tier = size < 10 ? 1
				 : size < 26 && size > 9 ? 2
				 : size < 50 && size > 25 ? 3
				 : 4;
				 
		return tier;
	}

	@Override
	public int getTier() {
		if (this.tier > 0) return this.tier;

		return this.width < 4 && this.height < 4 ? 1
				 : this.width < 6 && this.height < 6 ? 2
				 : this.width < 8 && this.height < 8 ? 3
				 : 4;
	}
	
	public boolean requiresTier() {
		return this.tier > 0;
	}

	@Override
	public ItemStack getCraftingResult(IItemHandler iItemHandler) {
		return null;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapedTableRecipe> {
		@Override
		public ShapedTableRecipe read(ResourceLocation recipeId, JsonObject json) {
			return null;
		}

		@Override
		public ShapedTableRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return null;
		}

		@Override
		public void write(PacketBuffer buffer, ShapedTableRecipe recipe) {

		}
	}
}