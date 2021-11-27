package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.BaseEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CraftingCoreTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
	private final BaseItemStackHandler inventory;
	private final BaseEnergyStorage energy;
	private final BaseItemStackHandler recipeInventory;
	private CombinationRecipe recipe;
	private int progress;
	private int oldEnergy;
	private int pedestalCount;
	private boolean haveItemsChanged = true;

	public CraftingCoreTileEntity() {
		super(ModTileEntities.CRAFTING_CORE.get());
		this.inventory = new BaseItemStackHandler(1, this::markDirtyAndDispatch);
		this.energy = new BaseEnergyStorage(ModConfigs.CRAFTING_CORE_POWER_CAPACITY.get());
		this.recipeInventory = new BaseItemStackHandler(49);

		this.inventory.setDefaultSlotLimit(1);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		super.load(state, tag);
		this.progress = tag.getInt("Progress");
		this.energy.setEnergy(tag.getInt("Energy"));
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		tag = super.save(tag);
		tag.putInt("Progress", this.progress);
		tag.putInt("Energy", this.energy.getEnergyStored());

		return tag;
	}

	@Override
	public void tick() {
		boolean mark = false;

		Map<BlockPos, ItemStack> pedestalsWithItems = this.getPedestalsWithItems();
		World world = this.getLevel();

		if (world != null) {
			ItemStack[] stacks = pedestalsWithItems.values().toArray(new ItemStack[0]);
			this.updateRecipeInventory(stacks);

			if (this.haveItemsChanged && (this.recipe == null || !this.recipe.matches(this.recipeInventory))) {
				this.recipe = (CombinationRecipe) world.getRecipeManager().getRecipeFor(RecipeTypes.COMBINATION, this.recipeInventory.toIInventory(), world).orElse(null);
			}

			if (!world.isClientSide()) {
				if (this.recipe != null) {
					if (this.energy.getEnergyStored() > 0) {
						boolean done = this.process(this.recipe);

						if (done) {
							for (BlockPos pedestalPos : pedestalsWithItems.keySet()) {
								TileEntity tile = world.getBlockEntity(pedestalPos);

								if (tile instanceof PedestalTileEntity) {
									PedestalTileEntity pedestal = (PedestalTileEntity) tile;
									IItemHandlerModifiable inventory = pedestal.getInventory();
									inventory.setStackInSlot(0, StackHelper.shrink(inventory.getStackInSlot(0), 1, true));
									pedestal.markDirtyAndDispatch();
									this.spawnParticles(ParticleTypes.SMOKE, pedestalPos, 1.1, 20);
								}
							}

							this.spawnParticles(ParticleTypes.END_ROD, this.getBlockPos(), 1.1, 50);
							this.inventory.setStackInSlot(0, this.recipe.getCraftingResult(this.recipeInventory));
							this.progress = 0;

							mark = true;
						} else {
							this.spawnParticles(ParticleTypes.ENTITY_EFFECT, this.getBlockPos(), 1.15, 2);

							if (this.shouldSpawnItemParticles()) {
								for (BlockPos pedestalPos : pedestalsWithItems.keySet()) {
									TileEntity tile = world.getBlockEntity(pedestalPos);

									if (tile instanceof PedestalTileEntity) {
										PedestalTileEntity pedestal = (PedestalTileEntity) tile;
										IItemHandlerModifiable inventory = pedestal.getInventory();
										ItemStack stack = inventory.getStackInSlot(0);
										this.spawnItemParticles(pedestalPos, stack);
									}
								}
							}
						}
					}
				} else {
					this.progress = 0;
				}
			}
		}

		if (this.oldEnergy != this.energy.getEnergyStored()) {
			this.oldEnergy = this.energy.getEnergyStored();
			if (!mark)
				mark = true;
		}

		if (mark) {
			this.markDirtyAndDispatch();
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.isRemoved() && cap == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(this::getEnergy));
		}

		return super.getCapability(cap, side);
	}

	@Override
	public ITextComponent getDisplayName() {
		return Localizable.of("container.extendedcrafting.crafting_core").build();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		return CraftingCoreContainer.create(windowId, playerInventory, this::isUsableByPlayer, new IntArray(0), this.getBlockPos());
	}

	public BaseEnergyStorage getEnergy() {
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
		World world = this.getLevel();

		int pedestalCount = 0;
		if (world != null) {
			BlockPos pos = this.getBlockPos();
			Iterator<BlockPos> positions = BlockPos.betweenClosedStream(pos.offset(-3, 0, -3), pos.offset(3, 0, 3)).iterator();

			while (positions.hasNext()) {
				BlockPos aoePos = positions.next();
				TileEntity tile = world.getBlockEntity(aoePos);

				if (tile instanceof PedestalTileEntity) {
					PedestalTileEntity pedestal = (PedestalTileEntity) tile;
					ItemStack stack = pedestal.getInventory().getStackInSlot(0);

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

	private <T extends IParticleData> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		ServerWorld world = (ServerWorld) this.getLevel();

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D;

		world.sendParticles(particle, x, y, z, count, 0, 0, 0, 0.1D);
	}

	private void spawnItemParticles(BlockPos pedestalPos, ItemStack stack) {
		if (this.getLevel() == null || this.getLevel().isClientSide())
			return;

		ServerWorld world = (ServerWorld) this.getLevel();
		BlockPos pos = this.getBlockPos();

		double x = pedestalPos.getX() + (world.getRandom().nextDouble() * 0.2D) + 0.4D;
		double y = pedestalPos.getY() + (world.getRandom().nextDouble() * 0.2D) + 1.4D;
		double z = pedestalPos.getZ() + (world.getRandom().nextDouble() * 0.2D) + 0.4D;

		double velX = pos.getX() - pedestalPos.getX();
		double velY = 0.25D;
		double velZ = pos.getZ() - pedestalPos.getZ();

		world.sendParticles(new ItemParticleData(ParticleTypes.ITEM, stack), x, y, z, 0, velX, velY, velZ, 0.18D);
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
