package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
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
import java.util.function.BiFunction;

public class ShapelessTableRecipe implements ISpecialRecipe, ITableRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int tier;
	private BiFunction<Integer, ItemStack, ItemStack> transformer;

	public ShapelessTableRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output) {
		this(recipeId, inputs, output, 0);
	}

	public ShapelessTableRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, int tier) {
		this.recipeId = recipeId;
		this.inputs = inputs;
		this.output = output;
		this.tier = tier;
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
		return ModRecipeSerializers.SHAPELESS_TABLE.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.TABLE.get();
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
		if (this.tier != 0 && this.tier != getTierFromSize(inventory.getSlots()))
			return false;

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
	public NonNullList<ItemStack> getRemainingItems(IItemHandler inventory) {
		var remaining = ISpecialRecipe.super.getRemainingItems(inventory);

		if (this.transformer != null) {
			var used = new boolean[remaining.size()];

			for (int i = 0; i < remaining.size(); i++) {
				var stack = inventory.getStackInSlot(i);

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
		@Override
		public ShapelessTableRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			NonNullList<Ingredient> inputs = NonNullList.create();
			var ingredients = GsonHelper.getAsJsonArray(json, "ingredients");

			for (int i = 0; i < ingredients.size(); i++) {
				inputs.add(Ingredient.fromJson(ingredients.get(i)));
			}

			var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int tier = GsonHelper.getAsInt(json, "tier", 0);

			return new ShapelessTableRecipe(recipeId, inputs, output, tier);
		}

		@Override
		public ShapelessTableRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int size = buffer.readVarInt();
			var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

			for (int i = 0; i < size; ++i) {
				inputs.set(i, Ingredient.fromNetwork(buffer));
			}

			var output = buffer.readItem();
			int tier = buffer.readVarInt();

			return new ShapelessTableRecipe(recipeId, inputs, output, tier);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ShapelessTableRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());

			for (var ingredient : recipe.inputs) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.tier);
		}
	}
}