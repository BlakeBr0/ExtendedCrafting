package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CompressorRecipe implements ISpecialRecipe, ICompressorRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int inputCount;
	private final Ingredient catalyst;
	private final int powerCost;
	private final int powerRate;
	
	public CompressorRecipe(ResourceLocation recipeId, Ingredient input, ItemStack output, int inputCount, Ingredient catalyst, int powerCost) {
		this(recipeId, input, output, inputCount, catalyst, powerCost, ModConfigs.COMPRESSOR_POWER_RATE.get());
	}

	public CompressorRecipe(ResourceLocation recipeId, Ingredient input, ItemStack output, int inputCount, Ingredient catalyst, int powerCost, int powerRate) {
		this.recipeId = recipeId;
		this.inputs = NonNullList.of(Ingredient.EMPTY, input);
		this.output = output;
		this.inputCount = inputCount;
		this.catalyst = catalyst;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
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
		return this.inputs;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeId;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.COMPRESSOR;
	}

	@Override
	public RecipeType<?> getType() {
		return RecipeTypes.COMPRESSOR;
	}

	@Override
	public ItemStack getCraftingResult(IItemHandler inventory) {
		return this.output.copy();
	}

	@Override
	public boolean matches(IItemHandler inventory, int startIndex, int endIndex) {
		ItemStack input = inventory.getStackInSlot(0);
		ItemStack catalyst = inventory.getStackInSlot(1);
		return this.inputs.get(0).test(input) && this.catalyst.test(catalyst);
	}

	@Override
	public int getInputCount() {
		return this.inputCount;
	}

	@Override
	public Ingredient getCatalyst() {
		return this.catalyst;
	}

	@Override
	public int getPowerCost() {
		return this.powerCost;
	}

	@Override
	public int getPowerRate() {
		return this.powerRate;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CompressorRecipe> {
		@Override
		public CompressorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			Ingredient input = Ingredient.fromJson(json.getAsJsonObject("ingredient"));
			ItemStack output = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int inputCount = GsonHelper.getAsInt(json, "inputCount", 10000);
			Ingredient catalyst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "catalyst"));
			int powerCost = GsonHelper.getAsInt(json, "powerCost");
			int powerRate = GsonHelper.getAsInt(json, "powerRate", ModConfigs.COMPRESSOR_POWER_RATE.get());

			return new CompressorRecipe(recipeId, input, output, inputCount, catalyst, powerCost, powerRate);
		}

		@Override
		public CompressorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			Ingredient input = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();
			int inputCount = buffer.readInt();
			Ingredient catalyst = Ingredient.fromNetwork(buffer);
			int powerCost = buffer.readInt();
			int powerRate = buffer.readInt();

			return new CompressorRecipe(recipeId, input, output, inputCount, catalyst, powerCost, powerRate);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CompressorRecipe recipe) {
			recipe.inputs.get(0).toNetwork(buffer);
			buffer.writeItem(recipe.output);
			buffer.writeInt(recipe.inputCount);
			recipe.catalyst.toNetwork(buffer);
			buffer.writeInt(recipe.powerCost);
			buffer.writeInt(recipe.powerRate);
		}
	}
}
