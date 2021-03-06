package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class CombinationRecipe implements ISpecialRecipe, ICombinationRecipe {
	private final ResourceLocation recipeId;
	private final ItemStack output;
	private final NonNullList<Ingredient> ingredients;
	private final int powerCost;
	private final int powerRate;
	private final List<ITextComponent> inputsList;
	
	public CombinationRecipe(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, ItemStack output, int powerCost) {
		this(recipeId, ingredients, output, powerCost, ModConfigs.CRAFTING_CORE_POWER_RATE.get());
	}

	public CombinationRecipe(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, ItemStack output, int powerCost, int powerRate) {
		this.recipeId = recipeId;
		this.ingredients = ingredients;
		this.output = output;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
		this.inputsList = new ArrayList<>();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
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
	public boolean matches(IItemHandler inventory) {
		ItemStack input = inventory.getStackInSlot(0);
		return this.ingredients.get(0).test(input) && ISpecialRecipe.super.matches(inventory);
	}

	@Override
	public int getPowerCost() {
		return this.powerCost;
	}

	@Override
	public int getPowerRate() {
		return this.powerRate;
	}

	@Override
	public List<ITextComponent> getInputsList() {
		return this.inputsList;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CombinationRecipe> {
		@Override
		public CombinationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			NonNullList<Ingredient> inputs = NonNullList.create();
			Ingredient input = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
			inputs.add(input);

			JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
			for (int i = 0; i < ingredients.size(); i++) {
				Ingredient ingredient = Ingredient.fromJson(ingredients.get(i));
				inputs.add(ingredient);
			}

			ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
			if (!json.has("powerCost"))
				throw new JsonSyntaxException("Missing powerCost for combination crafting recipe");
			int powerCost = JSONUtils.getAsInt(json, "powerCost");
			int powerRate = JSONUtils.getAsInt(json, "powerRate", ModConfigs.CRAFTING_CORE_POWER_RATE.get());

			return new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		}

		@Override
		public CombinationRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			int size = buffer.readVarInt();

			NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
			for (int i = 0; i < size; i++) {
				inputs.set(i, Ingredient.fromNetwork(buffer));
			}

			ItemStack output = buffer.readItem();
			int powerCost = buffer.readVarInt();
			int powerRate = buffer.readVarInt();

			return new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, CombinationRecipe recipe) {
			buffer.writeVarInt(recipe.ingredients.size());

			for (Ingredient ingredient : recipe.ingredients) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.powerCost);
			buffer.writeVarInt(recipe.powerRate);
		}
	}

}
