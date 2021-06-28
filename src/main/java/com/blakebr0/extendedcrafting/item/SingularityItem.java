package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.lib.ModTooltips;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import com.blakebr0.extendedcrafting.singularity.SingularityUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Function;

import net.minecraft.item.Item.Properties;

public class SingularityItem extends BaseItem implements IEnableable, IColored {
	public SingularityItem(Function<Properties, Properties> properties) {
		super(properties.compose(p -> p.rarity(Rarity.UNCOMMON)));
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isEnabled() && this.allowdedIn(group)) {
			SingularityRegistry.getInstance().getSingularities().forEach(singularity -> {
				items.add(SingularityUtils.getItemForSingularity(singularity));
			});
		}
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		Singularity singularity = SingularityUtils.getSingularity(stack);
		if (singularity == null) {
			return Localizable.of(this.getDescriptionId(stack)).args("NULL").build();
		}

		return Localizable.of(this.getDescriptionId(stack)).args(singularity.getDisplayName()).build();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		Singularity singularity = SingularityUtils.getSingularity(stack);
		if (singularity != null) {
			String modid = singularity.getId().getNamespace();
			if (!modid.equals(ExtendedCrafting.MOD_ID))
				tooltip.add(ModTooltips.getAddedByTooltip(modid));

			if (flag.isAdvanced())
				tooltip.add(ModTooltips.SINGULARITY_ID.args(singularity.getId()).color(TextFormatting.DARK_GRAY).build());
		}
	}

	//	@Override
//	public void init() {
//		addSingularity(0, "coal", new ItemStack(Items.COAL), 0x1B1B1B);
//		addSingularity(1, "iron", "ingotIron", 0x969696);
//		addSingularity(2, "lapis_lazuli", new ItemStack(Items.DYE, 1, 4), 0x345EC3);
//		addSingularity(3, "redstone", new ItemStack(Items.REDSTONE), 0x720000);
//		addSingularity(4, "glowstone", new ItemStack(Items.GLOWSTONE_DUST), 0x868600);
//		addSingularity(5, "gold", "ingotGold", 0xDEDE00);
//		addSingularity(6, "diamond", "gemDiamond", 0x2CCDB1);
//		addSingularity(7, "emerald", "gemEmerald", 0x00A835);
//
//		addSingularity(16, "aluminum", "ingotAluminum", 0xCACCDA);
//		addSingularity(17, "copper", "ingotCopper", 0xCE7201);
//		addSingularity(18, "tin", "ingotTin", 0x7690A5);
//		addSingularity(19, "bronze", "ingotBronze", 0xA87544);
//		addSingularity(20, "zinc", "ingotZinc", 0xCFD2CC);
//		addSingularity(21, "brass", "ingotBrass", 0xBC8B22);
//		addSingularity(22, "silver", "ingotSilver", 0x83AAB2);
//		addSingularity(23, "lead", "ingotLead", 0x484F67);
//		addSingularity(24, "steel", "ingotSteel", 0x565656);
//		addSingularity(25, "nickel", "ingotNickel", 0xBEB482);
//		addSingularity(26, "constantan", "ingotConstantan", 0xA98544);
//		addSingularity(27, "electrum", "ingotElectrum", 0xA79135);
//		addSingularity(28, "invar", "ingotInvar", 0x929D97);
//		addSingularity(29, "mithril", "ingotMithril", 0x659ABB);
//		addSingularity(30, "tungsten", "ingotTungsten", 0x494E51);
//		addSingularity(31, "titanium", "ingotTitanium", 0xA6A7B8);
//		addSingularity(32, "uranium", "ingotUranium", 0x46800D);
//		addSingularity(33, "chrome", "ingotChrome", 0xC1A9AE);
//		addSingularity(34, "platinum", "ingotPlatinum", 0x6FEAEF);
//		addSingularity(35, "iridium", "ingotIridium", 0x949FBE);
//
//		addSingularity(48, "signalum", "ingotSignalum", 0xDD3B00);
//		addSingularity(49, "lumium", "ingotLumium", 0xDEE59C);
//		addSingularity(50, "enderium", "ingotEnderium", 0x0B4D4E);
//
//		addSingularity(64, "ardite", "ingotArdite", 0xDA4817);
//		addSingularity(65, "cobalt", "ingotCobalt", 0x023C9B);
//		addSingularity(66, "manyullyn", "ingotManyullyn", 0x5C268A);
//
//		this.config.get("singularity", "default_singularities", new String[0]).setComment("Disable specific default singularities here.");
//
//		if (this.config.hasChanged()) {
//			this.config.save();
//		}
//	}
	
	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_SINGULARITIES.get();
	}

	@Override
	public int getColor(int i, ItemStack stack) {
		Singularity singularity = SingularityUtils.getSingularity(stack);
		if (singularity == null)
			return -1;

		return i == 0 ? singularity.getUnderlayColor() : i == 1 ? singularity.getOverlayColor() : -1;
	}
}
