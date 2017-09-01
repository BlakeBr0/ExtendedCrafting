package com.blakebr0.extendedcrafting.crafting.table;

import javax.annotation.Nonnull;

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

public class TableRecipeShaped implements IRecipe, ITieredRecipe {

	public static final int MAX_CRAFT_GRID_WIDTH = 9;
	public static final int MAX_CRAFT_GRID_HEIGHT = 9;

	@Nonnull
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

	public TableRecipeShaped(int tier, @Nonnull ItemStack result, Object... recipe) {
		this(tier, result, CraftingHelper.parseShaped(recipe));
	}

	public TableRecipeShaped(int tier, @Nonnull ItemStack result, ShapedPrimer primer) {
		this.group = RecipeHelper.EMPTY_GROUP;
		output = result.copy();
		this.width = primer.width;
		this.height = primer.height;
		this.input = primer.input;
		this.mirrored = primer.mirrored;
		this.tier = tier;
	}

	public TableRecipeShaped(int tier, @Nonnull ItemStack result, int width, int height, NonNullList<Ingredient> ingredients) {
		this.group = RecipeHelper.EMPTY_GROUP;
		output = result.copy();
		this.width = width;
		this.height = height;
		this.input = ingredients;
		this.tier = tier;
	}

	@Override
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		return output.copy();
	}

	@Override
	@Nonnull
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
		if (tier != 0) {
			if (!(this.tier == this.getTierFromGridSize(inv))) {
				return false;
			}
		}

		for (int x = 0; x <= inv.getWidth() - width; x++) {
			for (int y = 0; y <= inv.getHeight() - height; ++y) {
				if (checkMatch(inv, x, y, false)) {
					return true;
				}

				if (mirrored && checkMatch(inv, x, y, true)) {
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

				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
					if (mirror) {
						target = input.get(width - subX - 1 + subY * width);
					} else {
						target = input.get(subX + subY * width);
					}
				}

				if (!target.apply(inv.getStackInRowAndColumn(x, y))) {
					return false;
				}
			}
		}

		return true;
	}

	public TableRecipeShaped setMirrored(boolean mirror) {
		mirrored = mirror;
		return this;
	}

	@Override
	@Nonnull
	public NonNullList<Ingredient> getIngredients() {
		return this.input;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	@Nonnull
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

	private int getTierFromGridSize(InventoryCrafting inv) {
		int size = inv.getSizeInventory();
		int tier = size < 10 ? 1
				: size < 26 && size > 9 ? 2
						: size < 50 && size > 25 ? 3
								// : this.input.size() < 82 && this.input.size()
								// > 49 ? 4
								: 4;
		return tier;
	}

	@Override
	public int getTier() {
		int tier = (this.width < 4 && this.height < 4) ? 1
				: (this.width > 3 && this.height > 3 && this.width < 6 && this.height < 6) ? 2
						: (this.width > 5 && this.height > 5 && this.width < 8 && this.height < 8) ? 3
								// : (this.width > 7 && this.height > 7 &&
								// this.width < 10 && this.height < 10) ? 4
								: 4;
		return tier;
	}
}