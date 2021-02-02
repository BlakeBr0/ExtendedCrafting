package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CraftTweakerUtils;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.AdvancedTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.BasicTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.CraftingCoreTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EliteTableTileEntity;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.UltimateTableTileEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.INBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class RecipeMakerItem extends BaseItem implements IEnableable {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String NEW_LINE = System.lineSeparator() + "\t";
	private static final char[] KEYS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();

	public RecipeMakerItem(Function<Properties, Properties> properties) {
		super(properties.compose(p -> p.maxStackSize(1)));
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isEnabled() && this.isInGroup(group)) {
			ItemStack stack1 = new ItemStack(this);
			NBTHelper.setBoolean(stack1, "Shapeless", false);
			NBTHelper.setString(stack1, "Type", "Datapack");

			ItemStack stack2 = new ItemStack(this);
			NBTHelper.setBoolean(stack2, "Shapeless", false);
			NBTHelper.setString(stack2, "Type", "CraftTweaker");

			items.add(stack1);
			items.add(stack2);
		}
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		BlockPos pos = context.getPos();
		Direction facing = context.getFace();
		World world = context.getWorld();
		if (player == null || !player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return ActionResultType.PASS;
		}

		TileEntity tile = world.getTileEntity(pos);
		if (isTable(tile)) {
			if (world.isRemote()) {
				String type = NBTHelper.getString(stack, "Type");
				BaseItemStackHandler inventory = ((BaseInventoryTileEntity) tile).getInventory();
				if ("CraftTweaker".equals(type)) {
					type = tile instanceof EnderCrafterTileEntity ? "EnderCrafting" : "TableCrafting";
					String string = isShapeless(stack)
							? makeShapelessCraftTweakerTableRecipe(inventory, type)
							: makeShapedCraftTweakerTableRecipe(inventory, type);

					setClipboard(string);
				} else {
					type = tile instanceof EnderCrafterTileEntity ? "EnderCrafting" : "TableCrafting";
					String string = isShapeless(stack)
							? makeShapelessDatapackTableRecipe(inventory, type)
							: makeShapedDatapackTableRecipe(inventory, type);

					if ("TOO MANY ITEMS".equals(string)) {
						player.sendMessage(Localizable.of("message.extendedcrafting.max_unique_items_exceeded").args(KEYS.length).build(), Util.DUMMY_UUID);

						return ActionResultType.SUCCESS;
					}

					setClipboard(string);
				}

				player.sendMessage(Localizable.of("message.extendedcrafting.copied_recipe").build(), Util.DUMMY_UUID);

				if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !ModList.get().isLoaded("crafttweaker")) {
					player.sendMessage(Localizable.of("message.extendedcrafting.nbt_requires_crafttweaker").build(), Util.DUMMY_UUID);
				}
			}

			return ActionResultType.SUCCESS;
		} else if (tile instanceof CraftingCoreTileEntity) {
			if (world.isRemote()) {
				String type = NBTHelper.getString(stack, "Type");
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
				String string = "CraftTweaker".equals(type)
						? makeCraftTweakerCombinationRecipe(core)
						: makeDatapackCombinationRecipe(core);

				setClipboard(string);

				player.sendMessage(Localizable.of("message.extendedcrafting.copied_recipe").build(), Util.DUMMY_UUID);
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (player.isCrouching()) {
			ItemStack stack = player.getHeldItem(hand);
			NBTHelper.flipBoolean(stack, "Shapeless");

			if (world.isRemote()) {
				player.sendMessage(Localizable.of("message.extendedcrafting.changed_mode").args(getModeString(stack)).build(), Util.DUMMY_UUID);
			}
		}

		return super.onItemRightClick(world, player, hand);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(ModTooltips.TYPE.args(NBTHelper.getString(stack, "Type")).build());
		tooltip.add(ModTooltips.MODE.args(getModeString(stack)).build());
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_RECIPE_MAKER.get();
	}

	private static void setClipboard(String string) {
		Minecraft.getInstance().keyboardListener.setClipboardString(string);
	}

	// Create a shaped CraftTweaker recipe for a Table or Ender Crafter
	private static String makeShapedCraftTweakerTableRecipe(IItemHandler inventory, String type) {
		StringBuilder string = new StringBuilder();
		UUID uuid = UUID.randomUUID();
		string.append("mods.extendedcrafting.").append(type).append(".addShaped(\"").append(uuid).append("\", ");
		if ("TableCrafting".equals(type)) string.append("0, ");
		string.append("<>, [").append(NEW_LINE);

		int slots = getGridSlots(inventory);
		int sr = (int) Math.sqrt(slots);

		for (int i = 0; i < slots; i++) {
			if (i == 0 || i % sr == 0) {
				string.append("[");
			}

			ItemStack stack = inventory.getStackInSlot(i);

			String item = "";
			if (!stack.isEmpty() && ModConfigs.RECIPE_MAKER_USE_TAGS.get()) {
				ResourceLocation tagId = stack.getItem().getTags().stream().findFirst().orElse(null);
				if (tagId != null) {
					item = "tag:" + tagId;
				}
			}

			if (item.isEmpty()) {
				ResourceLocation id = stack.getItem().getRegistryName();
				item = id == null ? "item:minecraft:air" : "item:" + id.toString();
			}

			string.append("<").append(item).append(">");

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && !item.startsWith("tag") && ModList.get().isLoaded("crafttweaker")) {
				INBT nbt = stack.serializeNBT().get("tag");
				String tag = CraftTweakerUtils.writeTag(nbt);
				string.append(".withTag(").append(tag).append(")");
			}

			if ((i + 1) % sr != 0) {
				string.append(", ");
			}

			if (i + 1 == sr || (i + 1) % sr == 0) {
				string.append("]");
				if (i + 1 < slots) {
					string.append(", ");
					string.append(NEW_LINE);
				} else {
					string.append(System.lineSeparator());
				}
			}
		}

		string.append("]);");

		return string.toString();
	}

	// Create a shapeless CraftTweaker recipe for a Table or Ender Crafter
	private static String makeShapelessCraftTweakerTableRecipe(IItemHandler inventory, String type) {
		StringBuilder string = new StringBuilder();
		UUID uuid = UUID.randomUUID();
		string.append("mods.extendedcrafting.").append(type).append(".addShapeless(\"").append(uuid).append("\", ");
		if ("TableCrafting".equals(type)) string.append("0, ");
		string.append("<>, [").append(NEW_LINE);

		List<Integer> slotsWithItems = new ArrayList<>();
		int slots = getGridSlots(inventory);

		int lastSlot = 0;
		for (int i = 0; i < slots; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty()) {
				slotsWithItems.add(i);
				lastSlot = i;
			}
		}

		for (int i : slotsWithItems) {
			ItemStack stack = inventory.getStackInSlot(i);
			ResourceLocation tagId = stack.getItem().getTags().stream().findFirst().orElse(null);
			String item;
			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
				item = "tag:" + tagId;
			} else {
				ResourceLocation id = stack.getItem().getRegistryName();
				item = id == null ? "item:minecraft:air" : "item:" + id.toString();
			}

			string.append("<").append(item).append(">");

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && !item.startsWith("tag") && ModList.get().isLoaded("crafttweaker")) {
				INBT nbt = stack.serializeNBT().get("tag");
				String tag = CraftTweakerUtils.writeTag(nbt);
				string.append(".withTag(").append(tag).append(")");
			}

			if (i != lastSlot) {
				string.append(", ");
			}
		}

		string.append(System.lineSeparator()).append("]);");

		return string.toString();
	}


	// Create a CraftTweaker recipe for a combination crafting recipe
	private static String makeCraftTweakerCombinationRecipe(CraftingCoreTileEntity tile) {
		StringBuilder string = new StringBuilder();
		UUID uuid = UUID.randomUUID();
		string.append("mods.extendedcrafting.CombinationCrafting.addRecipe(\"").append(uuid).append("\", <>, 100000, [").append(NEW_LINE);

		ResourceLocation inputId = tile.getInventory().getStackInSlot(0).getItem().getRegistryName();
		String input = "item:minecraft:air";
		if (inputId != null)
			input = "item:" + inputId.toString();

		string.append("<").append(input).append(">, ");

		ItemStack[] stacks = tile.getPedestalsWithItems().values().stream().filter(s -> !s.isEmpty()).toArray(ItemStack[]::new);
		for (int i = 0; i < stacks.length; i++) {
			ItemStack stack = stacks[i];
			ResourceLocation tagId = stack.getItem().getTags().stream().findFirst().orElse(null);
			String item;
			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
				item = "tag:" + tagId;
			} else {
				ResourceLocation id = stack.getItem().getRegistryName();
				item = id == null ? "item:minecraft:air" : "item:" + id.toString();
			}

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && !item.startsWith("tag") && ModList.get().isLoaded("crafttweaker")) {
				INBT nbt = stack.serializeNBT().get("tag");
				String tag = CraftTweakerUtils.writeTag(nbt);
				string.append(".withTag(").append(tag).append(")");
			}

			string.append("<").append(item).append(">");

			if (i != stacks.length - 1) {
				string.append(", ");
			}
		}

		string.append(System.lineSeparator()).append("]);");

		return string.toString();
	}

	// Create a shaped Datapack recipe for a Table or Ender Crafter
	private static String makeShapedDatapackTableRecipe(IItemHandler inventory, String type) {
		JsonObject object = new JsonObject();

		object.addProperty("type", "TableCrafting".equals(type)
				? "extendedcrafting:shaped_table"
				: "extendedcrafting:shaped_ender_crafter"
		);

		Map<Ingredient, Character> keysMap = new LinkedHashMap<>();
		int slots = getGridSlots(inventory);

		for (int i = 0; i < slots; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.isEmpty() || keysMap.keySet().stream().anyMatch(ing -> ing.test(stack)))
				continue;

			ResourceLocation tagId = stack.getItem().getTags().stream().findFirst().orElse(null);
			ITag<Item> tag = tagId == null ? null : ItemTags.getCollection().get(tagId);
			char key = KEYS[keysMap.size()];
			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tag != null) {
				keysMap.put(Ingredient.fromTag(tag), key);
			} else {
				if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && stack.hasTag()) {
					keysMap.put(new NBTIngredient(stack), key);
				} else {
					keysMap.put(Ingredient.fromStacks(stack), key);
				}
			}

			if (keysMap.size() >= KEYS.length)
				return "TOO MANY ITEMS";
		}

		JsonArray pattern = new JsonArray();
		int size = (int) Math.sqrt(slots);
		Set<Map.Entry<Ingredient, Character>> keys = keysMap.entrySet();

		for (int i = 0; i < size; i++) {
			StringBuilder line = new StringBuilder();
			for (int j = 0; j < size; j++) {
				ItemStack stack = inventory.getStackInSlot(i * size + j);
				Map.Entry<Ingredient, Character> entry = keys.stream()
						.filter(e -> e.getKey().test(stack)).findFirst().orElse(null);

				if (entry == null) {
					line.append(" ");
				} else {
					line.append(entry.getValue());
				}
			}

			pattern.add(line.toString());
		}

		object.add("pattern", pattern);

		JsonObject key = new JsonObject();
		for (Map.Entry<Ingredient, Character> entry : keys) {
			key.add(entry.getValue().toString(), entry.getKey().serialize());
		}

		object.add("key", key);

		JsonObject result = new JsonObject();
		result.addProperty("item", "");
		object.add("result", result);

		return GSON.toJson(object);
	}

	// Create a shapeless Datapack recipe for a Table or Ender Crafter
	private static String makeShapelessDatapackTableRecipe(IItemHandler inventory, String type) {
		JsonObject object = new JsonObject();

		object.addProperty("type", "TableCrafting".equals(type)
				? "extendedcrafting:shapeless_table"
				: "extendedcrafting:shapeless_ender_crafter"
		);

		JsonArray ingredients = new JsonArray();
		int slots = getGridSlots(inventory);

		for (int i = 0; i < slots; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty()) {
				ResourceLocation tagId = stack.getItem().getTags().stream().findFirst().orElse(null);
				if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
					JsonObject tag = new JsonObject();
					tag.addProperty("tag", tagId.toString());
					ingredients.add(tag);
				} else {
					if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && stack.hasTag()) {
						ingredients.add(new NBTIngredient(stack).serialize());
					} else {
						ingredients.add(Ingredient.fromStacks(stack).serialize());
					}
				}
			}
		}

		object.add("ingredients", ingredients);

		JsonObject result = new JsonObject();
		result.addProperty("item", "");
		object.add("result", result);

		return GSON.toJson(object);
	}

	// Create a Datapack recipe for a combination crafting recipe
	private static String makeDatapackCombinationRecipe(CraftingCoreTileEntity core) {
		JsonObject object = new JsonObject();

		object.addProperty("type", "extendedcrafting:combination");
		object.addProperty("powerCost", 100000);

		ItemStack input = core.getInventory().getStackInSlot(0);
		object.add("input", Ingredient.fromStacks(input).serialize());

		JsonArray ingredients = new JsonArray();
		ItemStack[] stacks = core.getPedestalsWithItems().values().stream().filter(s -> !s.isEmpty()).toArray(ItemStack[]::new);
		for (ItemStack stack : stacks) {
			ResourceLocation tagId = stack.getItem().getTags().stream().findFirst().orElse(null);
			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
				JsonObject tag = new JsonObject();
				tag.addProperty("tag", tagId.toString());
				ingredients.add(tag);
			} else {
				if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && stack.hasTag()) {
					ingredients.add(new NBTIngredient(stack).serialize());
				} else {
					ingredients.add(Ingredient.fromStacks(stack).serialize());
				}
			}
		}

		object.add("ingredients", ingredients);

		JsonObject result = new JsonObject();
		result.addProperty("item", "");
		object.add("result", result);

		return GSON.toJson(object);
	}

	private static boolean isTable(TileEntity tile) {
		return tile instanceof BasicTableTileEntity ||
				tile instanceof AdvancedTableTileEntity ||
				tile instanceof EliteTableTileEntity ||
				tile instanceof UltimateTableTileEntity ||
				tile instanceof AutoTableTileEntity ||
				tile instanceof EnderCrafterTileEntity;
	}

	private static String getModeString(ItemStack stack) {
		return isShapeless(stack) ? "Shapeless" : "Shaped";
	}

	private static boolean isShapeless(ItemStack stack) {
		return NBTHelper.getBoolean(stack, "Shapeless");
	}

	private static int getGridSlots(IItemHandler inventory) {
		int slots = inventory.getSlots();

		if (slots >= 81) return 81;
		else if (slots >= 49) return 49;
		else if (slots >= 25) return 25;
		else return 9;
	}

	private static class NBTIngredient extends net.minecraftforge.common.crafting.NBTIngredient {
		public NBTIngredient(ItemStack stack) {
			super(stack);
		}
	}
}
