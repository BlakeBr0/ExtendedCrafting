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
	public boolean canFit(int width, int height) {
		return true;
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
