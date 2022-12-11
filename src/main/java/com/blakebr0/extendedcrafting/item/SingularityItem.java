package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class SingularityItem extends BaseItem implements IColored {
	public SingularityItem() {
		super(p -> p.rarity(Rarity.UNCOMMON));
	}

	@Override
	public Component getName(ItemStack stack) {
		var singularity = SingularityUtils.getSingularity(stack);

		if (singularity == null) {
			return Localizable.of(this.getDescriptionId(stack)).args("NULL").build();
		}

		return Localizable.of(this.getDescriptionId(stack)).args(singularity.getDisplayName()).build();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		var singularity = SingularityUtils.getSingularity(stack);

		if (singularity != null) {
			var modid = singularity.getId().getNamespace();

			if (!modid.equals(ExtendedCrafting.MOD_ID))
				tooltip.add(ModTooltips.getAddedByTooltip(modid));

			if (flag.isAdvanced())
				tooltip.add(ModTooltips.SINGULARITY_ID.args(singularity.getId()).color(ChatFormatting.DARK_GRAY).build());
		}
	}

	@Override
	public int getColor(int i, ItemStack stack) {
		var singularity = SingularityUtils.getSingularity(stack);

		if (singularity == null)
			return -1;

		return i == 0 ? singularity.getUnderlayColor() : i == 1 ? singularity.getOverlayColor() : -1;
	}
}
