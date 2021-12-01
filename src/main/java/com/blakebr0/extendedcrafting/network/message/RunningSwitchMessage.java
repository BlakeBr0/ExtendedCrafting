package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RunningSwitchMessage extends Message<RunningSwitchMessage> {
    private final BlockPos pos;

    public RunningSwitchMessage() {
        this.pos = BlockPos.ZERO;
    }

    public RunningSwitchMessage(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public RunningSwitchMessage read(FriendlyByteBuf buffer) {
        return new RunningSwitchMessage(buffer.readBlockPos());
    }

    @Override
    public void write(RunningSwitchMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
    }

    @Override
    public void onMessage(RunningSwitchMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();

            if (player != null) {
                var level = player.getCommandSenderWorld();
                var tile = level.getBlockEntity(message.pos);

                if (tile instanceof AutoTableTileEntity table)
                    table.toggleRunning();
            }
        });

        context.get().setPacketHandled(true);
    }
}
