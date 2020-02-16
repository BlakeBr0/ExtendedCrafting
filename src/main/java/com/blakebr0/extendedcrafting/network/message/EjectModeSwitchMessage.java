package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EjectModeSwitchMessage {
    private BlockPos pos;

    public EjectModeSwitchMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static EjectModeSwitchMessage read(PacketBuffer buffer) {
        return new EjectModeSwitchMessage(buffer.readBlockPos());
    }

    public static void write(EjectModeSwitchMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
    }

    public static void onMessage(EjectModeSwitchMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                World world = player.getEntityWorld();
                TileEntity tile = world.getTileEntity(message.pos);
                if (tile instanceof CompressorTileEntity) {
                    CompressorTileEntity compressor = (CompressorTileEntity) tile;
                    compressor.toggleEjecting();
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
