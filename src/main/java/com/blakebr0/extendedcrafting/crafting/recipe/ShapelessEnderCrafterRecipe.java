package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
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
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.Arrays;
import java.util.List;

public class ShapelessEnderCrafterRecipe implements IEnderCrafterRecipe {
	public static final MapCodec<ShapelessEnderCrafterRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
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
					ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Codec.INT.optionalFieldOf("crafting_time", ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get()).forGetter(recipe -> recipe.craftingTime)
			).apply(builder, ShapelessEnderCrafterRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessEnderCrafterRecipe> STREAM_CODEC = StreamCodec.of(
			ShapelessEnderCrafterRecipe::toNetwork, ShapelessEnderCrafterRecipe::fromNetwork
	);
	public static final RecipeSerializer<ShapelessEnderCrafterRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final List<Ingredient> ingredients;
	private final ItemStackTemplate result;
	private final int craftingTime;

	public ShapelessEnderCrafterRecipe(List<Ingredient> ingredients, ItemStackTemplate result, int craftingTime) {
		this.ingredients = ingredients;
		this.result = result;
		this.craftingTime = craftingTime;
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
		return this.result.create();
	}

	@Override
	public RecipeSerializer<ShapelessEnderCrafterRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<IEnderCrafterRecipe> getType() {
		return ModRecipeTypes.ENDER_CRAFTER.get();
	}

	@Override
	public List<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public int getCraftingTime() {
		return this.craftingTime;
	}

	private static ShapelessEnderCrafterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
		var ingredients = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
		var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
		int craftingTime = buffer.readVarInt();

		return new ShapelessEnderCrafterRecipe(ingredients, result, craftingTime);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapelessEnderCrafterRecipe recipe) {
		Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.ingredients);
		ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
		buffer.writeVarInt(recipe.craftingTime);
	}
}