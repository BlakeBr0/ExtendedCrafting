package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.extendedcrafting.tileentity.AutoEnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoFluxCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SelectRecipeMessage extends Message<SelectRecipeMessage> {
    private final BlockPos pos;
    private final int selected;

    public SelectRecipeMessage() {
        this.pos = BlockPos.ZERO;
        this.selected = -1;
    }

    public SelectRecipeMessage(BlockPos pos, int selected) {
        this.pos = pos;
        this.selected = selected;
    }

    @Override
    public SelectRecipeMessage read(FriendlyByteBuf buffer) {
        return new SelectRecipeMessage(buffer.readBlockPos(), buffer.readVarInt());
    }

    @Override
    public void write(SelectRecipeMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeVarInt(message.selected);
    }

    @Override
    public void onMessage(SelectRecipeMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();

            if (player != null) {
                var level = player.getCommandSenderWorld();
                var tile = level.getBlockEntity(message.pos);

                if (tile instanceof AutoTableTileEntity table)
                    table.selectRecipe(message.selected);

                if (tile instanceof AutoEnderCrafterTileEntity crafter)
                    crafter.selectRecipe(message.selected);

                if (tile instanceof AutoFluxCrafterTileEntity crafter)
                    crafter.selectRecipe(message.selected);
            }
        });

        context.get().setPacketHandled(true);
    }
}
