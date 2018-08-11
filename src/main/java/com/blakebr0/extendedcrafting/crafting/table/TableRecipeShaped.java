package com.blakebr0.extendedcrafting.crafting.table;

import com.blakebr0.cucumber.helper.RecipeHelper;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TableRecipeShaped implements IRecipe, ITieredRecipe {

	public static final int MAX_CRAFT_GRID_WIDTH = 9;
	public static final int MAX_CRAFT_GRID_HEIGHT = 9;

	protected ItemStack output = ItemStack.EMPTY;
	protected NonNullList<Ingredient> input = null;
	protected int width = 0;
	protected int height = 0;
	protected boolean mirrored = true;
	protected ResourceLocation group;
	protected int tier;

	public TableRecipeShaped(int tier, Block result, Object... recipe) {
		this(tier, new ItemStack(result), recipe);
	}

	public TableRecipeShaped(int tier, Item result, Object... recipe) {
		this(tier, new ItemStack(result), recipe);
	}

	public TableRecipeShaped(int tier, ItemStack result, Object... recipe) {
		this(tier, result, CraftingHelper.parseShaped(recipe));
	}

	public TableRecipeShaped(int tier, ItemStack result, ShapedPrimer primer) {
		this.group = RecipeHelper.EMPTY_GROUP;
		this.output = result.copy();
		this.width = primer.width;
		this.height = primer.height;
		this.input = primer.input;
		this.mirrored = primer.mirrored;
		this.tier = tier;
	}

	public TableRecipeShaped(int tier, ItemStack result, int width, int height, NonNullList<Ingredient> ingredients) {
		this.group = RecipeHelper.EMPTY_GROUP;
		this.output = result.copy();
		this.width = width;
		this.height = height;
		this.input = ingredients;
		this.tier = tier;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		return this.output.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
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

	protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		for (int x = 0; x < inv.getWidth(); x++) {
			for (int y = 0; y < inv.getHeight(); y++) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;

				if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
					if (mirror) {
						target = this.input.get(this.width - subX - 1 + subY * this.width);
					} else {
						target = this.input.get(subX + subY * this.width);
					}
				}

				if (!target.apply(inv.getStackInRowAndColumn(x, y))) {
					return false;
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

				if (this.mirrored && this.checkMatch(inv, x, y, true)) {
					return true;
				}
			}
		}

		return false;
	}
	
	protected boolean checkMatch(IItemHandlerModifiable inv, int startX, int startY, boolean mirror) {
		int size = (int) Math.sqrt(inv.getSlots());
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;

				if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
					if (mirror) {
						target = this.input.get(this.width - subX - 1 + subY * this.width);
					} else {
						target = this.input.get(subX + subY * this.width);
					}
				}

				if (!target.apply(inv.getStackInSlot(x + y * size))) {
					return false;
				}
			}
		}

		return true;
	}

	public TableRecipeShaped setMirrored(boolean mirror) {
		this.mirrored = mirror;
		return this;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.input;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	@Override
	public String getGroup() {
		return this.group == null ? "" : this.group.toString();
	}

	@Override
	public boolean canFit(int width, int height) {
		return width >= this.width && height >= this.height;
	}

	@Override
	public IRecipe setRegistryName(ResourceLocation name) {
		return null;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return null;
	}

	@Override
	public Class<IRecipe> getRegistryType() {
		return null;
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
			
		int tier = (this.width < 4 && this.height < 4) ? 1
				 : ((this.width > 3 || this.height > 3) && this.width < 6 && this.height < 6) ? 2
				 : ((this.width > 5 || this.height > 5) && this.width < 8 && this.height < 8) ? 3
				 : 4;
		
		return tier;
	}
	
	public boolean requiresTier() {
		return this.tier > 0;
	}
}