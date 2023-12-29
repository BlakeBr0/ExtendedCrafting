package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.util.TriFunction;
import com.blakebr0.extendedcrafting.api.crafting.ITableRecipe;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ShapedTableRecipe implements ISpecialRecipe, ITableRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int width;
	private final int height;
	private final int tier;
	private TriFunction<Integer, Integer, ItemStack, ItemStack> transformer;

	public ShapedTableRecipe(ResourceLocation recipeId, int width, int height, NonNullList<Ingredient> inputs, ItemStack output) {
		this(recipeId, width, height, inputs, output, 0);
	}

	public ShapedTableRecipe(ResourceLocation recipeId, int width, int height, NonNullList<Ingredient> inputs, ItemStack output, int tier) {
		this.recipeId = recipeId;
		this.inputs = inputs;
		this.output = output;
		this.width = width;
		this.height = height;
		this.tier = tier;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return this.output;
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
		if (this.tier != 0 && this.tier != this.getTierFromGridSize(inventory))
			return false;

		int size = (int) Math.sqrt(inventory.getSlots());
		for (int i = 0; i <= size - this.width; i++) {
			for (int j = 0; j <= size - this.height; j++) {
				if (this.checkMatch(inventory, i, j, true)) {
					return true;
				}

				if (this.checkMatch(inventory, i, j, false)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return this.matches(new InvWrapper(inv));
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
		return ModRecipeSerializers.SHAPED_TABLE.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.TABLE.get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= this.width && height >= this.height;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(IItemHandler inventory) {
		if (this.transformer != null) {
			var remaining = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
			int size = (int) Math.sqrt(inventory.getSlots());

			for (int i = 0; i <= size - this.width; i++) {
				for (int j = 0; j <= size - this.height; j++) {
					if (this.checkMatch(inventory, i, j, true)) {
						for (int k = 0; k < this.height; k++) {
							for (int l = 0; l < this.width; l++) {
								int index = (this.width - 1 - l) + i + (k + j) * size;
								var stack = inventory.getStackInSlot(index);

								remaining.set(index, this.transformer.apply(l, k, stack));
							}
						}

						break;
					}

					if (this.checkMatch(inventory, i, j, false)) {
						for (int k = 0; k < this.height; k++) {
							for (int l = 0; l < this.width; l++) {
								int index = l + i + (k + j) * size;
								var stack = inventory.getStackInSlot(index);

								remaining.set(index, this.transformer.apply(l, k, stack));
							}
						}

						break;
					}
				}
			}

			return remaining;
		}

		return ISpecialRecipe.super.getRemainingItems(inventory);
	}

	@Override
	public int getTier() {
		if (this.tier > 0) return this.tier;

		return this.width < 4 && this.height < 4 ? 1
				 : this.width < 6 && this.height < 6 ? 2
				 : this.width < 8 && this.height < 8 ? 3
				 : 4;
	}

	@Override
	public boolean hasRequiredTier() {
		return this.tier > 0;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	private int getTierFromGridSize(IItemHandler inv) {
		int size = inv.getSlots();
		return size < 10 ? 1
				: size < 26 ? 2
				: size < 50 ? 3
				: 4;
	}

	private boolean checkMatch(IItemHandler inventory, int x, int y, boolean mirror) {
		int size = (int) Math.sqrt(inventory.getSlots());
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int k = i - x;
				int l = j - y;
				var ingredient = Ingredient.EMPTY;

				if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
					if (mirror) {
						ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
					} else {
						ingredient = this.inputs.get(k + l * this.width);
					}
				}

				if (!ingredient.test(inventory.getStackInSlot(i + j * size))) {
					return false;
				}
			}
		}

		return true;
	}

	private static String[] patternFromJson(JsonArray jsonArr) {
		var astring = new String[jsonArr.size()];
		for (int i = 0; i < astring.length; ++i) {
			var s = GsonHelper.convertToString(jsonArr.get(i), "pattern[" + i + "]");

			if (i > 0 && astring[0].length() != s.length()) {
				throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
			}

			astring[i] = s;
		}

		return astring;
	}

	public void setTransformer(TriFunction<Integer, Integer, ItemStack, ItemStack> transformer) {
		this.transformer = transformer;
	}

	public static class Serializer implements RecipeSerializer<ShapedTableRecipe> {
		@Override
		public ShapedTableRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			var map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
			var pattern = ShapedRecipe.shrink(ShapedTableRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
			int width = pattern[0].length();
			int height = pattern.length;
			var inputs = ShapedRecipe.dissolvePattern(pattern, map, width, height);
			var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int tier = GsonHelper.getAsInt(json, "tier", 0);
			int size = tier * 2 + 1;

			if (tier != 0 && (width > size || height > size))
				throw new JsonSyntaxException("The pattern size is larger than the specified tier can support");

			return new ShapedTableRecipe(recipeId, width, height, inputs, output, tier);
		}

		@Override
		public ShapedTableRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int width = buffer.readVarInt();
			int height = buffer.readVarInt();
			var inputs = NonNullList.withSize(width * height, Ingredient.EMPTY);

			for (int i = 0; i < inputs.size(); i++) {
				inputs.set(i, Ingredient.fromNetwork(buffer));
			}

			var output = buffer.readItem();
			int tier = buffer.readVarInt();

			return new ShapedTableRecipe(recipeId, width, height, inputs, output, tier);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ShapedTableRecipe recipe) {
			buffer.writeVarInt(recipe.width);
			buffer.writeVarInt(recipe.height);

			for (var ingredient : recipe.inputs) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.tier);
		}
	}
}