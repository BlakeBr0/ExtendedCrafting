package com.blakebr0.extendedcrafting.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.blakebr0.cucumber.energy.CustomEnergyStorage;
import com.blakebr0.cucumber.energy.EnergyStorageCustom;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.VanillaPacketDispatcher;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
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

public class CraftingCoreTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
	private final BaseItemStackHandler inventory = new BaseItemStackHandler(1);
	private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfigs.CRAFTING_CORE_POWER_CAPACITY.get());
	private int progress;
	private int oldEnergy;
	private int pedestalCount;

	public CraftingCoreTileEntity() {
		super(ModTileEntities.CRAFTING_CORE.get());
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		this.inventory.deserializeNBT(tag);
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

		List<BlockPos> pedestalLocations = this.locatePedestals();

		if (this.getWorld() != null && !this.getWorld().isRemote()) {
			CombinationRecipe recipe = this.getRecipe();
			if (recipe != null) {
				if (this.getEnergy().getEnergyStored() > 0) {
					List<PedestalTileEntity> pedestals = this.getPedestalsWithStuff(recipe, pedestalLocations);
					boolean done = this.process(recipe);
					if (done) {
						for (PedestalTileEntity pedestal : pedestals) {
							IItemHandlerModifiable inventory = pedestal.getInventory();
							inventory.setStackInSlot(0, StackHelper.decrease(inventory.getStackInSlot(0), 1, true));
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
				this.progress = 0;
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

	private List<BlockPos> locatePedestals() {
		ArrayList<BlockPos> pedestals = new ArrayList<>();
		Iterable<BlockPos> blocks = BlockPos.getAllInBox(this.getPos().add(-3, 0, -3), this.getPos().add(3, 0, 3));

		for (BlockPos aoePos : blocks) {
			Block block = this.getWorld().getBlockState(aoePos).getBlock();
			if (block instanceof PedestalBlock) {
				pedestals.add(aoePos);
			}
		}

		this.pedestalCount = pedestals.size();

		return pedestals;
	}

	private List<PedestalTileEntity> getPedestalsWithStuff(CombinationRecipe recipe, List<BlockPos> locations) {
		ArrayList<Object> remaining = new ArrayList<>(recipe.getPedestalItems());
		ArrayList<PedestalTileEntity> pedestals = new ArrayList<>();

		if (locations.isEmpty()) return null;

		for (BlockPos pos : locations) {
			TileEntity tile = this.getWorld().getTileEntity(pos);
			if (tile instanceof PedestalTileEntity) {
				PedestalTileEntity pedestal = (PedestalTileEntity) tile;
				Iterator<Object> rem = remaining.iterator();
				while (rem.hasNext()) {
					boolean match = false;
					Object next = rem.next();
					ItemStack stack = pedestal.getInventory().getStackInSlot(0);
					if (next instanceof ItemStack) {
						ItemStack nextStack = (ItemStack) next;
						match = OreDictionary.itemMatches(nextStack, stack, false) && (!nextStack.hasTagCompound() || StackHelper.compareTags(nextStack, stack));
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

		if (pedestals.size() != recipe.getPedestalItems().size())
			return null;

		if (!remaining.isEmpty()) return null;

		return pedestals;
	}

	private boolean process(CombinationRecipe recipe) {
		int extract = recipe.getPowerRate();
		long difference = recipe.getPowerCost() - this.progress;
		if (difference < recipe.getPowerRate()) {
			extract = (int) difference;
		}

		int extracted = this.energy.extractEnergy(extract, false);
		this.progress += extracted;

		return this.progress >= recipe.getPowerCost();
	}

	public CustomEnergyStorage getEnergy() {
		return this.energy;
	}

	public CombinationRecipe getRecipe() {
		return getRecipe(locatePedestals());
	}

	public CombinationRecipe getRecipe(List<BlockPos> locations) {
		List<CombinationRecipe> recipes = getValidRecipes(this.getInventory().getStackInSlot(0));

		if (!recipes.isEmpty()) {
			for (CombinationRecipe recipe : recipes) {
				List<PedestalTileEntity> pedestals = this.getPedestalsWithStuff(recipe, locations);
				if (pedestals != null) {
					return recipe;
				}
			}
		}

		return null;
	}

	public int getProgress() {
		return this.progress;
	}

	public int getPedestalCount() {
		return this.pedestalCount;
	}
}
