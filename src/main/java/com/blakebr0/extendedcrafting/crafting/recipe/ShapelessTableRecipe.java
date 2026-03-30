package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.TableCraftingInput;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.function.BiFunction;

public class ShapelessTableRecipe implements ITableRecipe {
	private final NonNullList<Ingredient> inputs;
	private final ItemStack result;
	private final int tier;
	private BiFunction<Integer, ItemStack, ItemStack> transformer;

	public ShapelessTableRecipe(NonNullList<Ingredient> inputs, ItemStack result, int tier) {
		this.inputs = inputs;
		this.result = result;
		this.tier = tier;
	}

	@Override
	public boolean matches(TableCraftingInput inventory, Level level) {
		if (this.tier != 0 && this.tier != inventory.tier())
			return false;

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
	public ItemStack assemble(TableCraftingInput input, HolderLookup.Provider provider) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.inputs.size();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider lookup) {
		return this.result;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SHAPELESS_TABLE.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.TABLE.get();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(TableCraftingInput inventory) {
		var remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

		for (int i = 0; i < remaining.size(); ++i) {
			var item = inventory.getItem(i);
			if (item.hasCraftingRemainingItem()) {
				remaining.set(i, item.getCraftingRemainingItem());
			}
		}

		if (this.transformer != null) {
			var used = new boolean[remaining.size()];

			for (int i = 0; i < remaining.size(); i++) {
				var stack = inventory.getItem(i);

				for (int j = 0; j < this.inputs.size(); j++) {
					var input = this.inputs.get(j);

					if (!used[j] && input.test(stack)) {
						var ingredient = this.transformer.apply(j, stack);

						used[j] = true;
						remaining.set(i, ingredient);

						break;
					}
				}
			}
		}

		return remaining;
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

	public void setTransformer(BiFunction<Integer, ItemStack, ItemStack> transformer) {
		this.transformer = transformer;
	}

	private static int getTierFromSize(int size) {
		return size < 10 ? 1
				: size < 26 ? 2
				: size < 50 ? 3
				: 4;
	}

	public static class Serializer implements RecipeSerializer<ShapelessTableRecipe> {
		public static final MapCodec<ShapelessTableRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				builder.group(
						Ingredient.CODEC_NONEMPTY
								.listOf()
								.fieldOf("ingredients")
								.flatXmap(
										field -> {
											var max = 81;
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
						Codec.INT.optionalFieldOf("tier", 0).forGetter(recipe -> recipe.tier)
				).apply(builder, ShapelessTableRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessTableRecipe> STREAM_CODEC = StreamCodec.of(
				ShapelessTableRecipe.Serializer::toNetwork, ShapelessTableRecipe.Serializer::fromNetwork
		);

		@Override
		public MapCodec<ShapelessTableRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ShapelessTableRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static ShapelessTableRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			int size = buffer.readVarInt();
			var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

			for (int i = 0; i < size; ++i) {
				inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
			}

			var result = ItemStack.STREAM_CODEC.decode(buffer);
			int tier = buffer.readVarInt();

			return new ShapelessTableRecipe(inputs, result, tier);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapelessTableRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());

			for (var ingredient : recipe.inputs) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
			}

			ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
			buffer.writeVarInt(recipe.tier);
		}
	}
}