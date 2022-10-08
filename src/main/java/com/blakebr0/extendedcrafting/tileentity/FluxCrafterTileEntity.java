package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.block.EnderAlternatorBlock;
import com.blakebr0.extendedcrafting.block.FluxAlternatorBlock;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.container.FluxCrafterContainer;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class FluxCrafterTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final BaseItemStackHandler inventory;
	private final BaseItemStackHandler recipeInventory;
	private final EnergyStorage energy;
	private IFluxCrafterRecipe recipe;
	private int progress;
	private int progressReq;
	private boolean isGridChanged = true;

	public FluxCrafterTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.ENDER_CRAFTER.get(), pos, state);
		this.inventory = createInventoryHandler(this::onContentsChanged);
		this.recipeInventory = new BaseItemStackHandler(9);
		this.energy = new EnergyStorage(100);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.progress = tag.getInt("Progress");
		this.progressReq = tag.getInt("ProgressReq");
		this.energy.deserializeNBT(tag.get("Energy"));
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("Progress", this.progress);
		tag.putInt("ProgressReq", this.progressReq);
		tag.putInt("Energy", this.energy.getEnergyStored());
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.flux_crafter").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return FluxCrafterContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, new SimpleContainerData(0), this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, FluxCrafterTileEntity tile) {
		var mark = false;

		tile.updateRecipeInventory();

		var recipeInventory = tile.recipeInventory.toIInventory();

		if (tile.isGridChanged && (tile.recipe == null || !tile.recipe.matches(recipeInventory, level))) {
			tile.recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.FLUX_CRAFTER.get(), recipeInventory, level).orElse(null);
		}

		if (!level.isClientSide()) {
			if (tile.recipe != null) {
				var result = tile.recipe.assemble(recipeInventory);
				var output = tile.inventory.getStackInSlot(9);

				if (StackHelper.canCombineStacks(result, output)) {
					var alternators = tile.getAlternatorPositions();
					int alternatorCount = alternators.size();

					if (alternatorCount > 0) {
						tile.progress(alternatorCount, tile.recipe.getPowerRate());

						for (var alternatorPos : alternators) {
							if (level.isEmptyBlock(alternatorPos.above())) {
								tile.spawnParticles(ParticleTypes.PORTAL, alternatorPos, 1, 1);
							}
						}

						if (tile.progress >= tile.progressReq) {
							for (int i = 0; i < tile.inventory.getSlots() - 1; i++) {
								tile.inventory.extractItemSuper(i, 1, false);
							}

							tile.updateResult(result);
							tile.progress = 0;
						}

						mark = true;
					}
				} else {
					if (tile.progress > 0 || tile.progressReq > 0) {
						tile.progress = 0;
						tile.progressReq = 0;

						mark = true;
					}
				}
			} else {
				if (tile.progress > 0 || tile.progressReq > 0) {
					tile.progress = 0;
					tile.progressReq = 0;

					mark = true;
				}
			}

			if (mark) {
				tile.markDirtyAndDispatch();
			}
		}
	}

	public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
		var inventory = new BaseItemStackHandler(10, onContentsChanged);

		inventory.setOutputSlots(9);
		inventory.setSlotValidator((slot, stack) -> false);

		return inventory;
	}

	private void updateResult(ItemStack stack) {
		var result = this.inventory.getStackInSlot(9);

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
		var level = this.getLevel();

		if (level != null) {
			var pos = this.getBlockPos();

			BlockPos.betweenClosedStream(pos.offset(-3, -3, -3), pos.offset(3, 3, 3)).forEach(aoePos -> {
				var block = level.getBlockState(aoePos).getBlock();
				if (block instanceof FluxAlternatorBlock)
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
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		var level = (ServerLevel) this.getLevel();

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D;

		level.sendParticles(particle, x, y, z, count, 0, 0, 0, 0.1D);
	}

	private void onContentsChanged() {
		this.isGridChanged = true;
		this.markDirtyAndDispatch();
	}

	public int getProgress() {
		return this.progress;
	}

	public int getProgressRequired() {
		return this.progressReq;
	}

	public IFluxCrafterRecipe getActiveRecipe() {
		return this.recipe;
	}
}
