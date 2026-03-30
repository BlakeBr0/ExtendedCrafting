package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

public class ShapelessEnderCrafterRecipe implements IEnderCrafterRecipe {
	private final NonNullList<Ingredient> inputs;
	private final ItemStack result;
	private final int craftingTime;

	public ShapelessEnderCrafterRecipe(NonNullList<Ingredient> inputs, ItemStack result, int craftingTime) {
		this.inputs = inputs;
		this.result = result;
		this.craftingTime = craftingTime;
	}

	@Override
	public boolean matches(CraftingInput inventory, Level level) {
		if (this.inputs.size() != inventory.ingredientCount())
			return false;

		var inputs = NonNullList.<ItemStack>create();

		for (var i = 0; i < inventory.size(); i++) {
			var item = inventory.getItem(i);
			if (!item.isEmpty()) {
				inputs.add(item);
			}
		}

		return RecipeMatcher.findMatches(inputs, this.inputs) != null;
	}

	@Override
	public ItemStack assemble(CraftingInput inventory) {
		return this.result.copy();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SHAPELESS_ENDER_CRAFTER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.ENDER_CRAFTER.get();
	}

	@Override
	public int getCraftingTime() {
		return this.craftingTime;
	}

	public static class Serializer implements RecipeSerializer<ShapelessEnderCrafterRecipe> {
		public static final MapCodec<ShapelessEnderCrafterRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				builder.group(
						Ingredient.CODEC_NONEMPTY
								.listOf()
								.fieldOf("ingredients")
								.flatXmap(
										field -> {
											var max = 9;
											var ingredients = field.toArray(Ingredient[]::new);
											if (ingredients.length == 0) {
												return DataResult.error(() -> "No ingredients for Combination recipe");
											} else {
												return ingredients.length > max
														? DataResult.error(() -> "Too many ingredients for Combination recipe. The maximum is: %s".formatted(max))
														: DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
											}
										},
										DataResult::success
								)
								.forGetter(recipe -> recipe.inputs),
						ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
						Codec.INT.optionalFieldOf("crafting_time", ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get()).forGetter(recipe -> recipe.craftingTime)
				).apply(builder, ShapelessEnderCrafterRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessEnderCrafterRecipe> STREAM_CODEC = StreamCodec.of(
				ShapelessEnderCrafterRecipe.Serializer::toNetwork, ShapelessEnderCrafterRecipe.Serializer::fromNetwork
		);

		@Override
		public MapCodec<ShapelessEnderCrafterRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapelessEnderCrafterRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static ShapelessEnderCrafterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			int size = buffer.readVarInt();
			var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

			for (int i = 0; i < size; ++i) {
				inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
			}

			var result = ItemStack.STREAM_CODEC.decode(buffer);
			int craftingTime = buffer.readVarInt();

			return new ShapelessEnderCrafterRecipe(inputs, result, craftingTime);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapelessEnderCrafterRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());

			for (var ingredient : recipe.inputs) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
			}

			ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
			buffer.writeVarInt(recipe.craftingTime);
		}
	}
}