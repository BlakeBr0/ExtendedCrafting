package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

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
	public ItemStack getResultItem(RegistryAccess access) {
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
		return ModRecipeSerializers.COMPRESSOR.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.COMPRESSOR.get();
	}

	@Override
	public ItemStack assemble(IItemHandler inventory, RegistryAccess access) {
		return this.output.copy();
	}

	@Override
	public ItemStack assemble(Container inventory, RegistryAccess access) {
		return this.output.copy();
	}

	@Override
	public boolean matches(IItemHandler inventory) {
		var input = inventory.getStackInSlot(0);
		var catalyst = inventory.getStackInSlot(1);

		return this.inputs.get(0).test(input) && this.catalyst.test(catalyst);
	}

	@Override
	public boolean matches(Container inventory, Level level) {
		return this.matches(new InvWrapper(inventory));
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

	public static class Serializer implements RecipeSerializer<CompressorRecipe> {
		@Override
		public CompressorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			var input = Ingredient.fromJson(json.getAsJsonObject("ingredient"));
			var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int inputCount = GsonHelper.getAsInt(json, "inputCount", 10000);
			var catalyst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "catalyst"));
			int powerCost = GsonHelper.getAsInt(json, "powerCost");
			int powerRate = GsonHelper.getAsInt(json, "powerRate", ModConfigs.COMPRESSOR_POWER_RATE.get());

			return new CompressorRecipe(recipeId, input, output, inputCount, catalyst, powerCost, powerRate);
		}

		@Override
		public CompressorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			var input = Ingredient.fromNetwork(buffer);
			var output = buffer.readItem();
			int inputCount = buffer.readInt();
			var catalyst = Ingredient.fromNetwork(buffer);
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
