package com.blakebr0.extendedcrafting.lib;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;

public class PedestalPosition {

	private BlockPos pos;
	
	public PedestalPosition(BlockPos pos){
		this.pos = pos;
	}
	
	public BlockPos getPos(){
		return this.pos;
	}
	
	public enum Pedestals {
		PEDESTALS {
			@Override
			public void setupPositions(){
				pedestalPositions.add(new PedestalPosition(new BlockPos(2, 0, 0)));
				pedestalPositions.add(new PedestalPosition(new BlockPos(2, 0, 2)));
				pedestalPositions.add(new PedestalPosition(new BlockPos(0, 0, 2)));
				pedestalPositions.add(new PedestalPosition(new BlockPos(-2, 0, 2)));
				pedestalPositions.add(new PedestalPosition(new BlockPos(-2, 0, 0)));
				pedestalPositions.add(new PedestalPosition(new BlockPos(-2, 0, -2)));
				pedestalPositions.add(new PedestalPosition(new BlockPos(0, 0, -2)));
				pedestalPositions.add(new PedestalPosition(new BlockPos(2, 0, -2)));
			}
		};
		
		public ArrayList<PedestalPosition> pedestalPositions = new ArrayList<PedestalPosition>();
		
		public void setupPositions(){
			
		}
	}
}
