package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CEnergyStorage;
import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.ContainerDataBuilder;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.api.crafting.ICombinationRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class CraftingCoreTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final CItemStacksHandler inventory;
	private final CItemStacksHandler recipeInventory;
	private final CEnergyStorage energy;
	private final CachedRecipe<CraftingInput, ICombinationRecipe> recipe;
	private int progress;
	private int pedestalCount;
	private boolean haveItemsChanged = true;

	private final ContainerData dataAccess;

	public CraftingCoreTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.CRAFTING_CORE.get(), pos, state);
		this.inventory = createInventoryHandler((_, _) -> this.setChanged());
		this.energy = new CEnergyStorage(ModConfigs.CRAFTING_CORE_POWER_CAPACITY.get(), _ -> this.setChangedFast());
		this.recipeInventory = CItemStacksHandler.create(49);
		this.recipe = new CachedRecipe<>(ModRecipeTypes.COMBINATION.get());

		this.dataAccess = ContainerDataBuilder.builder()
				.sync(this.energy::getAmountAsInt, this.energy::set)
				.sync(this.energy::getCapacityAsInt, this.energy::setMaxCapacity)
				.sync(() -> this.progress, value -> this.progress = value)
				.sync(this::getEnergyRequired)
				.sync(this::getEnergyRate)
				.sync(() -> this.pedestalCount)
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
		this.energy.deserialize(input);
	}

	@Override
	public void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("Progress", this.progress);
		this.energy.serialize(output);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.extendedcrafting.crafting_core");
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return new CraftingCoreContainer(windowId, playerInventory, this.dataAccess, this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CraftingCoreTileEntity tile) {
		var recipe = tile.getActiveRecipe();

		if (recipe != null) {
			if (tile.energy.getAmountAsInt() > 0) {
				try (var tx = Transaction.openRoot()) {
					boolean done = tile.process(recipe, tx);
					var pedestalsWithItems = tile.getPedestalsWithItems();

					if (done) {
						var input = tile.toCraftingInput();
						var remaining = recipe.getRemainingItems(input);
						int index = 1; // 0 is the center stack

						for (var pedestalPos : pedestalsWithItems.keySet()) {
							var pedestalTile = level.getBlockEntity(pedestalPos);

							if (pedestalTile instanceof PedestalTileEntity pedestal) {
								var inventory = pedestal.getInventory();
								var remainder = remaining.get(index);

								inventory.set(0, ItemResource.of(remainder), remainder.count());

								tile.spawnParticles(ParticleTypes.SMOKE, pedestalPos, 1.1, 20);
							}

							index++;
						}

						var result = recipe.assemble(input);

						tile.spawnParticles(ParticleTypes.END_ROD, pos, 1.1, 50);
						tile.inventory.set(0, ItemResource.of(result), result.count());
						tile.progress = 0;
						tile.setChangedFast();
					} else {
						tile.spawnParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(Utils.randInt(0, 255), Utils.randInt(0, 255), Utils.randInt(0, 255))), pos, 1.15, 2);

						if (tile.shouldSpawnItemParticles()) {
							for (var pedestalPos : pedestalsWithItems.keySet()) {
								var pedestalTile = level.getBlockEntity(pedestalPos);

								if (pedestalTile instanceof PedestalTileEntity pedestal) {
									var inventory = pedestal.getInventory();
									var resource = inventory.getResource(0);

									tile.spawnItemParticles(pedestalPos, resource);
								}
							}
						}
					}

					tx.commit();
				}
			}
		} else {
			if (tile.progress > 0) {
				tile.progress = 0;
				tile.setChangedFast();
			}
		}

		tile.dispatchIfChanged();
	}

	public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
		return CItemStacksHandler.create(1, onContentsChanged, builder -> {
			builder.setDefaultSlotLimit(1);
		});
	}

	public CEnergyStorage getEnergy() {
		return this.energy;
	}

	public ICombinationRecipe getActiveRecipe() {
		if (this.level == null)
			return null;

		var pedestalsWithItems = this.getPedestalsWithItems();
		var stacks = pedestalsWithItems.values().toArray(new ItemStack[0]);

		this.updateRecipeInventory(stacks);

		if (!this.haveItemsChanged) {
			return this.recipe.get();
		}

		return this.recipe.checkAndGet(this.toCraftingInput(), (ServerLevel) this.level);
	}

	public boolean hasRecipe() {
		return this.recipe.exists();
	}

	public int getEnergyRequired() {
		return this.hasRecipe() ? this.recipe.get().getPowerCost() : 0;
	}

	public int getEnergyRate() {
		return this.hasRecipe() ? this.recipe.get().getPowerRate() : 0;
	}

	public int getProgress() {
		return this.progress;
	}

	public int getPedestalCount() {
		return this.pedestalCount;
	}

	private void updateRecipeInventory(ItemStack[] items) {
		boolean haveItemsChanged = this.recipeInventory.size() != items.length + 1
				|| !this.recipeInventory.getResource(0).equals(this.inventory.getResource(0));

		if (!haveItemsChanged) {
			for (int i = 0; i < items.length; i++) {
				if (!this.recipeInventory.getResource(i + 1).matches(items[i])) {
					haveItemsChanged = true;
					break;
				}
			}
		}

		this.haveItemsChanged = haveItemsChanged;

		if (!haveItemsChanged)
			return;

		this.recipeInventory.set(0, this.inventory.getResource(0), this.inventory.getAmountAsInt(0));

		for (int i = 0; i < this.recipeInventory.size() - 1; i++) {
			if (i < items.length) {
				this.recipeInventory.set(i + 1, ItemResource.of(items[i]), items[i].count());
			} else {
				this.recipeInventory.set(i + 1, ItemResource.EMPTY, 0);
			}
		}
	}

	private boolean process(ICombinationRecipe recipe, TransactionContext tx) {
		int extract = recipe.getPowerRate();
		int difference = recipe.getPowerCost() - this.progress;
		if (difference < recipe.getPowerRate())
			extract = difference;

		int extracted = this.energy.extract(extract, tx);
		this.progress += extracted;

		return this.progress >= recipe.getPowerCost();
	}

	public Map<BlockPos, ItemStack> getPedestalsWithItems() {
		Map<BlockPos, ItemStack> pedestals = new LinkedHashMap<>();
		var world = this.getLevel();

		int pedestalCount = 0;
		if (world != null) {
			var pos = this.getBlockPos();
			var positions = BlockPos.betweenClosedStream(pos.offset(-3, 0, -3), pos.offset(3, 0, 3)).iterator();

			while (positions.hasNext()) {
				var aoePos = positions.next();
				var tile = world.getBlockEntity(aoePos);

				if (tile instanceof PedestalTileEntity pedestal) {
					var resource = pedestal.getInventory().getResource(0);

					pedestalCount++;

					if (!resource.isEmpty()) {
						pedestals.put(aoePos.immutable(), resource.toStack());
					}
				}
			}
		}

		this.pedestalCount = pedestalCount;

		return pedestals;
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

	private void spawnItemParticles(BlockPos pedestalPos, ItemResource resource) {
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		var level = (ServerLevel) this.getLevel();
		var pos = this.getBlockPos();

		double x = pedestalPos.getX() + (level.getRandom().nextDouble() * 0.2D) + 0.4D;
		double y = pedestalPos.getY() + (level.getRandom().nextDouble() * 0.2D) + 1.4D;
		double z = pedestalPos.getZ() + (level.getRandom().nextDouble() * 0.2D) + 0.4D;

		double velX = pos.getX() - pedestalPos.getX();
		double velY = 0.25D;
		double velZ = pos.getZ() - pedestalPos.getZ();

		level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, resource.getItem()), x, y, z, 0, velX, velY, velZ, 0.18D);
	}

	private boolean shouldSpawnItemParticles() {
		int powerCost = this.recipe.get().getPowerCost();
		int powerRate = this.recipe.get().getPowerRate();
		int endingPower = powerRate * 40;

		return this.progress > (powerCost - endingPower);
	}

	private CraftingInput toCraftingInput() {
		return this.recipeInventory.toShapelessCraftingInput();
	}
}
