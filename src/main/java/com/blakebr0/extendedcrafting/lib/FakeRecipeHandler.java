package com.blakebr0.extendedcrafting.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class FakeRecipeHandler extends ItemStackHandler {
	
	public FakeRecipeHandler() {
		super();
	}
	
	public NonNullList<ItemStack> getStacks() {
		return this.stacks;
	}
	
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.stacks.size(); i++) {
            if (!this.stacks.get(i).isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                this.stacks.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("FakeItems", nbtTagList);
        nbt.setInteger("FakeSize", this.stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setSize(nbt.hasKey("FakeSize", Constants.NBT.TAG_INT) ? nbt.getInteger("FakeSize") : this.stacks.size());
        NBTTagList tagList = nbt.getTagList("FakeItems", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < this.stacks.size()) {
                this.stacks.set(slot, new ItemStack(itemTags));
            }
        }
        onLoad();
    }
}
