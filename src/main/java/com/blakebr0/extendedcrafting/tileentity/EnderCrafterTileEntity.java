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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class EnderCrafterTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
	private final BaseItemStackHandler inventory = new BaseItemStackHandler(10, this::markDirtyAndDispatch);
	private final BaseItemStackHandler recipeInventory = new BaseItemStackHandler(9);
	private IEnderCrafterRecipe recipe;
	private int progress;
	private int progressReq;
	protected IIntArray data = new IIntArray() {
		@Override
		public int get(int i) {
			switch (i) {
				case 0: return EnderCrafterTileEntity.this.getProgress();
				case 1: return EnderCrafterTileEntity.this.getProgressRequired();
				default: return 0;
			}
		}

		@Override
		public void set(int i, int value) { }

		@Override
		public int size() {
			return 2;
		}
	};

	public EnderCrafterTileEntity() {
		super(ModTileEntities.ENDER_CRAFTER.get());
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void read(BlockState state, CompoundNBT tag) {
		super.read(state, tag);
		this.progress = tag.getInt("Progress");
		this.progressReq = tag.getInt("ProgressReq");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		tag = super.write(tag);
		tag.putInt("Progress", this.progress);
		tag.putInt("ProgressReq", this.progressReq);

		return tag;
	}

	@Override
	public void tick() {
		World world = this.getWorld();
		if (world != null) {
			this.updateRecipeInventory();
			IInventory recipeInventory = this.recipeInventory.toIInventory();
			if (this.recipe == null || !this.recipe.matches(recipeInventory, world)) {
				this.recipe = world.getRecipeManager().getRecipe(RecipeTypes.ENDER_CRAFTER, recipeInventory, world).orElse(null);
			}

			if (!world.isRemote()) {
				if (this.recipe != null) {
					ItemStack result = this.recipe.getCraftingResult(recipeInventory);
					ItemStack output = this.inventory.getStackInSlot(9);
					if (StackHelper.canCombineStacks(result, output)) {
						List<BlockPos> alternators = this.getAlternatorPositions();
						int alternatorCount = alternators.size();

						if (alternatorCount > 0) {
							this.progress(alternatorCount, this.recipe.getCraftingTime());

							for (BlockPos pos : alternators) {
								if (world.isAirBlock(pos.up())) {
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

							this.markDirty();
						}
					} else {
						if (this.progress > 0 || this.progressReq > 0) {
							this.progress = 0;
							this.progressReq = 0;
							this.markDirty();
						}
					}
				} else {
					if (this.progress > 0 || this.progressReq > 0) {
						this.progress = 0;
						this.progressReq = 0;
						this.markDirty();
					}
				}
			}
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		return Localizable.of("container.extendedcrafting.ender_crafter").build();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		return EnderCrafterContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory, this.data);
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
		World world = this.getWorld();
		if (world != null) {
			BlockPos pos = this.getPos();
			BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(3, 3, 3)).forEach(aoePos -> {
				Block block = world.getBlockState(aoePos).getBlock();
				if (block instanceof EnderAlternatorBlock)
					alternators.add(aoePos.toImmutable());
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

	private <T extends IParticleData> void spawnParticles(T particle, BlockPos pos, double yOffset, int count) {
		if (this.getWorld() == null || this.getWorld().isRemote()) return;
		ServerWorld world = (ServerWorld) this.getWorld();

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + yOffset;
		double z = pos.getZ() + 0.5D;

		world.spawnParticle(particle, x, y, z, count, 0, 0, 0, 0.1D);
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
