package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DeleteRecipeMessage {
    private final BlockPos pos;
    private final int selected;

    public DeleteRecipeMessage(BlockPos pos, int selected) {
        this.pos = pos;
        this.selected = selected;
    }

    public static DeleteRecipeMessage read(PacketBuffer buffer) {
        return new DeleteRecipeMessage(buffer.readBlockPos(), buffer.readVarInt());
    }

    public static void write(DeleteRecipeMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeVarInt(message.selected);
    }

    public static void onMessage(DeleteRecipeMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                World world = player.getEntityWorld();
                TileEntity tile = world.getTileEntity(message.pos);
                if (tile instanceof AutoTableTileEntity) {
                    ((AutoTableTileEntity) tile).getRecipeStorage().unsetRecipe(message.selected);
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
