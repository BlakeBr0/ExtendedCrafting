package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.BaseEnergyStorage;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class CompressorTileEntity extends BaseInventoryTileEntity implements MenuProvider {
	private final LazyOptional<IEnergyStorage> energyCapability = LazyOptional.of(this::getEnergy);
	private final BaseItemStackHandler inventory;
	private final BaseItemStackHandler recipeInventory;
	private final BaseEnergyStorage energy;
	private final CachedRecipe<ICompressorRecipe> recipe;
	private ItemStack materialStack = ItemStack.EMPTY;
	private List<MaterialInput> inputs = NonNullList.create();
	private int materialCount;
	private int progress;
	private boolean ejecting = false;
	private boolean inputLimit = true;

	public CompressorTileEntity(BlockPos pos, BlockState state) {
		super(ModTileEntities.COMPRESSOR.get(), pos, state);
		this.inventory = createInventoryHandler(null);
		this.recipeInventory = BaseItemStackHandler.create(2);
		this.energy = new BaseEnergyStorage(ModConfigs.COMPRESSOR_POWER_CAPACITY.get(), this::setChangedFast);
		this.recipe = new CachedRecipe<>(ModRecipeTypes.COMPRESSOR.get());
	}

	@Override
	public BaseItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.materialCount = tag.getInt("MaterialCount");
		this.materialStack = ItemStack.of(tag.getCompound("MaterialStack"));
		this.progress = tag.getInt("Progress");
		this.ejecting = tag.getBoolean("Ejecting");
		this.energy.deserializeNBT(tag.get("Energy"));
		this.inputLimit = tag.getBoolean("InputLimit");

		this.inputs = loadMaterialInputs(tag);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("MaterialCount", this.materialCount);
		tag.put("MaterialStack", this.materialStack.serializeNBT());
		tag.putInt("Progress", this.progress);
		tag.putBoolean("Ejecting", this.ejecting);
		tag.putInt("Energy", this.energy.getEnergyStored());
		tag.putBoolean("InputLimit", this.inputLimit);

		saveMaterialInputs(tag, this.inputs);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.isRemoved() && cap == ForgeCapabilities.ENERGY) {
			return ForgeCapabilities.ENERGY.orEmpty(cap, this.energyCapability);
		}

		return super.getCapability(cap, side);
	}

	@Override
	public Component getDisplayName() {
		return Localizable.of("container.extendedcrafting.compressor").build();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
		return CompressorContainer.create(windowId, playerInventory, this.inventory, this.getBlockPos());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CompressorTileEntity tile) {
		var recipe = tile.getActiveRecipe();
		var output = tile.inventory.getStackInSlot(0);
		var input = tile.inventory.getStackInSlot(1);

		if (!input.isEmpty()) {
			if (tile.materialStack.isEmpty() || tile.materialCount <= 0) {
				tile.materialStack = input.copy();
				tile.setChangedFast();
			}

			if (!tile.inputLimit || (recipe != null && tile.materialCount < recipe.getInputCount())) {
				var index = tile.canInsertItem(input);
				if (index > -1) {
					tile.insertItem(index, input);
					tile.setChangedFast();
				}
			}
		}

		if (recipe != null && tile.getEnergy().getEnergyStored() > 0) {
			if (tile.materialCount >= recipe.getInputCount()) {
				if (tile.progress >= recipe.getPowerCost()) {
					var result = recipe.assemble(tile.recipeInventory.asRecipeWrapper(), level.registryAccess());

					if (StackHelper.canCombineStacks(result, output)) {
						tile.updateResult(result);
						tile.progress = 0;
						tile.materialCount -= recipe.getInputCount();

						tile.consumeInputs(recipe.getInputCount());

						if (tile.materialCount <= 0) {
							tile.materialStack = ItemStack.EMPTY;
							tile.ejecting = false;
						}

						tile.setChangedFast();
					}
				} else {
					tile.process(recipe);
					tile.setChangedFast();
				}
			}
		}

		if (tile.ejecting && !tile.inputs.isEmpty()) {
			var newestInput = tile.getNewestInput();
			var newestStack = newestInput.stack;

			if (tile.materialCount > 0 && !newestStack.isEmpty() && (output.isEmpty() || StackHelper.areStacksEqual(newestStack, output))) {
				int addCount = Math.min(newestInput.count, newestStack.getMaxStackSize() - output.getCount());
				if (addCount > 0) {
					var toAdd = StackHelper.withSize(newestStack, addCount, false);

					tile.updateResult(toAdd);
					tile.materialCount -= addCount;

					newestInput.count -= addCount;

					if (newestInput.count <= 0) {
						tile.inputs.remove(tile.inputs.size() - 1);
					}

					if (tile.materialCount < 1) {
						tile.materialStack = ItemStack.EMPTY;
						tile.ejecting = false;
					}

					if (tile.progress > 0)
						tile.progress = 0;

					tile.setChangedFast();
				}
			}
		}

		tile.dispatchIfChanged();
	}

	public static BaseItemStackHandler createInventoryHandler() {
		return createInventoryHandler(null);
	}

	public static BaseItemStackHandler createInventoryHandler(Runnable onContentsChanged) {
		return BaseItemStackHandler.create(3, onContentsChanged, builder -> {
			builder.setOutputSlots(0);
			builder.setCanInsert((slot, stack) -> slot == 1);
		});
	}

	public BaseEnergyStorage getEnergy() {
		return this.energy;
	}

	public ItemStack getMaterialStack() {
		return this.materialStack;
	}

	public boolean hasMaterialStack() {
		return !this.materialStack.isEmpty();
	}

	public int getMaterialCount() {
		return this.materialCount;
	}

	public boolean isEjecting() {
		return this.ejecting;
	}

	public void toggleEjecting() {
		if (this.materialCount > 0) {
			this.ejecting = !this.ejecting;
			this.setChangedAndDispatch();
		}
	}

	public boolean isLimitingInput() {
		return this.inputLimit;
	}

	public void toggleInputLimit() {
		this.inputLimit = !this.inputLimit;
		this.setChangedAndDispatch();
	}

	public int getProgress() {
		return this.progress;
	}

	public boolean hasRecipe() {
		return this.recipe.exists();
	}

	public ICompressorRecipe getActiveRecipe() {
		if (this.level == null)
			return null;

		var catalyst = this.inventory.getStackInSlot(2);

		this.recipeInventory.setStackInSlot(0, this.materialStack);
		this.recipeInventory.setStackInSlot(1, catalyst);

		return this.recipe.checkAndGet(this.recipeInventory, this.level);
	}

	public int getEnergyRequired() {
		if (this.hasRecipe())
			return this.recipe.get().getPowerCost();

		return 0;
	}

	public int getMaterialsRequired() {
		if (this.hasRecipe())
			return this.recipe.get().getInputCount();

		return 0;
	}

	public List<MaterialInput> getInputs() {
		return this.inputs;
	}

	private void process(ICompressorRecipe recipe) {
		int extract = recipe.getPowerRate();
		int difference = recipe.getPowerCost() - this.progress;
		if (difference < extract)
			extract = difference;

		int extracted = this.energy.extractEnergy(extract, false);
		this.progress += extracted;
	}

	private void updateResult(ItemStack stack) {
		var result = this.inventory.getStackInSlot(0);

		if (result.isEmpty()) {
			this.inventory.setStackInSlot(0, stack);
		} else {
			this.inventory.setStackInSlot(0, StackHelper.grow(result, stack.getCount()));
		}
	}

	private int canInsertItem(ItemStack stack) {
		var size = this.inputs.size();
		if (size == 0)
			return 0;

		for (int i = 0; i < size; i++) {
			var input = this.inputs.get(i);
			if (StackHelper.areStacksEqual(stack, input.stack))
				return i;
		}

		// if there's a valid recipe, we can start allowing item variants
		if (size < 100 && this.recipe.exists()) {
			var recipeStack = this.recipe.get().getIngredients().get(0);
			if (recipeStack.test(stack))
				return size;
		}

		return -1;
	}

	private void insertItem(int index, ItemStack stack) {
		int consumeAmount = stack.getCount();
		if (this.inputLimit) {
			consumeAmount = Math.min(consumeAmount, this.recipe.get().getInputCount() - this.materialCount);
		}

		if (this.inputs.isEmpty() || this.inputs.size() == index) {
			this.inputs.add(new MaterialInput(stack.copy(), consumeAmount));
		} else {
			var input = this.inputs.get(index);

			if (StackHelper.areStacksEqual(stack, input.stack)) {
				input.count += consumeAmount;
			} else {
				this.inputs.add(new MaterialInput(stack.copy(), consumeAmount));
			}
		}

		stack.shrink(consumeAmount);

		this.materialCount += consumeAmount;
	}

	private MaterialInput getNewestInput() {
		return this.inputs.get(this.inputs.size() - 1);
	}

	private void consumeInputs(int amount) {
		for (int i = this.inputs.size() - 1; i > -1; i--) {
			var input = this.inputs.get(i);
			if (input.count > amount) {
				input.count -= amount;
				break;
			} else {
				amount -= input.count;
				this.inputs.remove(i);
			}
		}
	}

	private static List<MaterialInput> loadMaterialInputs(CompoundTag tag) {
		var list = tag.getList("Inputs", 10);
		var inputs = new ArrayList<MaterialInput>();

		for (int i = 0; i < list.size(); i++) {
			inputs.add(MaterialInput.load(list.getCompound(i)));
		}

		// backwards compatibility
		// if there is a material stack set but no inputs then we add it as an input
		if (tag.contains("MaterialStack") && inputs.isEmpty()) {
			var stack = ItemStack.of(tag.getCompound("MaterialStack"));
			var count = tag.getInt("MaterialCount");

			if (count > 0) {
				inputs.add(new MaterialInput(stack, count));
			}
		}

		return inputs;
	}

	private static void saveMaterialInputs(CompoundTag tag, List<MaterialInput> inputs) {
		var list = new ListTag();
		for (var input : inputs) {
			list.add(input.save());
		}

		tag.put("Inputs", list);
	}

	public static class MaterialInput {
		public ItemStack stack;
		public int count;

		public MaterialInput(ItemStack stack, int count) {
			this.stack = stack;
			this.count = count;
		}

		public Component getDisplayName() {
			return Component.literal(this.count + "x ").append(this.stack.getHoverName());
		}

		public CompoundTag save() {
			var tag = new CompoundTag();

			tag.put("Item", stack.save(new CompoundTag()));
			tag.putInt("Count", count);

			return tag;
		}

		public static MaterialInput load(CompoundTag tag) {
			var stack = ItemStack.of(tag.getCompound("Item"));
			var count = tag.getInt("Count");

			return new MaterialInput(stack, count);
		}
	}
}
