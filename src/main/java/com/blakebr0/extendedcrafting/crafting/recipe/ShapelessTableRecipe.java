package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class ShapelessTableRecipe implements ISpecialRecipe, ITableRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int tier;

	public ShapelessTableRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output) {
		this(recipeId, inputs, output, 0);
	}

	public ShapelessTableRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, int tier) {
		this.recipeId = recipeId;
		this.inputs = inputs;
		this.output = output;
		this.tier = tier;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeId;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SHAPELESS_TABLE;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeTypes.TABLE;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.inputs.size();
	}

	@Override
	public ItemStack getCraftingResult(IItemHandler inventory) {
		return this.output.copy();
	}

	@Override
	public boolean matches(IItemHandler inventory) {
		if (this.tier != 0 && this.tier != getTierFromSize(inventory.getSlots()))
			return false;

		List<ItemStack> inputs = new ArrayList<>();
		int matched = 0;

		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (!stack.isEmpty()) {
				inputs.add(stack);

				matched++;
			}
		}

		return matched == this.inputs.size() && RecipeMatcher.findMatches(inputs,  this.inputs) != null;
	}

	@Override
	public int getTier() {
		if (this.tier > 0) return this.tier;
		return getTierFromSize(this.inputs.size());
	}

	@Override
	public boolean hasRequiredTier() {
		return this.tier > 0;
	}

	private static int getTierFromSize(int size) {
		return size < 10 ? 1
				: size < 26 ? 2
				: size < 50 ? 3
				: 4;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessTableRecipe> {
		@Override
		public ShapelessTableRecipe read(ResourceLocation recipeId, JsonObject json) {
			NonNullList<Ingredient> inputs = NonNullList.create();
			JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
			for (int i = 0; i < ingredients.size(); i++) {
				inputs.add(Ingredient.deserialize(ingredients.get(i)));
			}

			ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			int tier = JSONUtils.getInt(json, "tier", 0);

			return new ShapelessTableRecipe(recipeId, inputs, output, tier);
		}

		@Override
		public ShapelessTableRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int size = buffer.readVarInt();
			NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);

			for (int i = 0; i < size; ++i) {
				inputs.set(i, Ingredient.read(buffer));
			}

			ItemStack output = buffer.readItemStack();
			int tier = buffer.readVarInt();

			return new ShapelessTableRecipe(recipeId, inputs, output, tier);
		}

		@Override
		public void write(PacketBuffer buffer, ShapelessTableRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());

			for (Ingredient ingredient : recipe.inputs) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.output);
			buffer.writeVarInt(recipe.tier);
		}
	}
}