package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.client.gui.automationinterface.GuiAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileCompressor;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockAutomationInterface extends BlockBase implements ITileEntityProvider {

	public BlockAutomationInterface() {
		super("ec.interface", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.tabExtendedCrafting);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileAutomationInterface tile = (TileAutomationInterface) world.getTileEntity(pos);
		if (tile instanceof TileAutomationInterface) {
			for (int i = 0; i < tile.getSizeInventory(); i++) {
				ItemStack stack = tile.getStackInSlot(i);
				if (!stack.isEmpty()) {
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
			}
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAutomationInterface();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(ExtendedCrafting.instance, GuiHandler.AUTOMATION_INTERFACE, world, pos.getX(), pos.getY(), pos.getZ());
			TileEntity tile = world.getTileEntity(pos);
			/*if (tile instanceof TileAutomationInterface) { // TODO: DEBUG REMOVE OR DIE
				TileAutomationInterface compressor = (TileAutomationInterface) tile;
				if (compressor.getEnergy().receiveEnergy(100000, false) > 0) {
					player.sendMessage(new TextComponentString("it worked apparently"));
				}
			} */
		}
		return true;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
