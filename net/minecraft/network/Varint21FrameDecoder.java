/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Varint21FrameDecoder
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   protected void decode(ChannelHandlerContext debug1, ByteBuf debug2, List<Object> debug3) throws Exception {
/* 14 */     debug2.markReaderIndex();
/*    */     
/* 16 */     byte[] debug4 = new byte[3];
/* 17 */     for (int debug5 = 0; debug5 < debug4.length; debug5++) {
/* 18 */       if (!debug2.isReadable()) {
/* 19 */         debug2.resetReaderIndex();
/*    */         
/*    */         return;
/*    */       } 
/* 23 */       debug4[debug5] = debug2.readByte();
/* 24 */       if (debug4[debug5] >= 0) {
/* 25 */         FriendlyByteBuf debug6 = new FriendlyByteBuf(Unpooled.wrappedBuffer(debug4));
/*    */         try {
/* 27 */           int debug7 = debug6.readVarInt();
/*    */           
/* 29 */           if (debug2.readableBytes() < debug7) {
/* 30 */             debug2.resetReaderIndex();
/*    */             return;
/*    */           } 
/* 33 */           debug3.add(debug2.readBytes(debug7));
/*    */           
/*    */           return;
/*    */         } finally {
/* 37 */           debug6.release();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 42 */     throw new CorruptedFrameException("length wider than 21-bit");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\Varint21FrameDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */