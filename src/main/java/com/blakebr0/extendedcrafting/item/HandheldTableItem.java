package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class HandheldTableItem extends BaseItem implements IEnableable {
	public HandheldTableItem(Function<Properties, Properties> properties) {
		super(properties.compose(p -> p.stacksTo(1)));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide()) {
			player.openMenu(this.getContainer(world, player.blockPosition()));
		}

		return super.use(world, player, hand);
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_HANDHELD_WORKBENCH.get();
	}

	private MenuProvider getContainer(Level world, BlockPos pos) {
		return new SimpleMenuProvider((windowId, playerInventory, playerEntity) -> {
			return new CraftingMenu(windowId, playerInventory, ContainerLevelAccess.create(world, pos)) {
				@Override
				public boolean stillValid(Player player) {
					return true;
				}
			};
		}, Localizable.of("container.crafting").build());
	}
}
