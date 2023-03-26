package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.util.IngredientListCache;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;

public class CombinationRecipe implements ISpecialRecipe, ICombinationRecipe {
	private final ResourceLocation recipeId;
	private final ItemStack output;
	private final NonNullList<Ingredient> ingredients;
	private final int powerCost;
	private final int powerRate;

	public CombinationRecipe(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, ItemStack output, int powerCost) {
		this(recipeId, ingredients, output, powerCost, ModConfigs.CRAFTING_CORE_POWER_RATE.get());
	}

	public CombinationRecipe(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, ItemStack output, int powerCost, int powerRate) {
		this.recipeId = recipeId;
		this.ingredients = ingredients;
		this.output = output;
		this.powerCost = powerCost;
		this.powerRate = powerRate;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return this.output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeId;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.COMBINATION.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.COMBINATION.get();
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
		var input = inventory.getStackInSlot(0);
		return this.ingredients.get(0).test(input) && ISpecialRecipe.super.matches(inventory);
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return this.matches(new InvWrapper(inv));
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
		return IngredientListCache.getInstance().getIngredientsList(this.recipeId, this.ingredients);
	}

	public static class Serializer implements RecipeSerializer<CombinationRecipe> {
		@Override
		public CombinationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			NonNullList<Ingredient> inputs = NonNullList.create();
			var input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));

			inputs.add(input);

			var ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
			for (int i = 0; i < ingredients.size(); i++) {
				var ingredient = Ingredient.fromJson(ingredients.get(i));
				inputs.add(ingredient);
			}

			var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

			if (!json.has("powerCost"))
				throw new JsonSyntaxException("Missing powerCost for combination crafting recipe");

			int powerCost = GsonHelper.getAsInt(json, "powerCost");
			int powerRate = GsonHelper.getAsInt(json, "powerRate", ModConfigs.CRAFTING_CORE_POWER_RATE.get());

			return new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		}

		@Override
		public CombinationRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int size = buffer.readVarInt();
			var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

			for (int i = 0; i < size; i++) {
				inputs.set(i, Ingredient.fromNetwork(buffer));
			}

			var output = buffer.readItem();
			int powerCost = buffer.readVarInt();
			int powerRate = buffer.readVarInt();

			return new CombinationRecipe(recipeId, inputs, output, powerCost, powerRate);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CombinationRecipe recipe) {
			buffer.writeVarInt(recipe.ingredients.size());

			for (var ingredient : recipe.ingredients) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.powerCost);
			buffer.writeVarInt(recipe.powerRate);
		}
	}

}
