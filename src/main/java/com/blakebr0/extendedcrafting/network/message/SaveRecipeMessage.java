package com.blakebr0.extendedcrafting.network.message;

import com.blakebr0.cucumber.network.message.Message;
import com.blakebr0.extendedcrafting.tileentity.AutoEnderCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoFluxCrafterTileEntity;
import com.blakebr0.extendedcrafting.tileentity.AutoTableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SaveRecipeMessage extends Message<SaveRecipeMessage> {
    private final BlockPos pos;
    private final int selected;

    public SaveRecipeMessage() {
        this.pos = BlockPos.ZERO;
        this.selected = -1;
    }

    public SaveRecipeMessage(BlockPos pos, int selected) {
        this.pos = pos;
        this.selected = selected;
    }

    @Override
    public SaveRecipeMessage read(FriendlyByteBuf buffer) {
        return new SaveRecipeMessage(buffer.readBlockPos(), buffer.readVarInt());
    }

    @Override
    public void write(SaveRecipeMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeVarInt(message.selected);
    }

    @Override
    public void onMessage(SaveRecipeMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();

            if (player != null) {
                var level = player.getCommandSenderWorld();
                var tile = level.getBlockEntity(message.pos);

                if (tile instanceof AutoTableTileEntity table) {
                    if (!table.getRecipeStorage().hasRecipe(message.selected)) {
                        table.saveRecipe(message.selected);
                    } else {
                        table.deleteRecipe(message.selected);
                    }
                }

                if (tile instanceof AutoEnderCrafterTileEntity crafter) {
                    if (!crafter.getRecipeStorage().hasRecipe(message.selected)) {
                        crafter.saveRecipe(message.selected);
                    } else {
                        crafter.deleteRecipe(message.selected);
                    }
                }

                if (tile instanceof AutoFluxCrafterTileEntity crafter) {
                    if (!crafter.getRecipeStorage().hasRecipe(message.selected)) {
                        crafter.saveRecipe(message.selected);
                    } else {
                        crafter.deleteRecipe(message.selected);
                    }
                }
            }
        });

        context.get().setPacketHandled(true);
    }
}
