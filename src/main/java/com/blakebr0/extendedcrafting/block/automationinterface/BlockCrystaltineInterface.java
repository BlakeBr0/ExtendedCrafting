package com.blakebr0.extendedcrafting.block.automationinterface;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileUltimaterInterface;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCrystaltineInterface extends BlockBase implements ITileEntityProvider, IEnableable {

	public BlockCrystaltineInterface() {
		super("ec.interface_crystaltine", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		TileUltimaterInterface tile = new TileUltimaterInterface();
		tile.setRates(1,1);
		tile.iterations = 4;
		tile.powerMultiplier = 2;
		return (TileAutomationInterface)tile;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(ExtendedCrafting.instance, GuiHandler.CRYSTALTINE_INTERFACE, world, pos.getX(), pos.getY(), pos.getZ());
			TileEntity tile = world.getTileEntity(pos);
			if (ExtendedCrafting.DEBUG && tile instanceof TileAutomationInterface) {
				TileAutomationInterface compressor = (TileAutomationInterface) tile;
				compressor.getEnergy().receiveEnergy(100000, false);
			}
		}
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileAutomationInterface tile = (TileAutomationInterface) world.getTileEntity(pos);
		if (tile != null) {
			for (int i = 0; i < tile.getSizeInventory(); i++) {
				ItemStack stack = tile.getStackInSlot(i);
				this.spawnAsEntity(world, pos, stack);
			}
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return ModConfig.confInterfaceEnabled;
	}
}
