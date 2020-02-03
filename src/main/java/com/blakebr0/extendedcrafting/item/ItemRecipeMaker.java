package com.blakebr0.extendedcrafting.item;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CraftTweakerUtils;
import com.blakebr0.extendedcrafting.compat.jei.JeiCompat;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;
import com.blakebr0.extendedcrafting.tileentity.EnderCrafterTileEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class ItemRecipeMaker extends ItemBase implements IEnableable {
	
	private static final String NEW_LINE = System.lineSeparator() + "\t";

	public ItemRecipeMaker() {
		super("ec.recipe_maker");
		this.setCreativeTab(ExtendedCrafting.ITEM_GROUP);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		
		if (Loader.isModLoaded("jei") && this.isEnabled()) {
			JeiCompat.items.add(this);
		}
	}
	
	@Override
	public boolean isEnabled() {
		return ModConfigs.confRMEnabled;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			NBTHelper.setBoolean(stack, "Shapeless", false);
			items.add(stack);
		}
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack))
			return EnumActionResult.PASS;

		TileEntity tile = world.getTileEntity(pos);

		if (tile != null && tile instanceof IExtendedTable) {
			if (world.isRemote) {
				setClipboard(tile, stack);
				player.sendMessage(new TextComponentTranslation("message.ec.copied_recipe"));
				
				if (ModConfigs.confRMNBT && !Loader.isModLoaded("crafttweaker")) {
					player.sendMessage(new TextComponentTranslation("message.ec.nbt_requires_crafttweaker"));
				}
			}
			
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		
		if (player.isSneaking()) {
			NBTHelper.flipBoolean(stack, "Shapeless");
			
			if (world.isRemote) {
				player.sendMessage(new TextComponentTranslation("message.ec.changed_mode", getModeString(stack)));
			}
		}
		
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(Utils.localize("tooltip.ec.mode", getModeString(stack)));
	}

	private void setClipboard(TileEntity table, ItemStack stack) {
		if (Desktop.isDesktopSupported()) {
			String string = "mods.extendedcrafting." + (table instanceof EnderCrafterTileEntity ? "EnderCrafting" : "TableCrafting");
			
			if (isShapeless(stack)) {
				string += ".addShapeless(0, <>, [" + makeItemArrayShapeless((IExtendedTable) table);
			} else {
				string += ".addShaped(0, <>, [" + NEW_LINE + makeItemArrayShaped((IExtendedTable) table);
			}
			
			string += "]);";
			
			StringSelection stringSelection = new StringSelection(string);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
	}
	
	private String makeItemArrayShaped(IExtendedTable table) {
		String string = "";
		NonNullList<ItemStack> matrix = table.getMatrix();
		for (int i = 0; i < matrix.size(); i++) {
			int sr = (int) Math.sqrt(matrix.size());
			if (i == 0 || i % sr == 0) {
				string += "[";
			}

			ItemStack stack = matrix.get(i);
			
			String item = "";
			if (ModConfigs.confRMOredict && !stack.isEmpty()) {
				int[] oreIds = OreDictionary.getOreIDs(stack);
				if (oreIds.length > 0) {
					item = "ore:" + OreDictionary.getOreName(oreIds[0]);
				}
			}
			
			if (item.isEmpty()) {
				String reg = stack.getItem().getRegistryName().toString();
				item = stack.isEmpty() ? "null" : reg;
				if (!stack.isEmpty() && stack.getMetadata() > 0) {
					item += ":" + stack.getMetadata();
				}
			}
			
			if (!item.equalsIgnoreCase("null")) {
				string += "<" + item + ">";
				
				if (ModConfigs.confRMNBT && !stack.isEmpty() && stack.hasTagCompound() && Loader.isModLoaded("crafttweaker")) {
					NBTBase nbt = stack.serializeNBT().getTag("tag");
					String tag = CraftTweakerUtils.writeTag(nbt);
					string += ".withTag(" + tag + ")";
				}
			} else {
				string += item;
			}
			
			if ((i + 1) % sr != 0) {
				string += ", ";
			}
			
			if (i + 1 == sr || (i + 1) % sr == 0) {
				string += "]";
				if (i + 1 < matrix.size()) {
					string += ", ";
					string += NEW_LINE;
				} else {
					string += System.lineSeparator();
				}
			}
		}
		
		return string;
	}
	
	private String makeItemArrayShapeless(IExtendedTable table) {
		String string = "";
		NonNullList<ItemStack> matrix = table.getMatrix();
		ArrayList<Integer> slots = new ArrayList<>();
		int lastSlot = 0;
		for (int i = 0; i < matrix.size(); i++) {
			ItemStack stack = matrix.get(i);
			if (!stack.isEmpty()) {
				slots.add(i);
				lastSlot = i;
			}
		}
		
		for (int i : slots) {
			ItemStack stack = matrix.get(i);
			int[] oreIds = OreDictionary.getOreIDs(stack);
			String item = "";
			if (ModConfigs.confRMOredict && oreIds.length > 0) {
				item = "ore:" + OreDictionary.getOreName(oreIds[0]);
			} else {
				String reg = stack.getItem().getRegistryName().toString();
				item = stack.isEmpty() ? "null" : reg;
				if (!stack.isEmpty() && stack.getMetadata() > 0) {
					item += ":" + stack.getMetadata();
				}
			}
					
			string += "<" + item + ">";
			
			if (ModConfigs.confRMNBT && !stack.isEmpty() && stack.hasTagCompound() && Loader.isModLoaded("crafttweaker")) {
				NBTBase nbt = stack.serializeNBT().getTag("tag");
				String tag = CraftTweakerUtils.writeTag(nbt);
				string += ".withTag(" + tag + ")";
			}
			
			if (i != lastSlot) {
				string += ", ";
			}
		}
		
		return string;
	}
	
	private String getModeString(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		boolean shapeless = tag != null && tag.hasKey("Shapeless") ? tag.getBoolean("Shapeless") : false;
		return shapeless ? "Shapeless" : "Shaped";
	}
	
	private boolean isShapeless(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		boolean shapeless = tag != null && tag.hasKey("Shapeless") ? tag.getBoolean("Shapeless") : false;
		return shapeless;
	}
}
