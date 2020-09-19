/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public class BinaryMemcacheRequestEncoder
/*    */   extends AbstractBinaryMemcacheEncoder<BinaryMemcacheRequest>
/*    */ {
/*    */   protected void encodeHeader(ByteBuf buf, BinaryMemcacheRequest msg) {
/* 30 */     buf.writeByte(msg.magic());
/* 31 */     buf.writeByte(msg.opcode());
/* 32 */     buf.writeShort(msg.keyLength());
/* 33 */     buf.writeByte(msg.extrasLength());
/* 34 */     buf.writeByte(msg.dataType());
/* 35 */     buf.writeShort(msg.reserved());
/* 36 */     buf.writeInt(msg.totalBodyLength());
/* 37 */     buf.writeInt(msg.opaque());
/* 38 */     buf.writeLong(msg.cas());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */