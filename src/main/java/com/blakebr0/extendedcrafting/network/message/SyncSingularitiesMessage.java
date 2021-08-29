package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.cucumber.network.message.LoginMessage;
import com.blakebr0.extendedcrafting.network.NetworkHandler;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncSingularitiesMessage extends LoginMessage<SyncSingularitiesMessage> {
    private List<Singularity> singularities = new ArrayList<>();

    public SyncSingularitiesMessage() { }

    public List<Singularity> getSingularities() {
        return this.singularities;
    }

    @Override
    public SyncSingularitiesMessage read(FriendlyByteBuf buffer) {
        var message = new SyncSingularitiesMessage();

        message.singularities = SingularityRegistry.getInstance().readFromBuffer(buffer);

        return message;
    }

    @Override
    public void write(SyncSingularitiesMessage message, FriendlyByteBuf buffer) {
        SingularityRegistry.getInstance().writeToBuffer(buffer);
    }

    @Override
    public void onMessage(SyncSingularitiesMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            SingularityRegistry.getInstance().loadSingularities(message);

            NetworkHandler.INSTANCE.reply(new AcknowledgeMessage(), context.get());
        });

        context.get().setPacketHandled(true);
    }
}
