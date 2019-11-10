package com.blakebr0.extendedcrafting.network;

import com.blakebr0.extendedcrafting.tileentity.CompressorTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InputLimitSwitchPacket implements IMessage {

	private long pos;

	public InputLimitSwitchPacket() {
		
	}

	public InputLimitSwitchPacket(long pos) {
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

	public static class Handler implements IMessageHandler<InputLimitSwitchPacket, IMessage> {

		@Override
		public IMessage onMessage(InputLimitSwitchPacket message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(InputLimitSwitchPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(BlockPos.fromLong(message.pos));
			if (tile instanceof CompressorTileEntity) {
				CompressorTileEntity compressor = (CompressorTileEntity) tile;
				compressor.toggleInputLimit();
			}
		}
	}
}
