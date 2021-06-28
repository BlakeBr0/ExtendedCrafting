package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RunningSwitchMessage {
    private final BlockPos pos;

    public RunningSwitchMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static RunningSwitchMessage read(PacketBuffer buffer) {
        return new RunningSwitchMessage(buffer.readBlockPos());
    }

    public static void write(RunningSwitchMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
    }

    public static void onMessage(RunningSwitchMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                World world = player.getCommandSenderWorld();
                TileEntity tile = world.getBlockEntity(message.pos);
                if (tile instanceof AutoTableTileEntity) {
                    ((AutoTableTileEntity) tile).toggleRunning();
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
