package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;

public class HandheldTableItem extends BaseItem implements IEnableable {
	public HandheldTableItem(Function<Properties, Properties> properties) {
		super(properties.compose(p -> p.maxStackSize(1)));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote()) {
			player.openContainer(this.getContainer(world, player.getPosition()));
		}

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public boolean isEnabled() {
		return ModConfigs.ENABLE_HANDHELD_WORKBENCH.get();
	}

	private INamedContainerProvider getContainer(World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((windowId, playerInventory, playerEntity) -> {
			return new WorkbenchContainer(windowId, playerInventory, IWorldPosCallable.of(world, pos));
		}, Localizable.of("container.crafting").build());
	}
}
