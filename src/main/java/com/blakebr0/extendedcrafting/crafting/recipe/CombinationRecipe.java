package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.crafting.ISpecialRecipeSerializer;
import com.blakebr0.cucumber.crafting.ISpecialRecipeType;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.crafting.SpecialRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class CombinationRecipe implements ISpecialRecipe {
	private final ResourceLocation recipeId;
	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;
	private final int powerCost;
	private final int powerRate;
	
	public CombinationRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, int powerCost) {
		this(recipeId, inputs, output, powerCost, ModConfigs.CRAFTING_CORE_POWER_RATE.get());
	}

	public CombinationRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, int powerCost, int powerRate) {
		this.recipeId = recipeId;
		this.inputs = inputs;
		this.output = output;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
	}

	@Override
	public ItemStack getOutput() {
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
	public ISpecialRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SPECIAL_COMBINATION;
	}

	@Override
	public ISpecialRecipeType<?> getType() {
		return SpecialRecipeTypes.COMBINATION;
	}

	public int getPowerCost() {
		return this.powerCost;
	}

	public int getPowerRate() {
		return this.powerRate;
	}

	public static class Serializer implements ISpecialRecipeSerializer<CombinationRecipe> {
		@Override
		public CombinationRecipe read(ResourceLocation recipeId, JsonObject json) {
			JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
			NonNullList<Ingredient> inputs = NonNullList.create();
			for (int i = 0; i < ingredients.size(); i++) {
				Ingredient ingredient = Ingredient.deserialize(ingredients.get(i));
				inputs.add(ingredient);
			}

			ItemStack output = ShapedRecipe.deserializeItem(json.getAsJsonObject("result"));
			if (!json.has("powerCost"))
				throw new JsonSyntaxException("Missing powerCost for combination crafting recipe");
			int powerCost = JSONUtils.getInt(json, "powerCost");
			int powerRate = JSONUtils.getInt(json, "powerRate", ModConfigs.CRAFTING_CORE_POWER_RATE.get());

			return new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		}

		@Override
		public CombinationRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int size = buffer.readVarInt();

			NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
			for (int i = 0; i < size; i++) {
				inputs.add(Ingredient.read(buffer));
			}

			ItemStack output = buffer.readItemStack();
			int powerCost = buffer.readInt();
			int powerRate = buffer.readInt();

			return new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		}

		@Override
		public void write(PacketBuffer buffer, CombinationRecipe recipe) {
			buffer.writeInt(recipe.inputs.size());

			for (Ingredient ingredient : recipe.inputs) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.output);
			buffer.writeInt(recipe.powerCost);
			buffer.writeInt(recipe.powerRate);
		}
	}

}
