/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.memcache.AbstractMemcacheObjectEncoder;
/*    */ import io.netty.handler.codec.memcache.MemcacheMessage;
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
/*    */ 
/*    */ 
/*    */ public abstract class AbstractBinaryMemcacheEncoder<M extends BinaryMemcacheMessage>
/*    */   extends AbstractMemcacheObjectEncoder<M>
/*    */ {
/*    */   private static final int MINIMUM_HEADER_SIZE = 24;
/*    */   
/*    */   protected ByteBuf encodeMessage(ChannelHandlerContext ctx, M msg) {
/* 38 */     ByteBuf buf = ctx.alloc().buffer(24 + msg.extrasLength() + msg
/* 39 */         .keyLength());
/*    */     
/* 41 */     encodeHeader(buf, msg);
/* 42 */     encodeExtras(buf, msg.extras());
/* 43 */     encodeKey(buf, msg.key());
/*    */     
/* 45 */     return buf;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void encodeExtras(ByteBuf buf, ByteBuf extras) {
/* 55 */     if (extras == null || !extras.isReadable()) {
/*    */       return;
/*    */     }
/*    */     
/* 59 */     buf.writeBytes(extras);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void encodeKey(ByteBuf buf, ByteBuf key) {
/* 69 */     if (key == null || !key.isReadable()) {
/*    */       return;
/*    */     }
/*    */     
/* 73 */     buf.writeBytes(key);
/*    */   }
/*    */   
/*    */   protected abstract void encodeHeader(ByteBuf paramByteBuf, M paramM);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\AbstractBinaryMemcacheEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */