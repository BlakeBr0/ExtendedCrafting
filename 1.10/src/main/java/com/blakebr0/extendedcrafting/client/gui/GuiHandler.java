package com.blakebr0.extendedcrafting.client.gui;

import com.blakebr0.extendedcrafting.client.container.ContainerCraftingCore;
import com.blakebr0.extendedcrafting.tile.TileCraftingCore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0){
            return new GuiCraftingCore((TileCraftingCore)world.getTileEntity(new BlockPos(x, y, z)), new ContainerCraftingCore(player.inventory, (TileCraftingCore)world.getTileEntity(new BlockPos(x, y, z))));
        }
        return null;
    }

    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0){
            return new ContainerCraftingCore(player.inventory, (TileCraftingCore)world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }

}
