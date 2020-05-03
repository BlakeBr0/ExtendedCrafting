package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CustomEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.api.crafting.RecipeTypes;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.HashMap;
import java.util.Map;

public class CraftingCoreTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
	private final BaseItemStackHandler inventory = new BaseItemStackHandler(1, this::markDirtyAndDispatch);
	private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfigs.CRAFTING_CORE_POWER_CAPACITY.get());
	private final BaseItemStackHandler recipeInventory = new BaseItemStackHandler(49);
	private CombinationRecipe recipe;
	private int progress;
	private int oldEnergy;
	private int pedestalCount;
	protected IIntArray data = new IIntArray() {
		@Override
		public int get(int i) {
			switch (i) {
				case 0:
					return CraftingCoreTileEntity.this.getProgress();
				case 1:
					return CraftingCoreTileEntity.this.getPedestalCount();
				case 2:
					return CraftingCoreTileEntity.this.getEnergy().getEnergyStored();
				case 3:
					return CraftingCoreTileEntity.this.getEnergy().getMaxEnergyStored();
				case 4:
					return CraftingCoreTileEntity.this.getEnergyRequired();
				case 5:
					return CraftingCoreTileEntity.this.getEnergyRate();
				case 6:
					return CraftingCoreTileEntity.this.hasRecipe() ? 1 : 0;
				default:
					return 0;
			}
		}

		@Override
		public void set(int i, int value) {

		}

		@Override
		public int size() {
			return 7;
		}
	};

	public CraftingCoreTileEntity() {
		super(ModTileEntities.CRAFTING_CORE.get());
		this.inventory.setDefaultSlotLimit(1);
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		this.progress = tag.getInt("Progress");
		this.energy.setEnergy(tag.getInt("Energy"));
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		tag = super.write(tag);
		tag.putInt("Progress", this.progress);
		tag.putInt("Energy", this.energy.getEnergyStored());

		return tag;
	}

	@Override
	public void tick() {
		boolean mark = false;

		Map<BlockPos, ItemStack> pedestalsWithItems = this.getPedestalsWithItems();
		World world = this.getWorld();
		if (world != null) {
			ItemStack[] stacks = pedestalsWithItems.values().toArray(new ItemStack[0]);
			this.updateRecipeInventory(stacks);
			if (this.recipe == null || !this.recipe.matches(this.recipeInventory)) {
				this.recipe = (CombinationRecipe) world.getRecipeManager().getRecipe(RecipeTypes.COMBINATION, this.recipeInventory.toIInventory(), world).orElse(null);
			}

			if (!world.isRemote()) {
				if (this.recipe != null) {
					if (this.energy.getEnergyStored() > 0) {
						boolean done = this.process(this.recipe);
						if (done) {
							for (BlockPos pedestalPos : pedestalsWithItems.keySet()) {
								TileEntity tile = world.getTileEntity(pedestalPos);
								if (tile instanceof PedestalTileEntity) {
									PedestalTileEntity pedestal = (PedestalTileEntity) tile;
									IItemHandlerModifiable inventory = pedestal.getInventory();
									inventory.setStackInSlot(0, StackHelper.decrease(inventory.getStackInSlot(0), 1, true));
									pedestal.markDirtyAndDispatch();
									this.spawnParticles(ParticleTypes.SMOKE, pedestalPos, 1.1, 20);
								}
							}
							this.spawnParticles(ParticleTypes.END_ROD, this.getPos(), 1.1, 50);
							this.inventory.setStackInSlot(0, this.recipe.getCraftingResult(this.recipeInventory));
							this.progress = 0;
							mark = true;
						} else {
							this.spawnParticles(ParticleTypes.ENTITY_EFFECT, this.getPos(), 1.15, 2);
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

		if (mark)
			this.markDirty();
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
		return CraftingCoreContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.data, this.getPos());
	}

	private Map<BlockPos, ItemStack> getPedestalsWithItems() {
		Map<BlockPos, ItemStack> pedestals = new HashMap<>();
		World world = this.getWorld();
		if (world != null) {
			BlockPos pos = this.getPos();
			BlockPos.getAllInBox(pos.add(-3, 0, -3), pos.add(3, 0, 3)).forEach(aoePos -> {
				TileEntity tile = world.getTileEntity(aoePos);
				if (tile instanceof PedestalTileEntity) {
					PedestalTileEntity pedestal = (PedestalTileEntity) tile;
					ItemStack stack = pedestal.getInventory().getStackInSlot(0);
					pedestals.put(aoePos.toImmutable(), stack);
				}
			});
		}

		this.pedestalCount = pedestals.size();

		return pedestals;
	}

	private void updateRecipeInventory(ItemStack[] items) {
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

	private <T extends IParticleData> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
		if (this.getWorld() == null || this.getWorld().isRemote()) return;
		ServerWorld world = (ServerWorld) this.getWorld();

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D;

		world.spawnParticle(particle, x, y, z, count, 0, 0, 0, 0.1D);
	}

	public CustomEnergyStorage getEnergy() {
		return this.energy;
	}

	public CombinationRecipe getActiveRecipe() {
		return this.recipe;
	}

	public boolean hasRecipe() {
		return this.recipe != null;
	}

	public int getEnergyRequired() {
		if (this.hasRecipe())
			return this.recipe.getPowerCost();

		return 0;
	}

	public int getEnergyRate() {
		if (this.hasRecipe())
			return this.recipe.getPowerRate();

		return 0;
	}

	public int getProgress() {
		return this.progress;
	}

	public int getPedestalCount() {
		return this.pedestalCount;
	}
}
