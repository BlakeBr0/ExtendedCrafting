package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.ModRecipeSerializers;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Map;
import java.util.Set;

public class ShapedEnderCrafterRecipe implements ISpecialRecipe, IEnderCrafterRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int width;
	private final int height;
	private final int craftingTime;

	public ShapedEnderCrafterRecipe(ResourceLocation recipeId, int width, int height, NonNullList<Ingredient> inputs, ItemStack output) {
		this(recipeId, width, height, inputs, output, ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get());
	}

	public ShapedEnderCrafterRecipe(ResourceLocation recipeId, int width, int height, NonNullList<Ingredient> inputs, ItemStack output, int craftingTime) {
		this.recipeId = recipeId;
		this.inputs = inputs;
		this.output = output;
		this.width = width;
		this.height = height;
		this.craftingTime = craftingTime;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public ItemStack getCraftingResult(IItemHandler inventory) {
		return this.output.copy();
	}

	@Override
	public boolean matches(IItemHandler inventory) {
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
	public NonNullList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public ResourceLocation getId() {
		return this.recipeId;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SHAPED_ENDER_CRAFTER;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeTypes.ENDER_CRAFTER;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width >= this.width && height >= this.height;
	}

	@Override
	public int getCraftingTime() {
		return this.craftingTime;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	private boolean checkMatch(IItemHandler inventory, int x, int y, boolean mirror) {
		int size = (int) Math.sqrt(inventory.getSlots());
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int k = i - x;
				int l = j - y;
				Ingredient ingredient = Ingredient.EMPTY;
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

	private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
		NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(keys.keySet());
		set.remove(" ");

		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String s = pattern[i].substring(j, j + 1);
				Ingredient ingredient = keys.get(s);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
				}

				set.remove(s);
				nonnulllist.set(j + patternWidth * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return nonnulllist;
		}
	}

	@VisibleForTesting
	static String[] shrink(String... toShrink) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;

		for (int i1 = 0; i1 < toShrink.length; ++i1) {
			String s = toShrink[i1];
			i = Math.min(i, firstNonSpace(s));
			int j1 = lastNonSpace(s);
			j = Math.max(j, j1);
			if (j1 < 0) {
				if (k == i1) {
					++k;
				}

				++l;
			} else {
				l = 0;
			}
		}

		if (toShrink.length == l) {
			return new String[0];
		} else {
			String[] astring = new String[toShrink.length - l - k];

			for (int k1 = 0; k1 < astring.length; ++k1) {
				astring[k1] = toShrink[k1 + k].substring(i, j + 1);
			}

			return astring;
		}
	}

	private static int firstNonSpace(String str) {
		int i;
		for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
			;
		}

		return i;
	}

	private static int lastNonSpace(String str) {
		int i;
		for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
			;
		}

		return i;
	}

	private static String[] patternFromJson(JsonArray jsonArr) {
		String[] astring = new String[jsonArr.size()];
		for (int i = 0; i < astring.length; ++i) {
			String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");

			if (i > 0 && astring[0].length() != s.length()) {
				throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
			}

			astring[i] = s;
		}

		return astring;
	}

	/**
	 * Returns a key json object as a Java HashMap.
	 */
	private static Map<String, Ingredient> deserializeKey(JsonObject json) {
		Map<String, Ingredient> map = Maps.newHashMap();

		for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}

			map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
		}

		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapedEnderCrafterRecipe> {
		@Override
		public ShapedEnderCrafterRecipe read(ResourceLocation recipeId, JsonObject json) {
			Map<String, Ingredient> map = ShapedEnderCrafterRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
			String[] pattern = ShapedEnderCrafterRecipe.shrink(ShapedEnderCrafterRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
			int width = pattern[0].length();
			int height = pattern.length;
			NonNullList<Ingredient> inputs = ShapedEnderCrafterRecipe.deserializeIngredients(pattern, map, width, height);
			ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			int craftingTime = JSONUtils.getInt(json, "craftingTime", 0);
			int size = craftingTime * 2 + 1;
			if (craftingTime != 0 && (width > size || height > size))
				throw new JsonSyntaxException("The pattern size is larger than the specified tier can support");

			return new ShapedEnderCrafterRecipe(recipeId, width, height, inputs, output, craftingTime);
		}

		@Override
		public ShapedEnderCrafterRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int width = buffer.readVarInt();
			int height = buffer.readVarInt();
			NonNullList<Ingredient> inputs = NonNullList.withSize(width * height, Ingredient.EMPTY);

			for (int i = 0; i < inputs.size(); i++) {
				inputs.set(i, Ingredient.read(buffer));
			}

			ItemStack output = buffer.readItemStack();
			int craftingTime = buffer.readVarInt();

			return new ShapedEnderCrafterRecipe(recipeId, width, height, inputs, output, craftingTime);
		}

		@Override
		public void write(PacketBuffer buffer, ShapedEnderCrafterRecipe recipe) {
			buffer.writeVarInt(recipe.width);
			buffer.writeVarInt(recipe.height);

			for (Ingredient ingredient : recipe.inputs) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.output);
			buffer.writeVarInt(recipe.craftingTime);
		}
	}
}