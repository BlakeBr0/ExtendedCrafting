package com.blakebr0.extendedcrafting.util;

import com.blakebr0.extendedcrafting.tile.TileCompressor;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EjectModeSwitchPacket implements IMessage {

	private long pos;

	public EjectModeSwitchPacket() {
	}

	public EjectModeSwitchPacket(long pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos);
	}

	public static class Handler implements IMessageHandler<EjectModeSwitchPacket, IMessage> {

		@Override
		public IMessage onMessage(EjectModeSwitchPacket message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(EjectModeSwitchPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(BlockPos.fromLong(message.pos));
			if (tile instanceof TileCompressor) {
				TileCompressor compressor = (TileCompressor) tile;
				compressor.toggleEjecting();
			}
		}
	}
}
