package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ShapedRecipePatternCodecs;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class ShapedEnderCrafterRecipe implements IEnderCrafterRecipe {
	public static final MapCodec<ShapedEnderCrafterRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					ShapedRecipePatternCodecs.MAP_CODEC.forGetter(recipe -> recipe.pattern),
					ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Codec.INT.optionalFieldOf("crafting_time", ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get()).forGetter(recipe -> recipe.craftingTime)
			).apply(builder, ShapedEnderCrafterRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShapedEnderCrafterRecipe> STREAM_CODEC = StreamCodec.of(
			ShapedEnderCrafterRecipe::toNetwork, ShapedEnderCrafterRecipe::fromNetwork
	);
	public static final RecipeSerializer<ShapedEnderCrafterRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final ShapedRecipePattern pattern;
	private final ItemStackTemplate result;
	private final int craftingTime;

	public ShapedEnderCrafterRecipe(ShapedRecipePattern pattern, ItemStackTemplate result, int craftingTime) {
		this.pattern = pattern;
		this.result = result;
		this.craftingTime = craftingTime;
	}

	@Override
	public boolean matches(CraftingInput inventory, Level level) {
		return this.pattern.matches(inventory);
	}

	@Override
	public ItemStack assemble(CraftingInput inventory) {
		return this.result.create();
	}

	@Override
	public RecipeSerializer<ShapedEnderCrafterRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<IEnderCrafterRecipe> getType() {
		return ModRecipeTypes.ENDER_CRAFTER.get();
	}

	@Override
	public List<Optional<Ingredient>> getPositionedIngredients() {
		return this.pattern.ingredients();
	}

	@Override
	public int getCraftingTime() {
		return this.craftingTime;
	}

	public int getWidth() {
		return this.pattern.width();
	}

	public int getHeight() {
		return this.pattern.height();
	}

	private static ShapedEnderCrafterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
		var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
		var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
		int craftingTime = buffer.readVarInt();

		return new ShapedEnderCrafterRecipe(pattern, result, craftingTime);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapedEnderCrafterRecipe recipe) {
		ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
		ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
		buffer.writeVarInt(recipe.craftingTime);
	}
}