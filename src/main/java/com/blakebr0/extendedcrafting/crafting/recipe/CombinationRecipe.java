package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.ModRecipeSerializers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CombinationRecipe implements ISpecialRecipe, ICombinationRecipe {
	private final ResourceLocation recipeId;
	private final ItemStack output;
	private final Ingredient input;
	private final NonNullList<Ingredient> ingredients;
	private final int powerCost;
	private final int powerRate;
	
	public CombinationRecipe(ResourceLocation recipeId, Ingredient input, NonNullList<Ingredient> ingredients, ItemStack output, int powerCost) {
		this(recipeId, input, ingredients, output, powerCost, ModConfigs.CRAFTING_CORE_POWER_RATE.get());
	}

	public CombinationRecipe(ResourceLocation recipeId, Ingredient input, NonNullList<Ingredient> ingredients, ItemStack output, int powerCost, int powerRate) {
		this.recipeId = recipeId;
		this.input = input;
		this.ingredients = ingredients;
		this.output = output;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeId;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.COMBINATION;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeTypes.COMBINATION;
	}

	@Override
	public ItemStack getCraftingResult(IItemHandler inventory) {
		return this.output.copy();
	}

	@Override
	public int getPowerCost() {
		return this.powerCost;
	}

	@Override
	public int getPowerRate() {
		return this.powerRate;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CombinationRecipe> {
		@Override
		public CombinationRecipe read(ResourceLocation recipeId, JsonObject json) {
			Ingredient input = Ingredient.deserialize(json.get("input"));
			JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
			NonNullList<Ingredient> pedestals = NonNullList.create();
			for (int i = 0; i < ingredients.size(); i++) {
				Ingredient ingredient = Ingredient.deserialize(ingredients.get(i));
				pedestals.add(ingredient);
			}

			ItemStack output = ShapedRecipe.deserializeItem(json.getAsJsonObject("result"));
			if (!json.has("powerCost"))
				throw new JsonSyntaxException("Missing powerCost for combination crafting recipe");
			int powerCost = JSONUtils.getInt(json, "powerCost");
			int powerRate = JSONUtils.getInt(json, "powerRate", ModConfigs.CRAFTING_CORE_POWER_RATE.get());

			return new CombinationRecipe(recipeId, input, pedestals, output, powerCost, powerRate);
		}

		@Override
		public CombinationRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int size = buffer.readVarInt();
			Ingredient input = Ingredient.read(buffer);

			NonNullList<Ingredient> pedestals = NonNullList.withSize(size, Ingredient.EMPTY);
			for (int i = 0; i < size; i++) {
				pedestals.add(Ingredient.read(buffer));
			}

			ItemStack output = buffer.readItemStack();
			int powerCost = buffer.readInt();
			int powerRate = buffer.readInt();

			return new CombinationRecipe(recipeId, input, pedestals, output, powerCost, powerRate);
		}

		@Override
		public void write(PacketBuffer buffer, CombinationRecipe recipe) {
			buffer.writeInt(recipe.ingredients.size());
			recipe.input.write(buffer);

			for (Ingredient ingredient : recipe.ingredients) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.output);
			buffer.writeInt(recipe.powerCost);
			buffer.writeInt(recipe.powerRate);
		}
	}

}
