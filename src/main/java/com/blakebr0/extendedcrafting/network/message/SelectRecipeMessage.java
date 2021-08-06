package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SelectRecipeMessage {
    private final BlockPos pos;
    private final int selected;

    public SelectRecipeMessage(BlockPos pos, int selected) {
        this.pos = pos;
        this.selected = selected;
    }

    public static SelectRecipeMessage read(FriendlyByteBuf buffer) {
        return new SelectRecipeMessage(buffer.readBlockPos(), buffer.readVarInt());
    }

    public static void write(SelectRecipeMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeVarInt(message.selected);
    }

    public static void onMessage(SelectRecipeMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                Level world = player.getCommandSenderWorld();
                BlockEntity tile = world.getBlockEntity(message.pos);
                if (tile instanceof AutoTableTileEntity) {
                    AutoTableTileEntity table = (AutoTableTileEntity) tile;
                    table.selectRecipe(message.selected);
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
