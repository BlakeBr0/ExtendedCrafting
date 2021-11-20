package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.extendedcrafting.singularity.Singularity;
import com.blakebr0.extendedcrafting.singularity.SingularityRegistry;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncSingularitiesMessage {
    private final List<Singularity> singularities;

    public SyncSingularitiesMessage(List<Singularity> singularities) {
        this.singularities = singularities;
    }

    public List<Singularity> getSingularities() {
        return this.singularities;
    }

    public static SyncSingularitiesMessage read(PacketBuffer buffer) {
        List<Singularity> singularities = SingularityRegistry.getInstance().readFromBuffer(buffer);

        return new SyncSingularitiesMessage(singularities);
    }

    public static void write(SyncSingularitiesMessage message, PacketBuffer buffer) {
        SingularityRegistry.getInstance().writeToBuffer(buffer);
    }

    public static void onMessage(SyncSingularitiesMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            SingularityRegistry.getInstance().loadSingularities(message);
        });

        context.get().setPacketHandled(true);
    }
}
