package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import java.util.HashMap;
import java.util.Map;

public class CraftingCoreTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final BaseItemStackHandler inventory;
	private final EnergyStorage energy;
	private final BaseItemStackHandler recipeInventory;
	private CombinationRecipe recipe;
	private int progress;
	private int oldEnergy;
	private int pedestalCount;
	private boolean haveItemsChanged = true;

	public CraftingCoreTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.CRAFTING_CORE.get(), pos, state);
		this.inventory = createInventoryHandler(this::markDirtyAndDispatch);
		this.energy = new EnergyStorage(ModConfigs.CRAFTING_CORE_POWER_CAPACITY.get());
		this.recipeInventory = new BaseItemStackHandler(49);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.progress = tag.getInt("Progress");
		this.energy.deserializeNBT(tag.get("Energy"));
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag = super.save(tag);
		tag.putInt("Progress", this.progress);
		tag.putInt("Energy", this.energy.getEnergyStored());

		return tag;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.isRemoved() && cap == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(this::getEnergy));
		}

		return super.getCapability(cap, side);
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.crafting_core").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		return CraftingCoreContainer.create(windowId, playerInventory, this::isUsableByPlayer, new SimpleContainerData(0), this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CraftingCoreTileEntity tile) {
		var mark = false;

		var pedestalsWithItems = tile.getPedestalsWithItems();
		var stacks = pedestalsWithItems.values().toArray(new ItemStack[0]);

		tile.updateRecipeInventory(stacks);

		if (tile.haveItemsChanged && (tile.recipe == null || !tile.recipe.matches(tile.recipeInventory))) {
			tile.recipe = (CombinationRecipe) level.getRecipeManager().getRecipeFor(RecipeTypes.COMBINATION, tile.recipeInventory.toIInventory(), level).orElse(null);
		}

		if (!level.isClientSide()) {
			if (tile.recipe != null) {
				if (tile.energy.getEnergyStored() > 0) {
					boolean done = tile.process(tile.recipe);

					if (done) {
						for (var pedestalPos : pedestalsWithItems.keySet()) {
							var pedestalTile = level.getBlockEntity(pedestalPos);

							if (pedestalTile instanceof PedestalTileEntity pedestal) {
								var inventory = pedestal.getInventory();

								inventory.setStackInSlot(0, StackHelper.shrink(inventory.getStackInSlot(0), 1, true));
								pedestal.markDirtyAndDispatch();

								tile.spawnParticles(ParticleTypes.SMOKE, pedestalPos, 1.1, 20);
							}
						}

						tile.spawnParticles(ParticleTypes.END_ROD, pos, 1.1, 50);
						tile.inventory.setStackInSlot(0, tile.recipe.assemble(tile.recipeInventory));
						tile.progress = 0;

						mark = true;
					} else {
						tile.spawnParticles(ParticleTypes.ENTITY_EFFECT, pos, 1.15, 2);

						if (tile.shouldSpawnItemParticles()) {
							for (var pedestalPos : pedestalsWithItems.keySet()) {
								var pedestalTile = level.getBlockEntity(pedestalPos);

								if (pedestalTile instanceof PedestalTileEntity pedestal) {
									var inventory = pedestal.getInventory();
									var stack = inventory.getStackInSlot(0);

									tile.spawnItemParticles(pedestalPos, stack);
								}
							}
						}
					}
				}
			} else {
				tile.progress = 0;
			}
		}

		if (tile.oldEnergy != tile.energy.getEnergyStored()) {
			tile.oldEnergy = tile.energy.getEnergyStored();
			if (!mark)
				mark = true;
		}

		if (mark) {
			tile.markDirtyAndDispatch();
		}
	}

	public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
		var inventory = new BaseItemStackHandler(1, onContentsChanged);

		inventory.setDefaultSlotLimit(1);

		return inventory;
	}

	public EnergyStorage getEnergy() {
		return this.energy;
	}

	public CombinationRecipe getActiveRecipe() {
		return this.recipe;
	}

	public boolean hasRecipe() {
		return this.recipe != null;
	}

	public int getEnergyRequired() {
		return this.hasRecipe() ? this.recipe.getPowerCost() : 0;
	}

	public int getEnergyRate() {
		return this.hasRecipe() ? this.recipe.getPowerRate() : 0;
	}

	public int getProgress() {
		return this.progress;
	}

	public int getPedestalCount() {
		return this.pedestalCount;
	}

	private void updateRecipeInventory(ItemStack[] items) {
		boolean haveItemsChanged = this.recipeInventory.getSlots() != items.length + 1
				|| !areStacksEqual(this.recipeInventory.getStackInSlot(0), this.inventory.getStackInSlot(0));

		if (!haveItemsChanged) {
			for (int i = 0; i < items.length; i++) {
				if (!areStacksEqual(this.recipeInventory.getStackInSlot(i + 1), items[i])) {
					haveItemsChanged = true;
					break;
				}
			}
		}

		this.haveItemsChanged = haveItemsChanged;

		if (!haveItemsChanged)
			return;

		this.recipeInventory.setSize(items.length + 1);
		this.recipeInventory.setStackInSlot(0, this.inventory.getStackInSlot(0));

		for (int i = 0; i < items.length; i++) {
			this.recipeInventory.setStackInSlot(i + 1, items[i]);
		}
	}

	private boolean process(CombinationRecipe recipe) {
		int extract = recipe.getPowerRate();
		int difference = recipe.getPowerCost() - this.progress;
		if (difference < recipe.getPowerRate())
			extract = difference;

		int extracted = this.energy.extractEnergy(extract, false);
		this.progress += extracted;

		return this.progress >= recipe.getPowerCost();
	}

	public Map<BlockPos, ItemStack> getPedestalsWithItems() {
		Map<BlockPos, ItemStack> pedestals = new HashMap<>();
		var world = this.getLevel();

		int pedestalCount = 0;
		if (world != null) {
			var pos = this.getBlockPos();
			var positions = BlockPos.betweenClosedStream(pos.offset(-3, 0, -3), pos.offset(3, 0, 3)).iterator();

			while (positions.hasNext()) {
				var aoePos = positions.next();
				var tile = world.getBlockEntity(aoePos);

				if (tile instanceof PedestalTileEntity pedestal) {
					var stack = pedestal.getInventory().getStackInSlot(0);

					pedestalCount++;

					if (!stack.isEmpty()) {
						pedestals.put(aoePos.immutable(), stack);
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

	private void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
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

		level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
	}

	private boolean shouldSpawnItemParticles() {
		int powerCost = this.recipe.getPowerCost();
		int powerRate = this.recipe.getPowerRate();
		int endingPower = powerRate * 40;

		return this.progress > (powerCost - endingPower);
	}

	// TODO: 1.17: fix in cucumber StackHelper.areStacksEqual
	private static boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
		if (stack1.isEmpty() && stack2.isEmpty())
			return true;

		return !stack1.isEmpty() && stack1.sameItem(stack2) && ItemStack.tagMatches(stack1, stack2);
	}
}
