package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CustomEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.lib.Localizable;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.extendedcrafting.block.PedestalBlock;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CraftingCoreContainer;
import com.blakebr0.extendedcrafting.crafting.recipe.CombinationRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;

public class CraftingCoreTileEntity extends BaseInventoryTileEntity implements ITickableTileEntity, INamedContainerProvider {
	private final BaseItemStackHandler inventory = new BaseItemStackHandler(1);
	private final CustomEnergyStorage energy = new CustomEnergyStorage(ModConfigs.CRAFTING_CORE_POWER_CAPACITY.get());
	private int progress;
	private int oldEnergy;
	private int pedestalCount;
	protected IIntArray data = new IIntArray() {
		@Override
		public int get(int i) {
			switch (i) {
				case 0:
					return 0;
				default:
					return 0;
			}
		}

		@Override
		public void set(int i, int value) {

		}

		@Override
		public int size() {
			return 0;
		}
	};

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
		World world = this.getWorld();
		if (world != null && !world.isRemote()) {
			CombinationRecipe recipe = this.getActiveRecipe();
			if (recipe != null) {
				if (this.getEnergy().getEnergyStored() > 0) {
					List<PedestalTileEntity> pedestals = this.getPedestalsWithStuff(recipe, pedestalLocations);
					boolean done = this.process(recipe);
					if (done) {
						for (PedestalTileEntity pedestal : pedestals) {
							IItemHandlerModifiable inventory = pedestal.getInventory();
							inventory.setStackInSlot(0, StackHelper.decrease(inventory.getStackInSlot(0), 1, true));
							pedestal.markDirty();
//							((ServerWorld) world).spawnParticle(ParticleTypes.SMOKE_NORMAL, false, pedestal.getPos().getX() + 0.5D, pedestal.getPos().getY() + 1.1D, pedestal.getPos().getZ() + 0.5D, 20, 0, 0, 0, 0.1D);
						}
//						((ServerWorld) world).spawnParticle(ParticleTypes.END_ROD, false, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.1D, this.getPos().getZ() + 0.5D, 50, 0, 0, 0, 0.1D);
						this.getInventory().setStackInSlot(0, recipe.getRecipeOutput().copy());
						this.progress = 0;
						mark = true;
					} else {
//						((ServerWorld) world).spawnParticle(EnumParticleTypes.SPELL, false, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.1D, this.getPos().getZ() + 0.5D, 2, 0, 0, 0, 0.1D);
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
		World world = this.getWorld();
		if (world != null) {
			BlockPos.getAllInBox(this.getPos().add(-3, 0, -3), this.getPos().add(3, 0, 3)).forEach(aoePos -> {
				Block block = world.getBlockState(aoePos).getBlock();
				if (block instanceof PedestalBlock)
					pedestals.add(aoePos);
			});
		}

		this.pedestalCount = pedestals.size();

		return pedestals;
	}

	private List<PedestalTileEntity> getPedestalsWithStuff(CombinationRecipe recipe, List<BlockPos> locations) {
		ArrayList<Object> remaining = new ArrayList<>(recipe.getIngredients());
		ArrayList<PedestalTileEntity> pedestals = new ArrayList<>();

		if (locations.isEmpty()) return null;
//
//		for (BlockPos pos : locations) {
//			TileEntity tile = this.getWorld().getTileEntity(pos);
//			if (tile instanceof PedestalTileEntity) {
//				PedestalTileEntity pedestal = (PedestalTileEntity) tile;
//				Iterator<Object> rem = remaining.iterator();
//				while (rem.hasNext()) {
//					boolean match = false;
//					Object next = rem.next();
//					ItemStack stack = pedestal.getInventory().getStackInSlot(0);
//					if (next instanceof ItemStack) {
//						ItemStack nextStack = (ItemStack) next;
//						match = OreDictionary.itemMatches(nextStack, stack, false) && (!nextStack.hasTagCompound() || StackHelper.compareTags(nextStack, stack));
//					} else if (next instanceof List) {
//						Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
//						while (itr.hasNext()) {
//							match = OreDictionary.itemMatches(itr.next(), stack, false);
//							if (match) break;
//						}
//					}
//
//					if (match) {
//						pedestals.add(pedestal);
//						remaining.remove(next);
//						break;
//					}
//				}
//			}
//		}
//
//		if (pedestals.size() != recipe.getIngredients().size())
//			return null;
//
//		if (!remaining.isEmpty()) return null;

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

	public CombinationRecipe getActiveRecipe() {
		return getActiveRecipe(locatePedestals());
	}

	public CombinationRecipe getActiveRecipe(List<BlockPos> locations) {
//		List<CombinationRecipe> recipes = getValidRecipes(this.getInventory().getStackInSlot(0));

//		if (!recipes.isEmpty()) {
//			for (CombinationRecipe recipe : recipes) {
//				List<PedestalTileEntity> pedestals = this.getPedestalsWithStuff(recipe, locations);
//				if (pedestals != null) {
//					return recipe;
//				}
//			}
//		}

		return null;
	}

	public int getProgress() {
		return this.progress;
	}

	public int getPedestalCount() {
		return this.pedestalCount;
	}

	@Override
	public ITextComponent getDisplayName() {
		return Localizable.of("container.extendedcrafting.crafting_core").build();
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		return CraftingCoreContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.data);
	}
}
