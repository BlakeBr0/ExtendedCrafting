package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.block.EnderAlternatorBlock;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.IEnderCraftingRecipe;
import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.lib.EmptyContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;

public class EnderCrafterTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
	private final BaseItemStackHandler inventory = new BaseItemStackHandler(10);
	private int progress;
	private int progressReq;

	public EnderCrafterTileEntity() {
		super(ModTileEntities.ENDER_CRAFTER.get());
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
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
		if (world != null && !world.isRemote()) {
			TableCrafting crafting = new TableCrafting(new EmptyContainer(), this);
			IEnderCraftingRecipe recipe = EnderCrafterRecipeManager.getInstance().findMatchingRecipe(crafting, world);
			ItemStack result = recipe == null ? ItemStack.EMPTY : ((IRecipe) recipe).getCraftingResult(crafting);
			ItemStack output = this.getResult();
			if (!result.isEmpty() && (output.isEmpty() || StackHelper.canCombineStacks(output, result))) {
				List<BlockPos> alternators = this.getAlternatorPositions();
				int alternatorCount = alternators.size();

				if (alternatorCount > 0) {
					this.progress(alternatorCount, recipe.getEnderCrafterTimeSeconds());

					for (BlockPos pos : alternators) {
						if (world.isAirBlock(pos.up())) {
							((WorldServer) world).spawnParticle(EnumParticleTypes.PORTAL, false, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 1, 0, 0, 0, 0.1D);
						}
					}

					if (this.progress >= this.progressReq) {
						for (int i = 0; i < this.matrix.size(); i++) {
							this.decrStackSize(i, 1);
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
		}
	}

	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return null;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	public void updateResult(ItemStack result) {
		if (this.result.isEmpty()) {
			this.setResult(result);
		} else {
			this.result.grow(result.getCount());
		}
	}

	public List<BlockPos> getAlternatorPositions() {
		ArrayList<BlockPos> alternators = new ArrayList<BlockPos>();
		Iterable<BlockPos> blocks = this.getPos().getAllInBox(this.getPos().add(-3, -3, -3), this.getPos().add(3, 3, 3));
		for (BlockPos aoePos : blocks) {
			Block block = this.getWorld().getBlockState(aoePos).getBlock();
			if (block instanceof EnderAlternatorBlock) {
				alternators.add(aoePos);
			}
		}

		return alternators;
	}

	public int getProgress() {
		return this.progress;
	}

	private void progress(int alternators, int timeRequired) {
		this.progress++;

		int timeReq = 20 * timeRequired;
		this.progressReq = (int) Math.max(timeReq - (timeReq * (ModConfigs.confEnderAlternatorEff * alternators)), 20);
	}

	public int getProgressRequired() {
		return this.progressReq;
	}
}
