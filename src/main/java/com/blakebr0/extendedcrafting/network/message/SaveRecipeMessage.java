package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SaveRecipeMessage {
    private final BlockPos pos;
    private final int selected;

    public SaveRecipeMessage(BlockPos pos, int selected) {
        this.pos = pos;
        this.selected = selected;
    }

    public static SaveRecipeMessage read(PacketBuffer buffer) {
        return new SaveRecipeMessage(buffer.readBlockPos(), buffer.readVarInt());
    }

    public static void write(SaveRecipeMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeVarInt(message.selected);
    }

    public static void onMessage(SaveRecipeMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                World world = player.getEntityWorld();
                TileEntity tile = world.getTileEntity(message.pos);
                if (tile instanceof AutoTableTileEntity) {
                    AutoTableTileEntity table = (AutoTableTileEntity) tile;
                    if (!table.getRecipeStorage().hasRecipe(message.selected)) {
                        table.saveRecipe(message.selected);
                    } else {
                        table.deleteRecipe(message.selected);
                    }
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
