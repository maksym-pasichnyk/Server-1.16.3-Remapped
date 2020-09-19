/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ 
/*    */ 
/*    */ @Sharable
/*    */ public class Varint21LengthFieldPrepender
/*    */   extends MessageToByteEncoder<ByteBuf>
/*    */ {
/*    */   protected void encode(ChannelHandlerContext debug1, ByteBuf debug2, ByteBuf debug3) throws Exception {
/* 14 */     int debug4 = debug2.readableBytes();
/* 15 */     int debug5 = FriendlyByteBuf.getVarIntSize(debug4);
/*    */     
/* 17 */     if (debug5 > 3) {
/* 18 */       throw new IllegalArgumentException("unable to fit " + debug4 + " into " + '\003');
/*    */     }
/*    */     
/* 21 */     FriendlyByteBuf debug6 = new FriendlyByteBuf(debug3);
/*    */     
/* 23 */     debug6.ensureWritable(debug5 + debug4);
/*    */     
/* 25 */     debug6.writeVarInt(debug4);
/* 26 */     debug6.writeBytes(debug2, debug2.readerIndex(), debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\Varint21LengthFieldPrepender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */