package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.TableCraftingInput;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class ShapelessTableRecipe implements ITableRecipe {
	public static final MapCodec<ShapelessTableRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					Ingredient.CODEC
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
													: DataResult.success(Arrays.asList(ingredients));
										}
									},
									DataResult::success
							)
							.forGetter(recipe -> recipe.ingredients),
					ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Codec.INT.optionalFieldOf("tier", 0).forGetter(recipe -> recipe.tier)
			).apply(builder, ShapelessTableRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessTableRecipe> STREAM_CODEC = StreamCodec.of(
			ShapelessTableRecipe::toNetwork, ShapelessTableRecipe::fromNetwork
	);
	public static final RecipeSerializer<ShapelessTableRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final List<Ingredient> ingredients;
	private final ItemStack result;
	private final int tier;
	private BiFunction<Integer, ItemStack, ItemStack> transformer;

	public ShapelessTableRecipe(List<Ingredient> ingredients, ItemStack result, int tier) {
		this.ingredients = ingredients;
		this.result = result;
		this.tier = tier;
	}

	@Override
	public boolean matches(TableCraftingInput inventory, Level level) {
		if (this.tier != 0 && this.tier != inventory.tier())
			return false;

		if (this.ingredients.size() != inventory.ingredientCount())
			return false;

		var inputs = NonNullList.<ItemStack>create();

		for (var i = 0; i < inventory.size(); i++) {
			var item = inventory.getItem(i);
			if (!item.isEmpty()) {
				inputs.add(item);
			}
		}

		return RecipeMatcher.findMatches(inputs, this.ingredients) != null;
	}

	@Override
	public ItemStack assemble(TableCraftingInput input) {
		return this.result.copy();
	}

	@Override
	public RecipeSerializer<ShapelessTableRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<ITableRecipe> getType() {
		return ModRecipeTypes.TABLE.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(TableCraftingInput inventory) {
		var remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

		for (int i = 0; i < remaining.size(); ++i) {
			var item = inventory.getItem(i);
			var remainder = item.getCraftingRemainder();
			if (remainder != null) {
				remaining.set(i, remainder.create());
			}
		}

		if (this.transformer != null) {
			var used = new boolean[remaining.size()];

			for (int i = 0; i < remaining.size(); i++) {
				var stack = inventory.getItem(i);

				for (int j = 0; j < this.ingredients.size(); j++) {
					var input = this.ingredients.get(j);

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
		return getTierFromSize(this.ingredients.size());
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

	private static ShapelessTableRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
		var ingredients = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
		var result = ItemStack.STREAM_CODEC.decode(buffer);
		int tier = buffer.readVarInt();

		return new ShapelessTableRecipe(ingredients, result, tier);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapelessTableRecipe recipe) {
		Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.ingredients);
		ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
		buffer.writeVarInt(recipe.tier);
	}
}