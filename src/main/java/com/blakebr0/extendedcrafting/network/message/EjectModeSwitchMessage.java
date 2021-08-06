package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EjectModeSwitchMessage {
    private final BlockPos pos;

    public EjectModeSwitchMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static EjectModeSwitchMessage read(FriendlyByteBuf buffer) {
        return new EjectModeSwitchMessage(buffer.readBlockPos());
    }

    public static void write(EjectModeSwitchMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
    }

    public static void onMessage(EjectModeSwitchMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                Level world = player.getCommandSenderWorld();
                BlockEntity tile = world.getBlockEntity(message.pos);
                if (tile instanceof CompressorTileEntity) {
                    ((CompressorTileEntity) tile).toggleEjecting();
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
