package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.block.EnderAlternatorBlock;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class EnderCrafterTileEntity extends BaseInventoryTileEntity implements TickableBlockEntity, MenuProvider {
	private final BaseItemStackHandler inventory;
	private final BaseItemStackHandler recipeInventory;
	private IEnderCrafterRecipe recipe;
	private int progress;
	private int progressReq;

	public EnderCrafterTileEntity() {
		super(ModTileEntities.ENDER_CRAFTER.get());
		this.inventory = new BaseItemStackHandler(10, this::markDirtyAndDispatch);
		this.recipeInventory = new BaseItemStackHandler(9);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		this.progress = tag.getInt("Progress");
		this.progressReq = tag.getInt("ProgressReq");
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag = super.save(tag);
		tag.putInt("Progress", this.progress);
		tag.putInt("ProgressReq", this.progressReq);

		return tag;
	}

	@Override
	public void tick() {
		boolean mark = false;
		Level world = this.getLevel();

		if (world != null) {
			this.updateRecipeInventory();
			Container recipeInventory = this.recipeInventory.toIInventory();
			if (this.recipe == null || !this.recipe.matches(recipeInventory, world)) {
				this.recipe = world.getRecipeManager().getRecipeFor(RecipeTypes.ENDER_CRAFTER, recipeInventory, world).orElse(null);
			}

			if (!world.isClientSide()) {
				if (this.recipe != null) {
					ItemStack result = this.recipe.assemble(recipeInventory);
					ItemStack output = this.inventory.getStackInSlot(9);
					if (StackHelper.canCombineStacks(result, output)) {
						List<BlockPos> alternators = this.getAlternatorPositions();
						int alternatorCount = alternators.size();

						if (alternatorCount > 0) {
							this.progress(alternatorCount, this.recipe.getCraftingTime());

							for (BlockPos pos : alternators) {
								if (world.isEmptyBlock(pos.above())) {
									this.spawnParticles(ParticleTypes.PORTAL, pos, 1, 1);
								}
							}

							if (this.progress >= this.progressReq) {
								for (int i = 0; i < this.inventory.getSlots() - 1; i++) {
									this.inventory.extractItem(i, 1, false);
								}

								this.updateResult(result);
								this.progress = 0;
							}

							mark = true;
						}
					} else {
						if (this.progress > 0 || this.progressReq > 0) {
							this.progress = 0;
							this.progressReq = 0;

							mark = true;
						}
					}
				} else {
					if (this.progress > 0 || this.progressReq > 0) {
						this.progress = 0;
						this.progressReq = 0;

						mark = true;
					}
				}
			}

			if (mark) {
				this.markDirtyAndDispatch();
			}
		}
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.ender_crafter").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return EnderCrafterContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, new SimpleContainerData(0), this.getBlockPos());
	}

	private void updateResult(ItemStack stack) {
		ItemStack result = this.inventory.getStackInSlot(9);
		if (result.isEmpty()) {
			this.inventory.setStackInSlot(9, stack);
		} else {
			this.inventory.setStackInSlot(9, StackHelper.grow(result, stack.getCount()));
		}
	}

	private void updateRecipeInventory() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = this.inventory.getStackInSlot(i);
			this.recipeInventory.setStackInSlot(i, stack);
		}
	}

	private List<BlockPos> getAlternatorPositions() {
		List<BlockPos> alternators = new ArrayList<>();
		Level world = this.getLevel();
		if (world != null) {
			BlockPos pos = this.getBlockPos();
			BlockPos.betweenClosedStream(pos.offset(-3, -3, -3), pos.offset(3, 3, 3)).forEach(aoePos -> {
				Block block = world.getBlockState(aoePos).getBlock();
				if (block instanceof EnderAlternatorBlock)
					alternators.add(aoePos.immutable());
			});
		}

		return alternators;
	}

	private void progress(int alternators, int timeRequired) {
		this.progress++;

		int timeReq = 20 * timeRequired;
		double effectiveness = ModConfigs.ENDER_CRAFTER_ALTERNATOR_EFFECTIVENESS.get();
		this.progressReq = (int) Math.max(timeReq - (timeReq * (effectiveness * alternators)), 20);
	}

	private <T extends ParticleOptions> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
		if (this.getLevel() == null || this.getLevel().isClientSide()) return;
		ServerLevel world = (ServerLevel) this.getLevel();

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D;

		world.sendParticles(particle, x, y, z, count, 0, 0, 0, 0.1D);
	}

	public int getProgress() {
		return this.progress;
	}

	public int getProgressRequired() {
		return this.progressReq;
	}

	public IEnderCrafterRecipe getActiveRecipe() {
		return this.recipe;
	}
}
