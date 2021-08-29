package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.cucumber.network.message.LoginMessage;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class AcknowledgeMessage extends LoginMessage<AcknowledgeMessage> {
    public AcknowledgeMessage() { }

    @Override
    public AcknowledgeMessage read(FriendlyByteBuf buffer) {
        return new AcknowledgeMessage();
    }

    @Override
    public void write(AcknowledgeMessage message, FriendlyByteBuf buffer) { }

    @Override
    public void onMessage(AcknowledgeMessage message, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isClient()) {
            NetworkHandler.INSTANCE.reply(new AcknowledgeMessage(), context.get());
        }

        context.get().setPacketHandled(true);
    }
}
