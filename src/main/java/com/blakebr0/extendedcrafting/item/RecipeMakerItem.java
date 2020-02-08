package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Function;

public class RecipeMakerItem extends BaseItem implements IEnableable {
	private static final String NEW_LINE = System.lineSeparator() + "\t";

	public RecipeMakerItem(Function<Properties, Properties> properties) {
		super(properties.compose(p -> p.maxStackSize(1)));
	}
	
	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_RECIPE_MAKER.get();
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack stack = new ItemStack(this);
			NBTHelper.setBoolean(stack, "Shapeless", false);
			items.add(stack);
		}
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		BlockPos pos = context.getPos();
		Direction facing = context.getFace();
		if (player == null || !player.canPlayerEdit(pos.offset(facing), facing, stack))
			return ActionResultType.PASS;

		// TODO: Implement recipe maker functionality
//		TileEntity tile = world.getTileEntity(pos);
//		if (tile != null && tile instanceof IExtendedTable) {
//			if (world.isRemote) {
//				setClipboard(tile, stack);
//				player.sendMessage(new TextComponentTranslation("message.ec.copied_recipe"));
//
//				if (ModConfigs.confRMNBT && !Loader.isModLoaded("crafttweaker")) {
//					player.sendMessage(new TextComponentTranslation("message.ec.nbt_requires_crafttweaker"));
//				}
//			}
//
//			return ActionResultType.SUCCESS;
//		}

		return ActionResultType.PASS;	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (player.isCrouching()) {
			ItemStack stack = player.getHeldItem(hand);
			NBTHelper.flipBoolean(stack, "Shapeless");

			if (world.isRemote()) {
				player.sendMessage(ModTooltips.CHANGED_MODE.args(this.getModeString(stack)).build());
			}
		}

		return super.onItemRightClick(world, player, hand);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(ModTooltips.MODE.args(this.getModeString(stack)).build());
	}

//	private void setClipboard(TileEntity table, ItemStack stack) {
//		if (Desktop.isDesktopSupported()) {
//			String string = "mods.extendedcrafting." + (table instanceof EnderCrafterTileEntity ? "EnderCrafting" : "TableCrafting");
//
//			if (isShapeless(stack)) {
//				string += ".addShapeless(0, <>, [" + makeItemArrayShapeless((IExtendedTable) table);
//			} else {
//				string += ".addShaped(0, <>, [" + NEW_LINE + makeItemArrayShaped((IExtendedTable) table);
//			}
//
//			string += "]);";
//
//			StringSelection stringSelection = new StringSelection(string);
//			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//			clipboard.setContents(stringSelection, null);
//		}
//	}
//
//	private String makeItemArrayShaped(IExtendedTable table) {
//		String string = "";
//		NonNullList<ItemStack> matrix = table.getMatrix();
//		for (int i = 0; i < matrix.size(); i++) {
//			int sr = (int) Math.sqrt(matrix.size());
//			if (i == 0 || i % sr == 0) {
//				string += "[";
//			}
//
//			ItemStack stack = matrix.get(i);
//
//			String item = "";
//			if (ModConfigs.confRMOredict && !stack.isEmpty()) {
//				int[] oreIds = OreDictionary.getOreIDs(stack);
//				if (oreIds.length > 0) {
//					item = "ore:" + OreDictionary.getOreName(oreIds[0]);
//				}
//			}
//
//			if (item.isEmpty()) {
//				String reg = stack.getItem().getRegistryName().toString();
//				item = stack.isEmpty() ? "null" : reg;
//				if (!stack.isEmpty() && stack.getMetadata() > 0) {
//					item += ":" + stack.getMetadata();
//				}
//			}
//
//			if (!item.equalsIgnoreCase("null")) {
//				string += "<" + item + ">";
//
//				if (ModConfigs.confRMNBT && !stack.isEmpty() && stack.hasTagCompound() && Loader.isModLoaded("crafttweaker")) {
//					NBTBase nbt = stack.serializeNBT().getTag("tag");
//					String tag = CraftTweakerUtils.writeTag(nbt);
//					string += ".withTag(" + tag + ")";
//				}
//			} else {
//				string += item;
//			}
//
//			if ((i + 1) % sr != 0) {
//				string += ", ";
//			}
//
//			if (i + 1 == sr || (i + 1) % sr == 0) {
//				string += "]";
//				if (i + 1 < matrix.size()) {
//					string += ", ";
//					string += NEW_LINE;
//				} else {
//					string += System.lineSeparator();
//				}
//			}
//		}
//
//		return string;
//	}
//
//	private String makeItemArrayShapeless(IExtendedTable table) {
//		String string = "";
//		NonNullList<ItemStack> matrix = table.getMatrix();
//		ArrayList<Integer> slots = new ArrayList<>();
//		int lastSlot = 0;
//		for (int i = 0; i < matrix.size(); i++) {
//			ItemStack stack = matrix.get(i);
//			if (!stack.isEmpty()) {
//				slots.add(i);
//				lastSlot = i;
//			}
//		}
//
//		for (int i : slots) {
//			ItemStack stack = matrix.get(i);
//			int[] oreIds = OreDictionary.getOreIDs(stack);
//			String item = "";
//			if (ModConfigs.confRMOredict && oreIds.length > 0) {
//				item = "ore:" + OreDictionary.getOreName(oreIds[0]);
//			} else {
//				String reg = stack.getItem().getRegistryName().toString();
//				item = stack.isEmpty() ? "null" : reg;
//				if (!stack.isEmpty() && stack.getMetadata() > 0) {
//					item += ":" + stack.getMetadata();
//				}
//			}
//
//			string += "<" + item + ">";
//
//			if (ModConfigs.confRMNBT && !stack.isEmpty() && stack.hasTagCompound() && Loader.isModLoaded("crafttweaker")) {
//				NBTBase nbt = stack.serializeNBT().getTag("tag");
//				String tag = CraftTweakerUtils.writeTag(nbt);
//				string += ".withTag(" + tag + ")";
//			}
//
//			if (i != lastSlot) {
//				string += ", ";
//			}
//		}
//
//		return string;
//	}
//
	private String getModeString(ItemStack stack) {
		return this.isShapeless(stack) ? "Shapeless" : "Shaped";
	}

	private boolean isShapeless(ItemStack stack) {
		return NBTHelper.getBoolean(stack, "Shapeless");
	}
}
