package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.item.BaseItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;

public class HandheldTableItem extends BaseItem {
	public HandheldTableItem(Identifier id) {
		super(id, p -> p.stacksTo(1));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			player.openMenu(this.getContainer(level, player.blockPosition()));
		}

		return super.use(level, player, hand);
	}

	private MenuProvider getContainer(Level level, BlockPos pos) {
		return new SimpleMenuProvider((windowId, playerInventory, _) -> new CraftingMenu(windowId, playerInventory, ContainerLevelAccess.create(level, pos)) {
            @Override
            public boolean stillValid(Player player) {
                return true;
            }
        }, Component.translatable("container.crafting"));
	}
}
