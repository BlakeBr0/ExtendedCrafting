package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class EnderAlternatorBlock extends BaseBlock implements IEnableable {
	private static final VoxelShape ENDER_ALTERNATOR_SHAPE = new VoxelShapeBuilder()
			.cuboid(16.0, 6.0, 16.0, 0.0, 0.0, 0.0)
			.cuboid(12.0, 15.0, 12.0, 4.0, 6.0, 4.0)
			.cuboid(13.0, 16.0, 13.0, 3.0, 15.0, 3.0)
			.build();

	public EnderAlternatorBlock() {
		super(Material.IRON, SoundType.METAL, 5.0F, 10.0F, ToolType.PICKAXE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return ENDER_ALTERNATOR_SHAPE;
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_ENDER_CRAFTER.get();
	}
}
