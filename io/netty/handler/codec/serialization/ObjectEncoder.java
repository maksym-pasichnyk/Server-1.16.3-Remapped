/*    */ package io.netty.handler.codec.serialization;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufOutputStream;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Sharable
/*    */ public class ObjectEncoder
/*    */   extends MessageToByteEncoder<Serializable>
/*    */ {
/* 38 */   private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
/* 42 */     int startIdx = out.writerIndex();
/*    */     
/* 44 */     ByteBufOutputStream bout = new ByteBufOutputStream(out);
/* 45 */     ObjectOutputStream oout = null;
/*    */     try {
/* 47 */       bout.write(LENGTH_PLACEHOLDER);
/* 48 */       oout = new CompactObjectOutputStream((OutputStream)bout);
/* 49 */       oout.writeObject(msg);
/* 50 */       oout.flush();
/*    */     } finally {
/* 52 */       if (oout != null) {
/* 53 */         oout.close();
/*    */       } else {
/* 55 */         bout.close();
/*    */       } 
/*    */     } 
/*    */     
/* 59 */     int endIdx = out.writerIndex();
/*    */     
/* 61 */     out.setInt(startIdx, endIdx - startIdx - 4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\serialization\ObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */