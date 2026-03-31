package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
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
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.Arrays;
import java.util.List;

public class ShapelessFluxCrafterRecipe implements IFluxCrafterRecipe {
	public static final MapCodec<ShapelessFluxCrafterRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					Ingredient.CODEC
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
													: DataResult.success(Arrays.asList(ingredients));
										}
									},
									DataResult::success
							)
							.forGetter(recipe -> recipe.ingredients),
					ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Codec.INT.fieldOf("power_required").forGetter(recipe -> recipe.powerRequired),
					Codec.INT.optionalFieldOf("power_rate", ModConfigs.FLUX_CRAFTER_POWER_RATE.get()).forGetter(recipe -> recipe.powerRequired)
			).apply(builder, ShapelessFluxCrafterRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessFluxCrafterRecipe> STREAM_CODEC = StreamCodec.of(
			ShapelessFluxCrafterRecipe::toNetwork, ShapelessFluxCrafterRecipe::fromNetwork
	);
	public static final RecipeSerializer<ShapelessFluxCrafterRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final List<Ingredient> ingredients;
	private final ItemStack result;
	private final int powerRequired;
	private final int powerRate;

	public ShapelessFluxCrafterRecipe(List<Ingredient> ingredients, ItemStack result, int powerRequired, int powerRate) {
		this.ingredients = ingredients;
		this.result = result;
		this.powerRequired = powerRequired;
		this.powerRate = powerRate;
	}

	@Override
	public boolean matches(CraftingInput inventory, Level level) {
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
	public ItemStack assemble(CraftingInput inventory) {
		return this.result.copy();
	}

	@Override
	public RecipeSerializer<ShapelessFluxCrafterRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<IFluxCrafterRecipe> getType() {
		return ModRecipeTypes.FLUX_CRAFTER.get();
	}

	@Override
	public int getPowerRequired() {
		return this.powerRequired;
	}

	@Override
	public int getPowerRate() {
		return this.powerRate;
	}

	private static ShapelessFluxCrafterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
		var ingredients = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
		var result = ItemStack.STREAM_CODEC.decode(buffer);
		int powerRequired = buffer.readVarInt();
		int powerRate = buffer.readVarInt();

		return new ShapelessFluxCrafterRecipe(ingredients, result, powerRequired, powerRate);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapelessFluxCrafterRecipe recipe) {
		Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.ingredients);
		ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
		buffer.writeVarInt(recipe.powerRequired);
		buffer.writeVarInt(recipe.powerRate);
	}
}