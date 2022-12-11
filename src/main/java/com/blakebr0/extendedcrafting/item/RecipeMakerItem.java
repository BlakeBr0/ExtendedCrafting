package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.helper.NBTHelper;
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
import com.blakebr0.extendedcrafting.tileentity.FluxCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.UltimateTableTileEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipeMakerItem extends BaseItem {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String NEW_LINE = System.lineSeparator() + "\t";
	private static final char[] KEYS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-_*/".toCharArray();

	public RecipeMakerItem() {
		super(p -> p.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		var player = context.getPlayer();
		var pos = context.getClickedPos();
		var facing = context.getClickedFace();
		var level = context.getLevel();

		if (player == null || !player.mayUseItemAt(pos.relative(facing), facing, stack))
			return InteractionResult.PASS;

		var tile = level.getBlockEntity(pos);

		if (isTable(tile)) {
			if (level.isClientSide()) {
				var type = NBTHelper.getString(stack, "Type");
				var inventory = ((BaseInventoryTileEntity) tile).getInventory();
				var block = tile instanceof EnderCrafterTileEntity
						? "EnderCrafting"
						: tile instanceof FluxCrafterTileEntity
						? "FluxCrafting"
						: "TableCrafting";

				String string;
				if ("CraftTweaker".equals(type)) {
					string = isShapeless(stack)
							? makeShapelessCraftTweakerTableRecipe(inventory, block)
							: makeShapedCraftTweakerTableRecipe(inventory, block);

				} else {
					string = isShapeless(stack)
							? makeShapelessDatapackTableRecipe(inventory, block)
							: makeShapedDatapackTableRecipe(inventory, block);

					if ("TOO MANY ITEMS".equals(string)) {
						player.sendSystemMessage(Localizable.of("message.extendedcrafting.max_unique_items_exceeded").args(KEYS.length).build());

						return InteractionResult.SUCCESS;
					}
				}

				setClipboard(string);

				player.sendSystemMessage(Localizable.of("message.extendedcrafting.copied_recipe").build());

				if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && "CraftTweaker".equals(type) && !ModList.get().isLoaded("crafttweaker")) {
					player.sendSystemMessage(Localizable.of("message.extendedcrafting.nbt_requires_crafttweaker").build());
				}
			}

			return InteractionResult.SUCCESS;
		} else if (tile instanceof CraftingCoreTileEntity core) {
			if (level.isClientSide()) {
				var type = NBTHelper.getString(stack, "Type");
				var string = "CraftTweaker".equals(type)
						? makeCraftTweakerCombinationRecipe(core)
						: makeDatapackCombinationRecipe(core);

				setClipboard(string);

				player.sendSystemMessage(Localizable.of("message.extendedcrafting.copied_recipe").build());
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player.isCrouching()) {
			var stack = player.getItemInHand(hand);

			NBTHelper.flipBoolean(stack, "Shapeless");

			if (level.isClientSide()) {
				player.sendSystemMessage(Localizable.of("message.extendedcrafting.changed_mode").args(getModeString(stack)).build());
			}
		}

		return super.use(level, player, hand);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(ModTooltips.TYPE.args(NBTHelper.getString(stack, "Type")).build());
		tooltip.add(ModTooltips.MODE.args(getModeString(stack)).build());
	}

	private static void setClipboard(String string) {
		Minecraft.getInstance().keyboardHandler.setClipboard(string);
	}

	// Create a shaped CraftTweaker recipe for a Table, Flux Crafter or Ender Crafter
	private static String makeShapedCraftTweakerTableRecipe(IItemHandler inventory, String type) {
		var string = new StringBuilder();
		var uuid = UUID.randomUUID();

		string.append("mods.extendedcrafting.").append(type).append(".addShaped(\"").append(uuid).append("\", ");
		if ("TableCrafting".equals(type)) string.append("0, ");
		string.append("<>, [").append(NEW_LINE);

		int slots = getGridSlots(inventory);
		int sr = (int) Math.sqrt(slots);

		for (int i = 0; i < slots; i++) {
			if (i == 0 || i % sr == 0) {
				string.append("[");
			}

			var stack = inventory.getStackInSlot(i);
			var item = "";

			if (!stack.isEmpty() && ModConfigs.RECIPE_MAKER_USE_TAGS.get()) {
				var tagId = stack.getTags().findFirst().orElse(null);

				if (tagId != null) {
					item = "tag:items:" + tagId.location();
				}
			}

			if (item.isEmpty()) {
				var id = ForgeRegistries.ITEMS.getKey(stack.getItem());
				item = id == null ? "item:minecraft:air" : "item:" + id;
			}

			string.append("<").append(item).append(">");

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && !item.startsWith("tag") && ModList.get().isLoaded("crafttweaker")) {
				var nbt = stack.getTag();
				var tag = CraftTweakerUtils.writeTag(nbt);

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

		if (TableType.FLUX_CRAFTER.type.equals(type)) {
			string.append("], 100000, ");
			string.append(ModConfigs.FLUX_CRAFTER_POWER_RATE.get());
			string.append(");");
		} else {
			string.append("]);");
		}

		return string.toString();
	}

	// Create a shapeless CraftTweaker recipe for a Table, Flux Crafter or Ender Crafter
	private static String makeShapelessCraftTweakerTableRecipe(IItemHandler inventory, String type) {
		var string = new StringBuilder();
		var uuid = UUID.randomUUID();

		string.append("mods.extendedcrafting.").append(type).append(".addShapeless(\"").append(uuid).append("\", ");
		if ("TableCrafting".equals(type)) string.append("0, ");
		string.append("<>, [").append(NEW_LINE);

		List<Integer> slotsWithItems = new ArrayList<>();
		int slots = getGridSlots(inventory);

		int lastSlot = 0;
		for (int i = 0; i < slots; i++) {
			var stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty()) {
				slotsWithItems.add(i);
				lastSlot = i;
			}
		}

		for (int i : slotsWithItems) {
			var stack = inventory.getStackInSlot(i);
			var tagId = stack.getTags().findFirst().orElse(null);

			String item;
			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
				item = "tag:items:" + tagId;
			} else {
				var id = ForgeRegistries.ITEMS.getKey(stack.getItem());
				item = id == null ? "item:minecraft:air" : "item:" + id;
			}

			string.append("<").append(item).append(">");

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && !item.startsWith("tag") && ModList.get().isLoaded("crafttweaker")) {
				var nbt = stack.getTag();
				var tag = CraftTweakerUtils.writeTag(nbt);

				string.append(".withTag(").append(tag).append(")");
			}

			if (i != lastSlot) {
				string.append(", ");
			}
		}

		if (TableType.FLUX_CRAFTER.type.equals(type)) {
			string.append(System.lineSeparator());
			string.append("], 100000, ");
			string.append(ModConfigs.FLUX_CRAFTER_POWER_RATE.get());
			string.append(");");
		} else {
			string.append(System.lineSeparator()).append("]);");
		}

		return string.toString();
	}


	// Create a CraftTweaker recipe for a combination crafting recipe
	private static String makeCraftTweakerCombinationRecipe(CraftingCoreTileEntity tile) {
		var string = new StringBuilder();
		var uuid = UUID.randomUUID();

		string.append("mods.extendedcrafting.CombinationCrafting.addRecipe(\"").append(uuid).append("\", <>, 100000, [").append(NEW_LINE);

		var inputId = ForgeRegistries.ITEMS.getKey(tile.getInventory().getStackInSlot(0).getItem());
		var input = "item:minecraft:air";

		if (inputId != null)
			input = "item:" + inputId;

		string.append("<").append(input).append(">, ");

		var stacks = tile.getPedestalsWithItems().values().stream().filter(s -> !s.isEmpty()).toArray(ItemStack[]::new);

		for (int i = 0; i < stacks.length; i++) {
			var stack = stacks[i];
			var tagId = stack.getTags().findFirst().orElse(null);

			String item;
			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
				item = "tag:items:" + tagId;
			} else {
				var id = ForgeRegistries.ITEMS.getKey(stack.getItem());
				item = id == null ? "item:minecraft:air" : "item:" + id;
			}

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && !item.startsWith("tag") && ModList.get().isLoaded("crafttweaker")) {
				var nbt = stack.getTag();
				var tag = CraftTweakerUtils.writeTag(nbt);

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

	// Create a shaped Datapack recipe for a Table, Flux Crafter or Ender Crafter
	private static String makeShapedDatapackTableRecipe(IItemHandler inventory, String type) {
		var object = new JsonObject();
		var tableType = TableType.fromType(type);

		object.addProperty("type", tableType.shapedRecipeType);

		if (tableType == TableType.FLUX_CRAFTER) {
			object.addProperty("powerRequired", 100000);
			object.addProperty("powerRate", ModConfigs.FLUX_CRAFTER_POWER_RATE.get());
		}

		Map<Ingredient, Character> keysMap = new LinkedHashMap<>();
		int slots = getGridSlots(inventory);

		for (int i = 0; i < slots; i++) {
			var stack = inventory.getStackInSlot(i);

			if (stack.isEmpty() || keysMap.keySet().stream().anyMatch(ing -> ing.test(stack)))
				continue;

			var tag = stack.getTags().findFirst().orElse(null);
			char key = KEYS[keysMap.size()];
			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tag != null) {
				keysMap.put(Ingredient.of(tag), key);
			} else {
				if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && stack.hasTag()) {
					keysMap.put(StrictNBTIngredient.of(stack), key);
				} else {
					keysMap.put(Ingredient.of(stack), key);
				}
			}

			if (keysMap.size() >= KEYS.length)
				return "TOO MANY ITEMS";
		}

		var pattern = new JsonArray();
		int size = (int) Math.sqrt(slots);
		var keys = keysMap.entrySet();

		for (int i = 0; i < size; i++) {
			var line = new StringBuilder();

			for (int j = 0; j < size; j++) {
				var stack = inventory.getStackInSlot(i * size + j);
				var entry = keys.stream()
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

		var key = new JsonObject();

		for (var entry : keys) {
			key.add(entry.getValue().toString(), entry.getKey().toJson());
		}

		object.add("key", key);

		var result = new JsonObject();

		result.addProperty("item", "");
		object.add("result", result);

		return GSON.toJson(object);
	}

	// Create a shapeless Datapack recipe for a Table Flux Crafter or Ender Crafter
	private static String makeShapelessDatapackTableRecipe(IItemHandler inventory, String type) {
		var object = new JsonObject();
		var tableType = TableType.fromType(type);

		object.addProperty("type", tableType.shapelessRecipeType);

		if (tableType == TableType.FLUX_CRAFTER) {
			object.addProperty("powerRequired", 100000);
			object.addProperty("powerRate", ModConfigs.FLUX_CRAFTER_POWER_RATE.get());
		}

		var ingredients = new JsonArray();
		int slots = getGridSlots(inventory);

		for (int i = 0; i < slots; i++) {
			var stack = inventory.getStackInSlot(i);

			if (!stack.isEmpty()) {
				var tagId = stack.getTags().findFirst().orElse(null);

				if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
					var tag = new JsonObject();

					tag.addProperty("tag", tagId.toString());
					ingredients.add(tag);
				} else {
					if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && stack.hasTag()) {
						ingredients.add(StrictNBTIngredient.of(stack).toJson());
					} else {
						ingredients.add(Ingredient.of(stack).toJson());
					}
				}
			}
		}

		object.add("ingredients", ingredients);

		var result = new JsonObject();

		result.addProperty("item", "");
		object.add("result", result);

		return GSON.toJson(object);
	}

	// Create a Datapack recipe for a combination crafting recipe
	private static String makeDatapackCombinationRecipe(CraftingCoreTileEntity core) {
		var object = new JsonObject();

		object.addProperty("type", "extendedcrafting:combination");
		object.addProperty("powerCost", 100000);

		var input = core.getInventory().getStackInSlot(0);

		object.add("input", Ingredient.of(input).toJson());

		var ingredients = new JsonArray();
		var stacks = core.getPedestalsWithItems().values().stream().filter(s -> !s.isEmpty()).toArray(ItemStack[]::new);

		for (var stack : stacks) {
			var tagId = stack.getTags().findFirst().orElse(null);

			if (ModConfigs.RECIPE_MAKER_USE_TAGS.get() && tagId != null) {
				var tag = new JsonObject();

				tag.addProperty("tag", tagId.toString());
				ingredients.add(tag);
			} else {
				if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && stack.hasTag()) {
					ingredients.add(StrictNBTIngredient.of(stack).toJson());
				} else {
					ingredients.add(Ingredient.of(stack).toJson());
				}
			}
		}

		object.add("ingredients", ingredients);

		var result = new JsonObject();

		result.addProperty("item", "");
		object.add("result", result);

		return GSON.toJson(object);
	}

	private static boolean isTable(BlockEntity tile) {
		return tile instanceof BasicTableTileEntity ||
				tile instanceof AdvancedTableTileEntity ||
				tile instanceof EliteTableTileEntity ||
				tile instanceof UltimateTableTileEntity ||
				tile instanceof AutoTableTileEntity ||
				tile instanceof EnderCrafterTileEntity ||
				tile instanceof FluxCrafterTileEntity;
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

	private enum TableType {
		TABLE("TableCrafting", "extendedcrafting:shaped_table", "extendedcrafting:shapeless_table"),
		ENDER_CRAFTER("EnderCrafting", "extendedcrafting:shaped_ender_crafter", "extendedcrafting:shapeless_ender_crafter"),
		FLUX_CRAFTER("FluxCrafting","extendedcrafting:shaped_flux_crafter", "extendedcrafting:shapeless_flux_crafter");

		private static final Map<String, TableType> LOOKUP = new HashMap<>();

		static {
			for (var value : values()) {
				LOOKUP.put(value.type, value);
			}
		}

		public final String type;
		public final String shapedRecipeType;
		public final String shapelessRecipeType;

		TableType(String type, String shapedRecipeType, String shapelessRecipeType) {
			this.type = type;
			this.shapedRecipeType = shapedRecipeType;
			this.shapelessRecipeType = shapelessRecipeType;
		}

		public static TableType fromType(String type) {
			return LOOKUP.get(type);
		}
	}
}
