package com.blakebr0.extendedcrafting.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.init.ModRecipeSerializers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class ShapelessEnderCrafterRecipe implements ISpecialRecipe, IEnderCrafterRecipe {
	private final ResourceLocation recipeId;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final int craftingTime;

	public ShapelessEnderCrafterRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output) {
		this(recipeId, inputs, output, ModConfigs.ENDER_CRAFTER_TIME_REQUIRED.get());
	}

	public ShapelessEnderCrafterRecipe(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, int craftingTime) {
		this.recipeId = recipeId;
		this.inputs = inputs;
		this.output = output;
		this.craftingTime = craftingTime;
	}

	@Override
	public ItemStack getRecipeOutput() {
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
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SHAPELESS_ENDER_CRAFTER;
	}

	@Override
	public IRecipeType<?> getType() {
		return RecipeTypes.ENDER_CRAFTER;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.inputs.size();
	}

	@Override
	public ItemStack getCraftingResult(IItemHandler inventory) {
		return this.output.copy();
	}

	@Override
	public boolean matches(IItemHandler inventory) {
		int matches = 0;
		for (int x = 0; x < inventory.getSlots(); x++) {
			ItemStack slot = inventory.getStackInSlot(x);

			if (!slot.isEmpty()) {
				boolean inRecipe = false;

				for (Ingredient target : this.inputs) {
					if (target.test(slot)) {
						if (target.getMatchingStacks().length == 0) {
							inRecipe = true;
							matches++;
							break;
						}

						for (ItemStack stack : target.getMatchingStacks()) {
							if (StackHelper.compareTags(stack, slot)) {
								inRecipe = true;
								matches++;
								break;
							}
						}

						if (inRecipe) break;
					}
				}

				if (!inRecipe) return false;
			}
		}

		return matches == this.inputs.size();
	}

	@Override
	public int getCraftingTime() {
		return this.craftingTime;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessEnderCrafterRecipe> {
		@Override
		public ShapelessEnderCrafterRecipe read(ResourceLocation recipeId, JsonObject json) {
			NonNullList<Ingredient> inputs = NonNullList.create();
			JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
			for (int i = 0; i < ingredients.size(); i++) {
				inputs.add(Ingredient.deserialize(ingredients.get(i)));
			}

			ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			int craftingTime = JSONUtils.getInt(json, "craftingTime", 0);

			return new ShapelessEnderCrafterRecipe(recipeId, inputs, output, craftingTime);
		}

		@Override
		public ShapelessEnderCrafterRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int size = buffer.readVarInt();
			NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);

			for (int i = 0; i < size; ++i) {
				inputs.set(i, Ingredient.read(buffer));
			}

			ItemStack output = buffer.readItemStack();
			int craftingTime = buffer.readVarInt();

			return new ShapelessEnderCrafterRecipe(recipeId, inputs, output, craftingTime);
		}

		@Override
		public void write(PacketBuffer buffer, ShapelessEnderCrafterRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());

			for (Ingredient ingredient : recipe.inputs) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.output);
			buffer.writeVarInt(recipe.craftingTime);
		}
	}
}