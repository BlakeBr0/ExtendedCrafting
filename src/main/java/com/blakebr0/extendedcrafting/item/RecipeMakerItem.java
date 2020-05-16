package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class RecipeMakerItem extends BaseItem implements IEnableable {
	private static final String NEW_LINE = System.lineSeparator() + "\t";

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

				}

				player.sendMessage(Localizable.of("message.extendedcrafting.copied_recipe").build());

				if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !ModList.get().isLoaded("crafttweaker")) {
					player.sendMessage(Localizable.of("message.extendedcrafting.nbt_requires_crafttweaker").build());
				}
			}

			return ActionResultType.SUCCESS;
		} else if (tile instanceof CraftingCoreTileEntity) {
			if (world.isRemote()) {
				String type = NBTHelper.getString(stack, "Type");
				CraftingCoreTileEntity core = (CraftingCoreTileEntity) tile;
				if ("CraftTweaker".equals(type)) {
					String string = makeCraftTweakerCombinationRecipe(core);

					setClipboard(string);
				} else {

				}

				player.sendMessage(Localizable.of("message.extendedcrafting.copied_recipe").build());
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
				player.sendMessage(Localizable.of("message.extendedcrafting.changed_mode").args(getModeString(stack)).build());
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

		int slots = clampTableSlots(inventory.getSlots());
		for (int i = 0; i < slots; i++) {
			int sr = (int) Math.sqrt(slots);
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

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && ModList.get().isLoaded("crafttweaker")) {
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
		int slots = clampTableSlots(inventory.getSlots());
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

			if (ModConfigs.RECIPE_MAKER_USE_NBT.get() && !stack.isEmpty() && stack.hasTag() && ModList.get().isLoaded("crafttweaker")) {
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
			ResourceLocation id = stacks[i].getItem().getRegistryName();
			String item = id == null ? "item:minecraft:air" : "item:" + id.toString();

			string.append("<").append(item).append(">");

			if (i != stacks.length - 1) {
				string.append(", ");
			}
		}

		string.append(System.lineSeparator()).append("]);");

		return string.toString();
	}

	private static boolean isTable(TileEntity tile) {
		return tile instanceof BasicTableTileEntity ||
				tile instanceof AdvancedTableTileEntity ||
				tile instanceof EliteTableTileEntity ||
				tile instanceof UltimateTableTileEntity ||
				tile instanceof AutoTableTileEntity ||
				tile instanceof EnderCrafterTileEntity;
	}

	private static int clampTableSlots(int slots) {
		return slots % 9 != 0 ? slots - 1 : slots;
	}

	private static String getModeString(ItemStack stack) {
		return isShapeless(stack) ? "Shapeless" : "Shaped";
	}

	private static boolean isShapeless(ItemStack stack) {
		return NBTHelper.getBoolean(stack, "Shapeless");
	}
}
