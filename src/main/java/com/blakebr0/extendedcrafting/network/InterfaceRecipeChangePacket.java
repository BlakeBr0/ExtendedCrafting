//package com.blakebr0.extendedcrafting.network;
//
//import com.blakebr0.extendedcrafting.tileentity.TileAutomationInterface;
//
//import io.netty.buffer.ByteBuf;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.fml.common.FMLCommonHandler;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//
//public class InterfaceRecipeChangePacket implements IMessage {
//
//	private long pos;
//	private int mode;
//
//	public InterfaceRecipeChangePacket() {
//
//	}
//
//	public InterfaceRecipeChangePacket(long pos, int mode) {
//		this.pos = pos;
//		this.mode = mode;
//	}
//
//	@Override
//	public void fromBytes(ByteBuf buf) {
//		this.pos = buf.readLong();
//		this.mode = buf.readInt();
//	}
//
//	@Override
//	public void toBytes(ByteBuf buf) {
//		buf.writeLong(this.pos);
//		buf.writeInt(this.mode);
//	}
//
//	public static class Handler implements IMessageHandler<InterfaceRecipeChangePacket, IMessage> {
//
//		@Override
//		public IMessage onMessage(InterfaceRecipeChangePacket message, MessageContext ctx) {
//			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
//			return null;
//		}
//
//		private void handle(InterfaceRecipeChangePacket message, MessageContext ctx) {
//			TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(BlockPos.fromLong(message.pos));
//			if (tile instanceof TileAutomationInterface) {
//				TileAutomationInterface machine = (TileAutomationInterface) tile;
//				if (message.mode == 0 && machine.hasTable()) {
//					machine.saveRecipe();
//				} else if (message.mode == 1) {
//					machine.clearRecipe();
//				}
//			}
//		}
//	}
//}
