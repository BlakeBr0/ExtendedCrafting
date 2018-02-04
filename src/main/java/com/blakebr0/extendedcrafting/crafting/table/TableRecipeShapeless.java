package com.blakebr0.extendedcrafting.crafting.table;

import java.util.Iterator;

import javax.annotation.Nonnull;

import com.blakebr0.cucumber.helper.RecipeHelper;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TableRecipeShapeless implements IRecipe, ITieredRecipe {

	@Nonnull
	protected ItemStack output = ItemStack.EMPTY;
	protected NonNullList<Ingredient> input = NonNullList.create();
	protected ResourceLocation group;
	protected int tier;

	public TableRecipeShapeless(int tier, @Nonnull ItemStack result, Object... recipe) {
		this.group = RecipeHelper.EMPTY_GROUP;
		this.tier = tier;
		output = result.copy();
		for (Object in : recipe) {
			Ingredient ing = CraftingHelper.getIngredient(in);
			if (ing != null) {
				input.add(ing);
			} else {
				String ret = "Invalid shapeless ore recipe: ";
				for (Object tmp : recipe) {
					ret += tmp + ", ";
				}
				ret += output;
				throw new RuntimeException(ret);
			}
		}
	}

	@Override
	@Nonnull
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		return output.copy();
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World world) {
		NonNullList<Ingredient> required = NonNullList.create();
		required.addAll(input);

		if (tier != 0) {
			if (!(this.tier == this.getTierFromSize(var1.getSizeInventory()))) {
				return false;
			}
		}

		for (int x = 0; x < var1.getSizeInventory(); x++) {
			ItemStack slot = var1.getStackInSlot(x);

			if (!slot.isEmpty()) {
				boolean inRecipe = false;
				Iterator<Ingredient> req = required.iterator();

				while (req.hasNext()) {
					if (req.next().apply(slot)) {
						inRecipe = true;
						req.remove();
						break;
					}
				}

				if (!inRecipe) {
					return false;
				}
			}
		}

		return required.isEmpty();
	}

	@Override
	@Nonnull
	public NonNullList<Ingredient> getIngredients() {
		return this.input;
	}

	@Override
	@Nonnull
	public String getGroup() {
		return this.group == null ? "" : this.group.toString();
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.input.size();
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

	@Override
	public int getTier() {
		if (this.tier > 0) {
			return this.tier;
		}
		int tier = this.input.size() < 10 ? 1
				 : this.input.size() < 26 && this.input.size() > 9 ? 2
				 : this.input.size() < 50 && this.input.size() > 25 ? 3
				 : 4;
		return tier;
	}

	@Override
	public boolean matches(IItemHandlerModifiable grid) {
		NonNullList<Ingredient> required = NonNullList.create();
		required.addAll(input);

		if (tier != 0) {
			if (!(this.tier == this.getTierFromSize(grid.getSlots()))) {
				return false;
			}
		}

		for (int x = 0; x < grid.getSlots(); x++) {
			ItemStack slot = grid.getStackInSlot(x);

			if (!slot.isEmpty()) {
				boolean inRecipe = false;
				Iterator<Ingredient> req = required.iterator();

				while (req.hasNext()) {
					if (req.next().apply(slot)) {
						inRecipe = true;
						req.remove();
						break;
					}
				}

				if (!inRecipe) {
					return false;
				}
			}
		}

		return required.isEmpty();
	}
}