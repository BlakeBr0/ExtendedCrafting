package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.data.MapData;
import com.blamejared.crafttweaker.api.data.visitor.DataToTextComponentVisitor;
import net.minecraft.nbt.CompoundTag;

public final class CraftTweakerUtils {
	public static String writeTag(CompoundTag tag) {
        return (new MapData(tag).accept(new DataToTextComponentVisitor("", 0)).getString());
	}
}
