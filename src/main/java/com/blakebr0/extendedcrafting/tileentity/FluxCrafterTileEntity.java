package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.ContainerDataBuilder;
import com.blakebr0.extendedcrafting.api.crafting.IFluxCrafterRecipe;
import com.blakebr0.extendedcrafting.block.FluxAlternatorBlock;
import com.blakebr0.extendedcrafting.container.FluxCrafterContainer;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.util.AlternatorParticleOffsets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FluxCrafterTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final CItemStacksHandler inventory;
	private final CachedRecipe<CraftingInput, IFluxCrafterRecipe> recipe;
	private int progress;
	private int progressReq;
	protected boolean isGridChanged = true;

	private final ContainerData dataAccess;

	public FluxCrafterTileEntity(BlockPos pos, BlockState state) {
		this(ModTileEntities.FLUX_CRAFTER.get(), pos, state);
	}

	public FluxCrafterTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.inventory = createInventoryHandler(this::onContentsChanged);
		this.recipe = new CachedRecipe<>(ModRecipeTypes.FLUX_CRAFTER.get());

		this.dataAccess = ContainerDataBuilder.builder()
				.sync(() -> this.progress, value -> this.progress = value)
				.sync(() -> this.progressReq, value -> this.progressReq = value)
				.build();
	}

	@Override
	public CItemStacksHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.progress = input.getIntOr("Progress", 0);
		this.progressReq = input.getIntOr("ProgressReq", 0);
	}

	@Override
	public void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("Progress", this.progress);
		output.putInt("ProgressReq", this.progressReq);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.extendedcrafting.flux_crafter");
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return new FluxCrafterContainer(windowId, playerInventory, this.inventory, this.dataAccess, this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, FluxCrafterTileEntity tile) {
		var recipe = tile.getActiveRecipe();
		var selectedRecipe = tile.getSelectedRecipeGrid();

		if (recipe != null && (selectedRecipe == null || recipe.matches(selectedRecipe, level))) {
			var result = recipe.assemble(tile.inventory.toCraftingInput(3, 3, 0, 9));
			var output = tile.inventory.getResource(9);
			var canFit = result.getCount() + tile.inventory.getAmountAsInt(9) <= output.getMaxStackSize();

			if (canFit && output.matches(result)) {
				var alternators = tile.getAlternators();
				int alternatorCount = alternators.size();

				if (alternatorCount > 0) {
					tile.progress(alternatorCount);

					try (var tx = Transaction.openRoot()) {
						for (var alternator : alternators) {
							var direction = alternator.getBlockState().getValue(FluxAlternatorBlock.FACING);
							var alternatorPos = alternator.getBlockPos();

							if (level.isEmptyBlock(alternatorPos.relative(direction))) {
								tile.sendAlternatorParticles(alternatorPos, direction);
							}

							alternator.getEnergy().extract(recipe.getPowerRate(), tx);
						}

						if (tile.progress >= tile.progressReq) {
							for (int i = 0; i < tile.inventory.size() - 1; i++) {
								tile.inventory.extract(i, tile.inventory.getResource(i), 1, tx, false);
							}

							tile.progress = 0;
						}

						tx.commit();
					}

					tile.setChangedFast();
				}
			} else {
				if (tile.progress > 0 || tile.progressReq > 0) {
					tile.reset();
					tile.setChangedFast();
				}
			}
		} else {
			if (tile.progress > 0 || tile.progressReq > 0) {
				tile.reset();
				tile.setChangedFast();
			}
		}

		tile.dispatchIfChanged();
	}

	public static CItemStacksHandler createInventoryHandler() {
		return createInventoryHandler(null);
	}

	public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
		return CItemStacksHandler.create(10, onContentsChanged, builder -> {
			builder.setOutputSlots(9);
			builder.setCanInsert((_, _) -> false);
		});
	}

	// to be overridden by the auto variant
	public TableRecipeStorage getRecipeStorage() {
		return null;
	}

	private CraftingInput getSelectedRecipeGrid() {
		var storage = this.getRecipeStorage();
		if (storage != null) {
			var grid = storage.getSelectedRecipeGrid();
			if (grid != null) {
				return grid.toCraftingInput(3, 3);
			}
		}

		return null;
	}

	private void updateResult(ItemStack stack) {
		var result = this.inventory.getResource(9);

		if (result.isEmpty()) {
			this.inventory.set(9, ItemResource.of(stack), stack.getCount());
		} else {
			var amount = this.inventory.getAmountAsInt(0);
			this.inventory.set(9, result, amount + stack.getCount());
		}
	}

	private List<FluxAlternatorTileEntity> getAlternators() {
		List<FluxAlternatorTileEntity> alternators = new ArrayList<>();
		var level = this.getLevel();

		if (level != null) {
			var pos = this.getBlockPos();

			BlockPos.betweenClosedStream(pos.offset(-3, -3, -3), pos.offset(3, 3, 3)).forEach(aoePos -> {
				var tile = level.getBlockEntity(aoePos);
				if (tile instanceof FluxAlternatorTileEntity alternator && alternator.getEnergy().getAmountAsInt() >= this.recipe.get().getPowerRate())
					alternators.add(alternator);
			});
		}

		return alternators;
	}

	private void progress(int alternators) {
		this.progress += this.recipe.get().getPowerRate() * alternators;
		this.progressReq = this.recipe.get().getPowerRequired();
	}

	private void reset() {
		this.progress = 0;
		this.progressReq = 0;
	}

	private void sendAlternatorParticles(BlockPos pos, Direction direction) {
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		var level = (ServerLevel) this.getLevel();
		var offsets = AlternatorParticleOffsets.fromDirection(direction);

		double x = pos.getX() + offsets.x;
		double y = pos.getY() + offsets.y;
		double z = pos.getZ() + offsets.z;

		level.sendParticles(ParticleTypes.PORTAL, x, y, z, 1, 0, 0, 0, 0.1D);
	}

	private void onContentsChanged(int slot, ItemStack oldStack) {
		if (!this.isGridChanged) {
			this.isGridChanged = true;
			this.setChanged();
		}
	}

	public int getProgress() {
		return this.progress;
	}

	public int getProgressRequired() {
		return this.progressReq;
	}

	public IFluxCrafterRecipe getActiveRecipe() {
		if (this.isGridChanged) {
			this.isGridChanged = false;
			return this.recipe.checkAndGet(this.inventory.toCraftingInput(3, 3, 0, 9), (ServerLevel) this.level);
		}

		return this.recipe.get();
	}
}
