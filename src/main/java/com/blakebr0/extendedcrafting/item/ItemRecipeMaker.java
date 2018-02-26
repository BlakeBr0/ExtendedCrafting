package com.blakebr0.extendedcrafting.item;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.compat.jei.CompatJEI;
import com.blakebr0.extendedcrafting.lib.IExtendedTable;
import com.blakebr0.extendedcrafting.tile.TileEnderCrafter;
import com.blakebr0.extendedcrafting.util.NetworkThingy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

public class ItemRecipeMaker extends ItemBase {

	public ItemRecipeMaker() {
		super("ec.recipe_maker");
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		
		if (Loader.isModLoaded("jei")) {
			CompatJEI.items.add(this);
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack) + " (" + getModeString(stack) + ")";
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			NBTTagCompound tag = new NBTTagCompound();
			
			ItemStack shaped = new ItemStack(this);
			tag.setBoolean("Shapeless", false);
			shaped.setTagCompound(tag);
			items.add(shaped);
			
			ItemStack shapeless = new ItemStack(this);
			tag = new NBTTagCompound();
			tag.setBoolean("Shapeless", true);
			shapeless.setTagCompound(tag);
			items.add(shapeless);
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
				player.sendMessage(new TextComponentTranslation("Copied recipe to clipboard"));
			}
			
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	private void setClipboard(TileEntity table, ItemStack stack) {
		if (Desktop.isDesktopSupported()) {
			String string = "mods.extendedcrafting." + (table instanceof TileEnderCrafter ? "EnderCrafting" : "TableCrafting");
			
			if (isShapeless(stack)) {
				string += ".addShapeless(0, <>, [" + makeItemArrayShapeless((IExtendedTable) table);
			} else {
				string += ".addShaped(0, <>, [" + makeItemArrayShaped((IExtendedTable) table);
			}
			
			string += "]);";
			
			StringSelection stringSelection = new StringSelection(string);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
	}
	
	private String makeItemArrayShaped(IExtendedTable table) {
		String string = "";
		IItemHandlerModifiable matrix = table.getMatrix();
		for (int i = 0; i < matrix.getSlots(); i++) {
			int sr = (int) Math.sqrt(matrix.getSlots());
			if (i == 0 || i % sr == 0) {
				string += "[";
			}

			ItemStack stack = matrix.getStackInSlot(i);
			
			String item = "";
			if (!stack.isEmpty()) {
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
			} else {
				string += item;
			}
			
			if ((i + 1) % sr != 0) {
				string += ", ";
			}
			
			if (i + 1 == sr || (i + 1) % sr == 0) {
				string += "]";
				if (i + 1 < matrix.getSlots()) {
					string += ", ";
				}
			}
		}
		return string;
	}
	
	private String makeItemArrayShapeless(IExtendedTable table) {
		String string = "";
		IItemHandlerModifiable matrix = table.getMatrix();
		for (int i = 0; i < matrix.getSlots(); i++) {
			ItemStack stack = matrix.getStackInSlot(i);
			if (!stack.isEmpty()) {
				int[] oreIds = OreDictionary.getOreIDs(stack);
				String item = "";
				if (oreIds.length > 0) {
					item = "ore:" + OreDictionary.getOreName(oreIds[0]);
				} else {
					String reg = stack.getItem().getRegistryName().toString();
					item = stack.isEmpty() ? "null" : reg;
					if (!stack.isEmpty() && stack.getMetadata() > 0) {
						item += ":" + stack.getMetadata();
					}
				}
					
				string += "<" + item + ">";
				
				if (i + 1 < matrix.getSlots()) {
					string += ", ";
				}
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
