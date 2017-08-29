package com.blakebr0.extendedcrafting.util;

import java.util.*;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class StackHelper {
	
	public static ItemStack withSize(ItemStack stack, int size, boolean container){
		if(size <= 0){
			if(container && stack.getItem().hasContainerItem(stack)){
				return stack.getItem().getContainerItem(stack);
			} else {
				return null;
			}
		}
		stack.stackSize = size;
		return stack;
	}
	
	public static ItemStack increase(ItemStack stack, int amount){
		stack.stackSize += amount;
		return withSize(stack, stack.stackSize, false);
	}

	public static ItemStack decrease(ItemStack stack, int amount, boolean container){
		if(isNull(stack)){
			return null;
		}
		stack.stackSize -= amount;
		return withSize(stack, stack.stackSize, container);
	}
	
    public static int getPlaceFromList(List<ItemStack> list, ItemStack stack, boolean wildcard){
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                if((isNull(stack) && isNull(list.get(i))) || areItemsEqual(stack, list.get(i), wildcard)){
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2, boolean wildcard){
        return !isNull(stack1) && !isNull(stack2) && (stack1.isItemEqual(stack2) || (wildcard && stack1.getItem() == stack2.getItem() && (stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE)));
    }
	
	public static boolean isNull(ItemStack stack){
		return stack == null;
	}
	
	public static ItemStack getNull(){
		return null;
	}
}
