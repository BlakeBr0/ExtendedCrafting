package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.ItemResourceHelper;
import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.ContainerDataBuilder;
import com.blakebr0.extendedcrafting.api.crafting.IEnderCrafterRecipe;
import com.blakebr0.extendedcrafting.block.EnderAlternatorBlock;
import com.blakebr0.extendedcrafting.client.handler.ClientRecipeHandler;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.EnderCrafterContainer;
import com.blakebr0.extendedcrafting.crafting.TableRecipeStorage;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.blakebr0.extendedcrafting.util.AlternatorParticleOffsets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnderCrafterTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final CItemStacksHandler inventory;
	private final CachedRecipe<CraftingInput, IEnderCrafterRecipe> recipe;
	private int progress;
	private int progressReq;
	private @Nullable Identifier recipeId;
	protected boolean isGridChanged = true;

	private final ContainerData dataAccess;

	public EnderCrafterTileEntity(BlockPos pos, BlockState state) {
		this(ModTileEntities.ENDER_CRAFTER.get(), pos, state);
	}

	public EnderCrafterTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.inventory = createInventoryHandler((_, _) -> this.onContentsChanged());
		this.recipe = new CachedRecipe<>(ModRecipeTypes.ENDER_CRAFTER.get());

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
		this.progress = input.getIntOr("progress", 0);
		this.progressReq = input.getIntOr("progress_required", 0);
		this.recipeId = input.read("recipe_id", Identifier.CODEC).orElse(null);
	}

	@Override
	public void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("progress", this.progress);
		output.putInt("progress_required", this.progressReq);
		output.storeNullable("recipe_id", Identifier.CODEC, this.recipeId);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.extendedcrafting.ender_crafter");
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return new EnderCrafterContainer(windowId, playerInventory, this.inventory, this.dataAccess, this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, EnderCrafterTileEntity tile) {
		var recipe = tile.getActiveRecipe();
		var selectedRecipe = tile.getSelectedRecipeGrid();

		if (recipe != null && (selectedRecipe == null || recipe.matches(selectedRecipe, level))) {
			var result = recipe.assemble(tile.toCraftingInput());

			if (ItemResourceHelper.canCombine(tile.inventory, 9, result)) {
				var alternators = tile.getAlternatorPositions();
				int alternatorCount = alternators.size();

				if (alternatorCount > 0) {
					tile.progress(alternatorCount, recipe.getCraftingTime());

					for (var alternatorPos : alternators) {
						var alternator = level.getBlockState(alternatorPos);
						var direction = alternator.getValue(EnderAlternatorBlock.FACING);

						if (level.isEmptyBlock(alternatorPos.relative(direction))) {
							tile.sendAlternatorParticles(alternatorPos, direction);
						}
					}

					if (tile.progress >= tile.progressReq) {
						try (var tx = Transaction.openRoot()) {
							for (int i = 0; i < tile.inventory.size() - 1; i++) {
								tile.inventory.extract(i, tile.inventory.getResource(i), 1, tx, true);
							}

							tile.inventory.insert(9, ItemResource.of(result), result.count(), tx, true);
							tile.progress = 0;

							tx.commit();
						}
					}

					tile.setChangedFast();
				}
			} else {
				if (tile.progress > 0 || tile.progressReq > 0) {
					tile.progress = 0;
					tile.progressReq = 0;

					tile.setChangedFast();
				}
			}
		} else {
			if (tile.progress > 0 || tile.progressReq > 0) {
				tile.progress = 0;
				tile.progressReq = 0;

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

	private List<BlockPos> getAlternatorPositions() {
		List<BlockPos> alternators = new ArrayList<>();
		var level = this.getLevel();

		if (level != null) {
			var pos = this.getBlockPos();

			BlockPos.betweenClosedStream(pos.offset(-3, -3, -3), pos.offset(3, 3, 3)).forEach(aoePos -> {
				var block = level.getBlockState(aoePos).getBlock();
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

	private void onContentsChanged() {
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

	// to be overridden by the auto version
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

	public @Nullable IEnderCrafterRecipe getActiveRecipe() {
		if (this.isGridChanged) {
			this.isGridChanged = false;

			this.recipe.check(this.toCraftingInput(), (ServerLevel) this.level);

			if (this.recipeId != this.recipe.id()) {
				this.recipeId = this.recipe.id();
				this.setChangedFast();
			}
		}

		return this.recipe.get();
	}

	public @Nullable IEnderCrafterRecipe getActiveClientRecipe() {
		return ClientRecipeHandler.ENDER_CRAFTER_RECIPE_MAP.get(this.recipeId);
	}

	private CraftingInput toCraftingInput() {
		return this.inventory.toCraftingInput(3, 3, 0, 9);
	}
}
