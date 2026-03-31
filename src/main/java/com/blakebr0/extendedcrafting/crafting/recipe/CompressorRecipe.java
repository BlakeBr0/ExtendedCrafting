package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public class CompressorRecipe implements ICompressorRecipe {
	public static final MapCodec<CompressorRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
					ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Ingredient.CODEC.fieldOf("catalyst").forGetter(recipe -> recipe.catalyst),
					Codec.INT.fieldOf("power_cost").forGetter(recipe -> recipe.powerCost),
					Codec.INT.optionalFieldOf("power_rate", ModConfigs.COMPRESSOR_POWER_RATE.get()).forGetter(recipe -> recipe.powerRate)
			).apply(builder, CompressorRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, CompressorRecipe> STREAM_CODEC = StreamCodec.of(
			CompressorRecipe::toNetwork, CompressorRecipe::fromNetwork
	);
	public static final RecipeSerializer<CompressorRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final SizedIngredient ingredient;
	private final ItemStack result;
	private final Ingredient catalyst;
	private final int powerCost;
	private final int powerRate;

	public CompressorRecipe(SizedIngredient input, ItemStack result, Ingredient catalyst, int powerCost, int powerRate) {
		this.ingredient = input;
		this.result = result;
		this.catalyst = catalyst;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
	}

	@Override
	public boolean matches(CraftingInput inventory, Level level) {
		if (inventory.ingredientCount() != 2)
			return false;

		var input = inventory.getItem(0);
		var catalyst = inventory.getItem(1);

		return this.ingredient.ingredient().test(input) && this.catalyst.test(catalyst);
	}

	@Override
	public ItemStack assemble(CraftingInput inventory) {
		return this.result.copy();
	}

	@Override
	public RecipeSerializer<CompressorRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<ICompressorRecipe> getType() {
		return ModRecipeTypes.COMPRESSOR.get();
	}

	@Override
	public SizedIngredient getIngredient() {
		return this.ingredient;
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

	private static CompressorRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
		var ingredient = SizedIngredient.STREAM_CODEC.decode(buffer);
		var result = ItemStack.STREAM_CODEC.decode(buffer);
		var catalyst = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
		int powerCost = buffer.readInt();
		int powerRate = buffer.readInt();

		return new CompressorRecipe(ingredient, result, catalyst, powerCost, powerRate);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, CompressorRecipe recipe) {
		SizedIngredient.STREAM_CODEC.encode(buffer, recipe.ingredient);
		ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
		Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.catalyst);
		buffer.writeInt(recipe.powerCost);
		buffer.writeInt(recipe.powerRate);
	}
}
