package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.container.FluxCrafterContainer;
import com.blakebr0.extendedcrafting.container.inventory.ExtendedCraftingInventory;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.util.EmptyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class FluxCrafterTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final BaseItemStackHandler inventory;
	private final BaseItemStackHandler recipeInventory;
	private IFluxCrafterRecipe recipe;
	private int progress;
	private int progressReq;
	protected boolean isGridChanged = true;

	public FluxCrafterTileEntity(BlockPos pos, BlockState state) {
		this(ModTileEntities.FLUX_CRAFTER.get(), pos, state);
	}

	public FluxCrafterTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.inventory = createInventoryHandler(this::onContentsChanged);
		this.recipeInventory = BaseItemStackHandler.create(9);
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
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("Progress", this.progress);
		tag.putInt("ProgressReq", this.progressReq);
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.flux_crafter").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return FluxCrafterContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, FluxCrafterTileEntity tile) {
		var mark = false;
		var recipe = tile.getActiveRecipe();
		var selectedRecipe = tile.getSelectedRecipeGrid();

		if (recipe != null && (selectedRecipe == null || recipe.matches(selectedRecipe, level))) {
			var result = recipe.assemble(tile.recipeInventory.toIInventory());
			var output = tile.inventory.getStackInSlot(9);

			if (StackHelper.canCombineStacks(result, output)) {
				var alternators = tile.getAlternators();
				int alternatorCount = alternators.size();

				if (alternatorCount > 0) {
					tile.progress(alternatorCount);

					for (var alternator : alternators) {
						var alternatorPos = alternator.getBlockPos();
						if (level.isEmptyBlock(alternatorPos.above())) {
							tile.spawnDustParticles(alternatorPos, 1, 1);
						}

						alternator.getEnergy().extractEnergy(recipe.getPowerRate(), false);
					}

					if (tile.progress >= tile.progressReq) {
						for (int i = 0; i < tile.inventory.getSlots() - 1; i++) {
							tile.inventory.setStackInSlot(i, StackHelper.shrink(tile.inventory.getStackInSlot(i), 1, false));
						}

						tile.updateResult(result);
						tile.progress = 0;
					}

					mark = true;
				}
			} else {
				if (tile.progress > 0 || tile.progressReq > 0) {
					tile.reset();

					mark = true;
				}
			}
		} else {
			if (tile.progress > 0 || tile.progressReq > 0) {
				tile.reset();

				mark = true;
			}
		}

		if (mark) {
			tile.markDirtyAndDispatch();
		}
	}

	public static BaseItemStackHandler createInventoryHandler() {
		return createInventoryHandler(null);
	}

	public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
		return BaseItemStackHandler.create(10, onContentsChanged, builder -> {
			builder.setOutputSlots(9);
			builder.setCanInsert((slot, stack) -> false);
		});
	}

	// to be overridden by the auto variant
	public TableRecipeStorage getRecipeStorage() {
		return null;
	}

	private CraftingContainer getSelectedRecipeGrid() {
		var storage = this.getRecipeStorage();
		if (storage != null) {
			var grid = storage.getSelectedRecipeGrid();
			if (grid != null) {
				return new ExtendedCraftingInventory(EmptyContainer.INSTANCE, storage.getSelectedRecipeGrid(), 3);
			}
		}

		return null;
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

	private List<FluxAlternatorTileEntity> getAlternators() {
		List<FluxAlternatorTileEntity> alternators = new ArrayList<>();
		var level = this.getLevel();

		if (level != null) {
			var pos = this.getBlockPos();

			BlockPos.betweenClosedStream(pos.offset(-3, -3, -3), pos.offset(3, 3, 3)).forEach(aoePos -> {
				var tile = level.getBlockEntity(aoePos);
				if (tile instanceof FluxAlternatorTileEntity alternator && alternator.getEnergy().getEnergyStored() >= this.recipe.getPowerRate())
					alternators.add(alternator);
			});
		}

		return alternators;
	}

	private void progress(int alternators) {
		this.progress += this.recipe.getPowerRate() * alternators;
		this.progressReq = this.recipe.getPowerRequired();
	}

	private void reset() {
		this.progress = 0;
		this.progressReq = 0;
	}

	private <T extends ParticleOptions> void spawnDustParticles(BlockPos pos, double yOffset, int count) {
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		var level = (ServerLevel) this.getLevel();

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D;

		level.sendParticles(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.0f), x, y, z, count, 0, 0, 0, 0.1D);
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

	public BaseItemStackHandler getRecipeInventory() {
		return this.recipeInventory;
	}

	public IFluxCrafterRecipe getActiveRecipe() {
		if (this.level == null)
			return null;

		this.updateRecipeInventory();

		var recipeInventory = this.recipeInventory.toIInventory();

		if (this.isGridChanged && (this.recipe == null || !this.recipe.matches(recipeInventory, level))) {
			this.recipe = level.getRecipeManager()
					.getRecipeFor(ModRecipeTypes.FLUX_CRAFTER.get(), recipeInventory, level)
					.orElse(null);
		}

		return this.recipe;
	}
}
