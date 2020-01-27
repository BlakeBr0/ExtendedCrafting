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
import net.minecraftforge.items.IItemHandler;

public class CompressorRecipe implements ISpecialRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int inputCount;
	private final Ingredient catalyst;
	private final int powerCost;
	private final int powerRate;
	
	public CompressorRecipe(ResourceLocation recipeId, Ingredient input, ItemStack output, int inputCount, Ingredient catalyst, int powerCost) {
		this(recipeId, input, output, inputCount, catalyst, powerCost, ModConfigs.confCompressorRFRate);
	}

	public CompressorRecipe(ResourceLocation recipeId, Ingredient input, ItemStack output, int inputCount, Ingredient catalyst, int powerCost, int powerRate) {
		this.recipeId = recipeId;
		this.inputs = NonNullList.from(Ingredient.EMPTY, input);
		this.output = output;
		this.inputCount = inputCount;
		this.catalyst = catalyst;
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
		return ModRecipeSerializers.SPECIAL_COMPRESSOR;
	}

	@Override
	public ISpecialRecipeType<?> getType() {
		return SpecialRecipeTypes.COMPRESSOR;
	}

	@Override
	public boolean matches(IItemHandler inventory, int startIndex, int endIndex) {
		ItemStack input = inventory.getStackInSlot(0);
		ItemStack catalyst = inventory.getStackInSlot(1);
		return this.inputs.get(0).test(input) && this.catalyst.test(catalyst);
	}

	public int getInputCount() {
		return this.inputCount;
	}

	public Ingredient getCatalyst() {
		return this.catalyst;
	}

	public int getPowerCost() {
		return this.powerCost;
	}
	
	public int getPowerRate() {
		return this.powerRate;
	}

	public static class Serializer implements ISpecialRecipeSerializer<CompressorRecipe> {
		@Override
		public CompressorRecipe read(ResourceLocation recipeId, JsonObject json) {
			Ingredient input = Ingredient.deserialize(json.getAsJsonObject("ingredient"));
			ItemStack output = ShapedRecipe.deserializeItem(json.getAsJsonObject("result"));
			int inputCount = JSONUtils.getInt(json, "inputCount", 10000);
			Ingredient catalyst = Ingredient.deserialize(json.getAsJsonObject("catalyst"));
			if (!json.has("powerCost"))
				throw new JsonSyntaxException("Missing powerCost for compressor recipe");
			int powerCost = JSONUtils.getInt(json, "powerCost");
			int powerRate = JSONUtils.getInt(json, "powerRate", ModConfigs.confCompressorRFRate);

			return new CompressorRecipe(recipeId, input, output, inputCount, catalyst, powerCost, powerRate);
		}

		@Override
		public CompressorRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			Ingredient input = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();
			int inputCount = buffer.readInt();
			Ingredient catalyst = Ingredient.read(buffer);
			int powerCost = buffer.readInt();
			int powerRate = buffer.readInt();

			return new CompressorRecipe(recipeId, input, output, inputCount, catalyst, powerCost, powerRate);
		}

		@Override
		public void write(PacketBuffer buffer, CompressorRecipe recipe) {
			recipe.inputs.get(0).write(buffer);
			buffer.writeItemStack(recipe.output);
			buffer.writeInt(recipe.inputCount);
			recipe.catalyst.write(buffer);
			buffer.writeInt(recipe.powerCost);
			buffer.writeInt(recipe.powerRate);
		}
	}
}
