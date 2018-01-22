package com.blakebr0.extendedcrafting.tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.block.BlockPedestal;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.lib.CustomEnergyStorage;
import com.blakebr0.extendedcrafting.util.VanillaPacketDispatcher;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileCraftingCore extends TileEntity implements ITickable {

	private final ItemStackHandler inventory = new StackHandler(1);
	private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfig.confCraftingCoreRFCapacity);

	public CombinationRecipe activeRecipe;
	private int progress;
	private int oldEnergy;
	private int pedestalCount;

	private static List<CombinationRecipe> getValidRecipes(ItemStack stack) {
		List<CombinationRecipe> valid = new ArrayList<CombinationRecipe>();
		if (!StackHelper.isNull(stack)) {
			for (CombinationRecipe recipe : CombinationRecipeManager.getInstance().getRecipes()) {
				if (!StackHelper.isNull(recipe.getInput()) && recipe.getInput().isItemEqual(stack)) {
					valid.add(recipe);
				}
			}
		}
		return valid;
	}

	@Override
	public void update() {
		boolean mark = false;

		locatePedestals();

		if (!this.getWorld().isRemote) {
			CombinationRecipe recipe = this.getRecipe();
			if (recipe != null) {
				if (this.getEnergy().getEnergyStored() > 0) {
					List<TilePedestal> pedestals = this.getPedestalsWithStuff(recipe);
					boolean done = this.process(recipe);
					if (done) {
						for (TilePedestal pedestal : pedestals) {
							pedestal.getInventory().setStackInSlot(0, StackHelper.decrease(pedestal.getInventory().getStackInSlot(0), 1, true));
							pedestal.markDirty();
							((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false, pedestal.getPos().getX() + 0.5D, pedestal.getPos().getY() + 1.1D, pedestal.getPos().getZ() + 0.5D, 20, 0, 0, 0, 0.1D);
						}
						((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.END_ROD, false, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.1D, this.getPos().getZ() + 0.5D, 50, 0, 0, 0, 0.1D);
						this.getInventory().setStackInSlot(0, recipe.getOutput().copy());
						this.progress = 0;
						mark = true;
					} else {
						((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.SPELL, false, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.1D, this.getPos().getZ() + 0.5D, 2, 0, 0, 0, 0.1D);
					}
				}
			} else {
				progress = 0;
			}
		}

		if (this.oldEnergy != this.energy.getEnergyStored()) {
			this.oldEnergy = this.energy.getEnergyStored();
			if (!mark) {
				mark = true;
			}
		}

		if (mark) {
			this.markDirty();
		}
	}

	private List<BlockPos> locatePedestals() {
		ArrayList<BlockPos> pedestals = new ArrayList<BlockPos>();
		Iterable<BlockPos> blocks = this.getPos().getAllInBox(this.getPos().add(-3, 0, -3), this.getPos().add(3, 0, 3));
		for (BlockPos aoePos : blocks) {
			Block block = this.getWorld().getBlockState(aoePos).getBlock();
			if (block instanceof BlockPedestal) {
				pedestals.add(aoePos);
			}
		}
		this.pedestalCount = pedestals.size();
		return pedestals;
	}

	private List<TilePedestal> getPedestalsWithStuff(CombinationRecipe recipe) {
		ArrayList<Object> remaining = new ArrayList<Object>(recipe.getPedestalItems());
		ArrayList<TilePedestal> pedestals = new ArrayList<TilePedestal>();
		ArrayList<BlockPos> locations = new ArrayList<BlockPos>(locatePedestals());

		if (locations.isEmpty()) {
			return null;
		}

		for (BlockPos pos : locations) {
			TileEntity tile = this.getWorld().getTileEntity(pos);
			if (tile instanceof TilePedestal) {
				TilePedestal pedestal = (TilePedestal) tile;
				Iterator<Object> rem = remaining.iterator();
				while (rem.hasNext()) {
					boolean match = false;
					Object next = rem.next();
					ItemStack stack = pedestal.getInventory().getStackInSlot(0);
					if (next instanceof ItemStack) {
						match = OreDictionary.itemMatches((ItemStack) next, stack, false);
					} else if (next instanceof List) {
						Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
						while (itr.hasNext()) {
							match = OreDictionary.itemMatches(itr.next(), stack, false);
							if (match) break;
						}
					}
					
					if (match) {
						pedestals.add(pedestal);
						remaining.remove(next);
						break;
					}
				}
			}
		}
		if (pedestals.size() != recipe.getPedestalItems().size()) {
			return null;
		}
		if (!remaining.isEmpty()) {
			return null;
		}
		return pedestals;
	}

	private boolean process(CombinationRecipe recipe) {
		int extract = recipe.getPerTick();
		int difference = recipe.getCost() - this.progress;
		if (difference < recipe.getPerTick()) {
			extract = difference;
		}
		int extracted = this.getEnergy().extractEnergy(extract, false);
		this.progress += extracted;
		if (this.progress >= recipe.getCost()) {
			return true;
		}
		return false;
	}

	public int getProgress() {
		return progress;
	}

	public CombinationRecipe getActiveRecipe() {
		return activeRecipe;
	}

	public CombinationRecipe getRecipe() {
		List<CombinationRecipe> recipes = getValidRecipes(this.getInventory().getStackInSlot(0));
		if (!recipes.isEmpty()) {
			for (CombinationRecipe recipe : recipes) {
				List<TilePedestal> pedestals = this.getPedestalsWithStuff(recipe);
				if (pedestals != null) {
					return recipe;
				}
			}
		}
		return null;
	}

	public IItemHandlerModifiable getInventory() {
		return inventory;
	}

	public CustomEnergyStorage getEnergy() {
		return energy;
	}

	public int getPedestalCount() {
		return pedestalCount;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inventory.deserializeNBT(tag);
		progress = tag.getInteger("Progress");
		energy.setEnergy(tag.getInteger("Energy"));
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.merge(inventory.serializeNBT());
		tag.setInteger("Progress", progress);
		tag.setInteger("Energy", energy.getEnergyStored());
		return tag;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, -1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public final NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing side) {
		return this.getCapability(capability, side) != null;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().add(0.5, 0.5, 0.5)) <= 64;
	}

	@Nonnull
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		} else if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energy);
		}
		return super.getCapability(capability, side);
	}

	class StackHandler extends ItemStackHandler {
		StackHandler(int size) {
			super(size);
		}

		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return 1;
		}

		@Override
		public void onContentsChanged(int slot) {
			TileCraftingCore.this.markDirty();
		}
	}
}
