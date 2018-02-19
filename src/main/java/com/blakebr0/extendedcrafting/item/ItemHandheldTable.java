package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHandheldTable extends ItemBase {

	public ItemHandheldTable() {
		super("ec.handheld_table");
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		BlockPos pos = player.getPosition();
		if (!world.isRemote) {
			player.openGui(ExtendedCrafting.instance, GuiHandler.HANDHELD_TABLE, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return super.onItemRightClick(world, player, hand);
	}
}
