package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.network.NetworkHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class AcknowledgeMessage implements IntSupplier {
    private int loginIndex;

    public AcknowledgeMessage() { }

    @Override
    public int getAsInt() {
        return this.loginIndex;
    }

    public int getLoginIndex() {
        return this.loginIndex;
    }

    public void setLoginIndex(int loginIndex) {
        this.loginIndex = loginIndex;
    }

    public static AcknowledgeMessage read(PacketBuffer buffer) {
        return new AcknowledgeMessage();
    }

    public static void write(AcknowledgeMessage message, PacketBuffer buffer) { }

    public static void onMessage(AcknowledgeMessage message, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isClient()) {
            NetworkHandler.INSTANCE.reply(new AcknowledgeMessage(), context.get());
        }

        context.get().setPacketHandled(true);
    }
}
