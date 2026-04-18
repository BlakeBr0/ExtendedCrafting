package com.blakebr0.extendedcrafting.singularity;

import com.blakebr0.extendedcrafting.init.ModDataComponentTypes;
import com.blakebr0.extendedcrafting.init.ModItems;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public final class SingularityUtils {
    public static ItemStackTemplate getItemForSingularity(Singularity singularity) {
        var components = DataComponentPatch.builder().set(ModDataComponentTypes.SINGULARITY_ID.get(), singularity.getId()).build();
        return new ItemStackTemplate(ModItems.SINGULARITY.get(), components);
    }

    public static Singularity getSingularity(ItemStack stack) {
        var id = stack.get(ModDataComponentTypes.SINGULARITY_ID);
        if (id != null) {
            return SingularityRegistry.getInstance().getSingularityById(id);
        }

        return null;
    }
}
