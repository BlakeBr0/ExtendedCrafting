package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ShapedRecipePatternCodecs;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
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

public class ShapedFluxCrafterRecipe implements IFluxCrafterRecipe {
	public static final MapCodec<ShapedFluxCrafterRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					ShapedRecipePatternCodecs.MAP_CODEC.forGetter(recipe -> recipe.pattern),
					ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Codec.INT.fieldOf("power_required").forGetter(recipe -> recipe.powerRequired),
					Codec.INT.optionalFieldOf("power_rate", ModConfigs.FLUX_CRAFTER_POWER_RATE.get()).forGetter(recipe -> recipe.powerRate)
			).apply(builder, ShapedFluxCrafterRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShapedFluxCrafterRecipe> STREAM_CODEC = StreamCodec.of(
			ShapedFluxCrafterRecipe::toNetwork, ShapedFluxCrafterRecipe::fromNetwork
	);
	public static final RecipeSerializer<ShapedFluxCrafterRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final ShapedRecipePattern pattern;
	private final ItemStackTemplate result;
	private final int powerRequired;
	private final int powerRate;

	public ShapedFluxCrafterRecipe(ShapedRecipePattern pattern, ItemStackTemplate result, int powerRequired, int powerRate) {
		this.pattern = pattern;
		this.result = result;
		this.powerRequired = powerRequired;
		this.powerRate = powerRate;
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
	public RecipeSerializer<ShapedFluxCrafterRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<IFluxCrafterRecipe> getType() {
		return ModRecipeTypes.FLUX_CRAFTER.get();
	}

	@Override
	public List<Optional<Ingredient>> getPositionedIngredients() {
		return this.pattern.ingredients();
	}

	@Override
	public int getPowerRequired() {
		return this.powerRequired;
	}

	@Override
	public int getPowerRate() {
		return this.powerRate;
	}

	public int getWidth() {
		return this.pattern.width();
	}

	public int getHeight() {
		return this.pattern.height();
	}

	private static ShapedFluxCrafterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
		var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
		var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
		int powerRequired = buffer.readVarInt();
		int powerRate = buffer.readVarInt();

		return new ShapedFluxCrafterRecipe(pattern, result, powerRequired, powerRate);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, ShapedFluxCrafterRecipe recipe) {
		ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
		ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
		buffer.writeVarInt(recipe.powerRequired);
		buffer.writeVarInt(recipe.powerRate);
	}
}