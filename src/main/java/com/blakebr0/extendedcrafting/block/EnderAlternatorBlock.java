package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BaseBlock;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnderAlternatorBlock extends BaseBlock implements IEnableable {
	private static final VoxelShape ENDER_ALTERNATOR_SHAPE = new VoxelShapeBuilder()
			.cuboid(16.0, 6.0, 16.0, 0.0, 0.0, 0.0)
			.cuboid(12.0, 15.0, 12.0, 4.0, 6.0, 4.0)
			.cuboid(13.0, 16.0, 13.0, 3.0, 15.0, 3.0)
			.build();

	public EnderAlternatorBlock() {
		super(Material.METAL, SoundType.METAL, 5.0F, 10.0F, true);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return ENDER_ALTERNATOR_SHAPE;
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_ENDER_CRAFTER.get();
	}
}
