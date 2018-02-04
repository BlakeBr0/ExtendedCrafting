package com.blakebr0.extendedcrafting.crafting.table;

import net.minecraftforge.items.IItemHandlerModifiable;

public interface ITieredRecipe {

	int getTier();
	boolean matches(IItemHandlerModifiable grid);
}
