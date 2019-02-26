package com.blakebr0.extendedcrafting.tile;

public class TileUltimaterInterface extends TileAutomationInterface {
	
	public int iterations = 2;
	
	@Override
	public void update() {
		
		super.update();
		
		for( int i = 1; i < iterations; i++)
		{
			super.ticks--;
			super.update();
		}
	}
}
