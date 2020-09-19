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
/*    */ public class BinaryMemcacheResponseDecoder
/*    */   extends AbstractBinaryMemcacheDecoder<BinaryMemcacheResponse>
/*    */ {
/*    */   public BinaryMemcacheResponseDecoder() {
/* 30 */     this(8192);
/*    */   }
/*    */   
/*    */   public BinaryMemcacheResponseDecoder(int chunkSize) {
/* 34 */     super(chunkSize);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BinaryMemcacheResponse decodeHeader(ByteBuf in) {
/* 39 */     DefaultBinaryMemcacheResponse header = new DefaultBinaryMemcacheResponse();
/* 40 */     header.setMagic(in.readByte());
/* 41 */     header.setOpcode(in.readByte());
/* 42 */     header.setKeyLength(in.readShort());
/* 43 */     header.setExtrasLength(in.readByte());
/* 44 */     header.setDataType(in.readByte());
/* 45 */     header.setStatus(in.readShort());
/* 46 */     header.setTotalBodyLength(in.readInt());
/* 47 */     header.setOpaque(in.readInt());
/* 48 */     header.setCas(in.readLong());
/* 49 */     return header;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BinaryMemcacheResponse buildInvalidMessage() {
/* 54 */     return new DefaultBinaryMemcacheResponse(Unpooled.EMPTY_BUFFER, Unpooled.EMPTY_BUFFER);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */