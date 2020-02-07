package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.tileentity.EliteTableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class EliteTableBlock extends BaseBlock implements IEnableable {
	public static final VoxelShape ELITE_TABLE_SHAPE = new VoxelShapeBuilder()
			.cuboid(14.0, 2.0, 14.0, 2.0, 0.0, 2.0)
			.cuboid(9.0, 10.0, 4.0, 7.0, 2.0, 2.0)
			.cuboid(9.0, 10.0, 14.0, 7.0, 2.0, 12.0)
			.cuboid(14.0, 10.0, 9.0, 12.0, 2.0, 7.0)
			.cuboid(4.0, 10.0, 9.0, 2.0, 2.0, 7.0)
			.cuboid(16.0, 16.0, 16.0, 0.0, 10.0, 0.0)
			.build();

	public EliteTableBlock() {
		super(Material.IRON, SoundType.METAL, 5.0F, 10.0F);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new EliteTableTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (!world.isRemote()) {
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof EliteTableTileEntity)
				player.openContainer((EliteTableTileEntity) tile);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof EliteTableTileEntity) {
			EliteTableTileEntity table = (EliteTableTileEntity) tile;
			InventoryHelper.dropItems(world, pos, table.getInventory().getStacks());
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(ModTooltips.TIER.args(3).build());
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_TABLES.get();
	}
}
