package com.blakebr0.extendedcrafting.lib;

import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

	public CustomEnergyStorage(int capacity) {
		super(capacity);
	}

	public void setEnergy(int amount) {
		this.energy = amount;
	}
}
