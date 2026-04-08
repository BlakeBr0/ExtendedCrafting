package com.blakebr0.extendedcrafting.tileentity;

import com.blakebr0.cucumber.energy.CEnergyStorage;
import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.inventory.OnContentsChangedFunction;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.ContainerDataBuilder;
import com.blakebr0.extendedcrafting.api.crafting.ICompressorRecipe;
import com.blakebr0.extendedcrafting.config.ModConfigs;
import com.blakebr0.extendedcrafting.container.CompressorContainer;
import com.blakebr0.extendedcrafting.init.ModRecipeTypes;
import com.blakebr0.extendedcrafting.init.ModTileEntities;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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

import java.util.ArrayList;
import java.util.List;

public class CompressorTileEntity extends BaseInventoryTileEntity implements MenuProvider {
    private final CItemStacksHandler inventory;
    private final CItemStacksHandler recipeInventory;
    private final CEnergyStorage energy;
    private final CachedRecipe<CraftingInput, ICompressorRecipe> recipe;
    private ItemStack materialStack = ItemStack.EMPTY;
    private List<MaterialInput> inputs = NonNullList.create();
    private int materialCount;
    private int progress;
    private boolean ejecting = false;
    private boolean inputLimit = true;

    private final ContainerData dataAccess;

    public CompressorTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.COMPRESSOR.get(), pos, state);
        this.inventory = createInventoryHandler((_, _) -> this.setChanged());
        this.recipeInventory = CItemStacksHandler.create(2);
        this.energy = new CEnergyStorage(ModConfigs.COMPRESSOR_POWER_CAPACITY.get(), _ -> this.setChangedFast());
        this.recipe = new CachedRecipe<>(ModRecipeTypes.COMPRESSOR.get());

        this.dataAccess = ContainerDataBuilder.builder()
                .sync(this.energy::getAmountAsInt, this.energy::set)
                .sync(this.energy::getCapacityAsInt, this.energy::setMaxCapacity)
                .sync(() -> this.progress, value -> this.progress = value)
                .sync(this::getEnergyRequired)
                .sync(() -> this.materialCount, value -> this.materialCount = value)
                .sync(this::getMaterialsRequired)
                .sync(() -> this.ejecting ? 1 : 0, value -> this.ejecting = value != 0)
                .sync(() -> this.inputLimit ? 1 : 0, value -> this.inputLimit = value != 0)
                .build();
    }

    @Override
    public CItemStacksHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.materialCount = input.getIntOr("MaterialCount", 0);
        this.materialStack = input.read("MaterialStack", ItemStack.OPTIONAL_CODEC).orElse(null);
        this.progress = input.getIntOr("Progress", 0);
        this.ejecting = input.getBooleanOr("Ejecting", false);
        this.energy.deserialize(input);
        this.inputLimit = input.getBooleanOr("InputLimit", false);
        this.inputs = input.read("MaterialStacks", MaterialInput.CODEC.listOf()).orElseGet(ArrayList::new);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("MaterialCount", this.materialCount);
        output.storeNullable("MaterialStack", ItemStack.OPTIONAL_CODEC, this.materialStack);
        output.putInt("Progress", this.progress);
        output.putBoolean("Ejecting", this.ejecting);
        this.energy.serialize(output);
        output.putBoolean("InputLimit", this.inputLimit);
        output.store("MaterialStacks", MaterialInput.CODEC.listOf(), this.inputs);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.extendedcrafting.compressor");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        return new CompressorContainer(windowId, playerInventory, this.inventory, this.dataAccess, this.getBlockPos());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CompressorTileEntity tile) {
        var recipe = tile.getActiveRecipe();
        var output = tile.inventory.getResource(0);
        var input = tile.inventory.getResource(1);

        if (!input.isEmpty()) {
            if (tile.materialStack.isEmpty() || tile.materialCount <= 0) {
                tile.materialStack = input.toStack();
                tile.setChangedFast();
            }

            if (!tile.inputLimit || (recipe != null && tile.materialCount < recipe.getIngredient().count())) {
                var inputAmount = tile.inventory.getAmountAsInt(1);
                var index = tile.canInsertItem(input.toStack());
                if (index > -1) {
                    try (var tx = Transaction.openRoot()) {
                        tile.insertItem(index, input, inputAmount, tx);
                        tx.commit();
                    }

                    tile.setChangedFast();
                }
            }
        }

        if (recipe != null && tile.getEnergy().getAmountAsInt() > 0) {
            if (tile.materialCount >= recipe.getIngredient().count()) {
                if (tile.progress >= recipe.getPowerCost()) {
                    var result = recipe.assemble(tile.toCraftingInput());
                    var canFit = result.getCount() + tile.inventory.getAmountAsInt(0) <= output.getMaxStackSize();

                    if (canFit && output.matches(result)) {
                        var amount = recipe.getIngredient().count();

                        tile.updateResult(result);
                        tile.progress = 0;
                        tile.materialCount -= amount;

                        tile.consumeInputs(amount);

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

            if (tile.materialCount > 0 && !newestStack.isEmpty() && (output.isEmpty() || output.matches(newestStack))) {
                int addCount = Math.min(newestInput.count, newestStack.getMaxStackSize() - tile.inventory.getAmountAsInt(0));
                if (addCount > 0) {
                    var toAdd = newestStack.copyWithCount(addCount);

                    tile.updateResult(toAdd);
                    tile.materialCount -= addCount;

                    newestInput.count -= addCount;

                    if (newestInput.count <= 0) {
                        tile.inputs.removeLast();
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

    public static CItemStacksHandler createInventoryHandler() {
        return createInventoryHandler(null);
    }

    public static CItemStacksHandler createInventoryHandler(OnContentsChangedFunction onContentsChanged) {
        return CItemStacksHandler.create(3, onContentsChanged, builder -> {
            builder.setOutputSlots(0);
            builder.setCanInsert((slot, _) -> slot == 1);
        });
    }

    public CEnergyStorage getEnergy() {
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

        var catalyst = this.inventory.getResource(2);

        this.recipeInventory.set(0, ItemResource.of(this.materialStack), 1);
        this.recipeInventory.set(1, catalyst, 1);

        return this.recipe.checkAndGet(this.toCraftingInput(), (ServerLevel) this.level);
    }

    public int getEnergyRequired() {
        if (this.hasRecipe())
            return this.recipe.get().getPowerCost();

        return 0;
    }

    public int getMaterialsRequired() {
        if (this.hasRecipe())
            return this.recipe.get().getIngredient().count();

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

        try (var tx = Transaction.openRoot()) {
            int extracted = this.energy.extract(extract, tx);
            this.progress += extracted;
            tx.commit();
        }
    }

    private void updateResult(ItemStack stack) {
        var result = this.inventory.getResource(9);

        if (result.isEmpty()) {
            this.inventory.set(9, ItemResource.of(stack), stack.getCount());
        } else {
            var amount = this.inventory.getAmountAsInt(0);
            this.inventory.set(9, result, amount + stack.getCount());
        }
    }

    private int canInsertItem(ItemStack stack) {
        var size = this.inputs.size();
        if (size == 0)
            return 0;

        for (int i = 0; i < size; i++) {
            var input = this.inputs.get(i);
            if (ItemStack.isSameItemSameComponents(stack, input.stack))
                return i;
        }

        // if there's a valid recipe, we can start allowing stack variants
        if (size < 100 && this.recipe.exists()) {
            var recipeStack = this.recipe.get().getIngredient().ingredient();
            if (recipeStack.test(stack))
                return size;
        }

        return -1;
    }

    private void insertItem(int index, ItemResource resource, int amount, TransactionContext tx) {
        int consumeAmount = amount;
        if (this.inputLimit) {
            consumeAmount = Math.min(consumeAmount, this.recipe.get().getIngredient().count() - this.materialCount);
        }

        if (this.inputs.isEmpty() || this.inputs.size() == index) {
            this.inputs.add(new MaterialInput(resource.toStack(), consumeAmount));
        } else {
            var input = this.inputs.get(index);

            if (resource.matches(input.stack)) {
                input.count += consumeAmount;
            } else {
                this.inputs.add(new MaterialInput(resource.toStack(), consumeAmount));
            }
        }

        this.inventory.extract(1, resource, consumeAmount, tx, true);
        this.materialCount += consumeAmount;
    }

    private MaterialInput getNewestInput() {
        return this.inputs.getLast();
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

    private CraftingInput toCraftingInput() {
        return this.recipeInventory.toShapelessCraftingInput();
    }

    public static final class MaterialInput {
        public static final MapCodec<MaterialInput> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        ItemStack.CODEC.fieldOf("stack").forGetter(i -> i.stack),
                        Codec.INT.fieldOf("count").forGetter(i -> i.count)
                ).apply(builder, MaterialInput::new)
        );
        public static final Codec<MaterialInput> CODEC = MAP_CODEC.codec();

        public ItemStack stack;
        public int count;

        public MaterialInput(ItemStack stack, int count) {
            this.stack = stack;
            this.count = count;
        }

        public Component getDisplayName() {
            return Component.literal(this.count + "x ").append(this.stack.getHoverName());
        }
    }
}
