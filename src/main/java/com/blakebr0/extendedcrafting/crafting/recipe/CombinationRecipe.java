package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.util.IngredientListCache;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
import java.util.function.BiFunction;

public class CombinationRecipe implements ICombinationRecipe {
	public static final MapCodec<CombinationRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
					Ingredient.CODEC
							.listOf()
							.fieldOf("ingredients")
							.flatXmap(
									field -> {
										var max = 48;
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
					Codec.INT.fieldOf("power_cost").forGetter(recipe -> recipe.powerCost),
					Codec.INT.optionalFieldOf("power_rate", ModConfigs.CRAFTING_CORE_POWER_RATE.get()).forGetter(recipe -> recipe.powerRate)
			).apply(builder, CombinationRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, CombinationRecipe> STREAM_CODEC = StreamCodec.of(
			CombinationRecipe::toNetwork, CombinationRecipe::fromNetwork
	);
	public static final RecipeSerializer<CombinationRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Ingredient input;
	private final List<Ingredient> ingredients;
	private final ItemStack result;
	private final int powerCost;
	private final int powerRate;
    // for CraftTweaker recipes
    private BiFunction<Integer, ItemStack, ItemStack> transformer;

	public CombinationRecipe(Ingredient input, List<Ingredient> ingredients, ItemStack result, int powerCost, int powerRate) {
		this.input = input;
		this.ingredients = ingredients;
		this.result = result;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
	}

	@Override
	public boolean matches(CraftingInput inventory, Level level) {
		// -1 ingredient for the input stack
		if (this.ingredients.size() != inventory.ingredientCount() - 1)
			return false;

		var input = inventory.getItem(0);
		if (!this.input.test(input))
			return false;

		var inputs = NonNullList.<ItemStack>create();

		for (var i = 1; i < inventory.size(); i++) {
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
	public RecipeSerializer<CombinationRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<ICombinationRecipe> getType() {
		return ModRecipeTypes.COMBINATION.get();
	}

	@Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
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
            var inputs = NonNullList.<Ingredient>create();

            inputs.add(this.input);
            inputs.addAll(this.ingredients);

            for (int i = 0; i < remaining.size(); i++) {
                var stack = inventory.getItem(i);
                for (int j = 0; j < inputs.size(); j++) {
                    var input = inputs.get(j);

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
	public Ingredient getInput() {
		return this.input;
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
	public List<Component> getInputsList() {
		return IngredientListCache.getInstance().getIngredientsList(this, () -> {
            var ingredients = NonNullList.<Ingredient>create();
            ingredients.add(this.input);
            ingredients.addAll(this.ingredients);
            return ingredients;
        });
	}

    public void setTransformer(BiFunction<Integer, ItemStack, ItemStack> transformer) {
        this.transformer = transformer;
    }

	private static CombinationRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
		var input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
		var ingredients = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
		var result = ItemStack.STREAM_CODEC.decode(buffer);
		int powerCost = buffer.readVarInt();
		int powerRate = buffer.readVarInt();

		return new CombinationRecipe(input, ingredients, result, powerCost, powerRate);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, CombinationRecipe recipe) {
		Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
		Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.ingredients);
		ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
		buffer.writeVarInt(recipe.powerCost);
		buffer.writeVarInt(recipe.powerRate);
	}
}
