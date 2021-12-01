package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncSingularitiesMessage extends Message<SyncSingularitiesMessage> {
    private List<Singularity> singularities;

    public SyncSingularitiesMessage() { }

    public SyncSingularitiesMessage(List<Singularity> singularities) {
        this.singularities = singularities;
    }

    public List<Singularity> getSingularities() {
        return this.singularities;
    }

    @Override
    public SyncSingularitiesMessage read(FriendlyByteBuf buffer) {
        var singularities = SingularityRegistry.getInstance().readFromBuffer(buffer);

        return new SyncSingularitiesMessage(singularities);
    }

    @Override
    public void write(SyncSingularitiesMessage message, FriendlyByteBuf buffer) {
        SingularityRegistry.getInstance().writeToBuffer(buffer);
    }

    @Override
    public void onMessage(SyncSingularitiesMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            SingularityRegistry.getInstance().loadSingularities(message);
        });

        context.get().setPacketHandled(true);
    }
}
