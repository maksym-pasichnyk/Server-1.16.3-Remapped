/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
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
/*    */ public class BinaryMemcacheRequestDecoder
/*    */   extends AbstractBinaryMemcacheDecoder<BinaryMemcacheRequest>
/*    */ {
/*    */   public BinaryMemcacheRequestDecoder() {
/* 30 */     this(8192);
/*    */   }
/*    */   
/*    */   public BinaryMemcacheRequestDecoder(int chunkSize) {
/* 34 */     super(chunkSize);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BinaryMemcacheRequest decodeHeader(ByteBuf in) {
/* 39 */     DefaultBinaryMemcacheRequest header = new DefaultBinaryMemcacheRequest();
/* 40 */     header.setMagic(in.readByte());
/* 41 */     header.setOpcode(in.readByte());
/* 42 */     header.setKeyLength(in.readShort());
/* 43 */     header.setExtrasLength(in.readByte());
/* 44 */     header.setDataType(in.readByte());
/* 45 */     header.setReserved(in.readShort());
/* 46 */     header.setTotalBodyLength(in.readInt());
/* 47 */     header.setOpaque(in.readInt());
/* 48 */     header.setCas(in.readLong());
/* 49 */     return header;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BinaryMemcacheRequest buildInvalidMessage() {
/* 54 */     return new DefaultBinaryMemcacheRequest(Unpooled.EMPTY_BUFFER, Unpooled.EMPTY_BUFFER);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */