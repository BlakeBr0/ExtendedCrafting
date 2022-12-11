package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnderAlternatorBlock extends BaseBlock {
	private static final VoxelShape ENDER_ALTERNATOR_SHAPE = new VoxelShapeBuilder()
			.cuboid(0, 0, 0, 16, 6, 16)
			.cuboid(4, 6, 4, 12, 15, 12)
			.cuboid(3, 15, 3, 13, 16, 13)
			.build();

	public EnderAlternatorBlock() {
		super(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return ENDER_ALTERNATOR_SHAPE;
	}
}
