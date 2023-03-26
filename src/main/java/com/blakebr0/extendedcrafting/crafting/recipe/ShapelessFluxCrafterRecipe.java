package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
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
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.List;

public class ShapelessFluxCrafterRecipe implements ISpecialRecipe, IFluxCrafterRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int powerRequired;
	private final int powerRate;

	public ShapelessFluxCrafterRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, int powerRequired) {
		this(recipeId, inputs, output, powerRequired, ModConfigs.FLUX_CRAFTER_POWER_RATE.get());
	}

	public ShapelessFluxCrafterRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, int powerRequired, int powerRate) {
		this.recipeId = recipeId;
		this.inputs = inputs;
		this.output = output;
		this.powerRequired = powerRequired;
		this.powerRate = powerRate;
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
		return ModRecipeSerializers.SHAPELESS_FLUX_CRAFTER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.FLUX_CRAFTER.get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.inputs.size();
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
		List<ItemStack> inputs = new ArrayList<>();
		int matched = 0;

		for (int i = 0; i < inventory.getSlots(); i++) {
			var stack = inventory.getStackInSlot(i);

			if (!stack.isEmpty()) {
				inputs.add(stack);

				matched++;
			}
		}

		return matched == this.inputs.size() && RecipeMatcher.findMatches(inputs,  this.inputs) != null;
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return this.matches(new InvWrapper(inv));
	}

	@Override
	public int getPowerRequired() {
		return this.powerRequired;
	}

	@Override
	public int getPowerRate() {
		return this.powerRate;
	}

	public static class Serializer implements RecipeSerializer<ShapelessFluxCrafterRecipe> {
		@Override
		public ShapelessFluxCrafterRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			NonNullList<Ingredient> inputs = NonNullList.create();
			var ingredients = GsonHelper.getAsJsonArray(json, "ingredients");

			for (int i = 0; i < ingredients.size(); i++) {
				inputs.add(Ingredient.fromJson(ingredients.get(i)));
			}

			var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int powerRequired = GsonHelper.getAsInt(json, "powerRequired");
			int powerRate = GsonHelper.getAsInt(json, "powerRate", ModConfigs.FLUX_CRAFTER_POWER_RATE.get());

			return new ShapelessFluxCrafterRecipe(recipeId, inputs, output, powerRequired, powerRate);
		}

		@Override
		public ShapelessFluxCrafterRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int size = buffer.readVarInt();
			var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

			for (int i = 0; i < size; ++i) {
				inputs.set(i, Ingredient.fromNetwork(buffer));
			}

			var output = buffer.readItem();
			int powerRequired = buffer.readVarInt();
			int powerRate = buffer.readVarInt();

			return new ShapelessFluxCrafterRecipe(recipeId, inputs, output, powerRequired, powerRate);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ShapelessFluxCrafterRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());

			for (var ingredient : recipe.inputs) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.powerRequired);
			buffer.writeVarInt(recipe.powerRate);
		}
	}
}