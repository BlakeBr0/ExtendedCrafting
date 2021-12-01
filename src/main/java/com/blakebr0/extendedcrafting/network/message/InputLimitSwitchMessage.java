package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InputLimitSwitchMessage extends Message<InputLimitSwitchMessage> {
    private final BlockPos pos;

    public InputLimitSwitchMessage() {
        this.pos = BlockPos.ZERO;
    }

    public InputLimitSwitchMessage(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public InputLimitSwitchMessage read(FriendlyByteBuf buffer) {
        return new InputLimitSwitchMessage(buffer.readBlockPos());
    }

    @Override
    public void write(InputLimitSwitchMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
    }

    @Override
    public void onMessage(InputLimitSwitchMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();

            if (player != null) {
                var level = player.getCommandSenderWorld();
                var tile = level.getBlockEntity(message.pos);

                if (tile instanceof CompressorTileEntity compressor)
                    compressor.toggleInputLimit();
            }
        });

        context.get().setPacketHandled(true);
    }
}
