package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class InputLimitSwitchMessage {
    private BlockPos pos;

    public InputLimitSwitchMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static InputLimitSwitchMessage read(PacketBuffer buffer) {
        return new InputLimitSwitchMessage(buffer.readBlockPos());
    }

    public static void write(InputLimitSwitchMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
    }

    public static void onMessage(InputLimitSwitchMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                World world = player.getEntityWorld();
                TileEntity tile = world.getTileEntity(message.pos);
                if (tile instanceof CompressorTileEntity) {
                    CompressorTileEntity compressor = (CompressorTileEntity) tile;
                    compressor.toggleInputLimit();
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
