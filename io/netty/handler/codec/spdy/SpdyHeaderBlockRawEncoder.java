/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import java.util.Set;
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
/*    */ public class SpdyHeaderBlockRawEncoder
/*    */   extends SpdyHeaderBlockEncoder
/*    */ {
/*    */   private final int version;
/*    */   
/*    */   public SpdyHeaderBlockRawEncoder(SpdyVersion version) {
/* 32 */     if (version == null) {
/* 33 */       throw new NullPointerException("version");
/*    */     }
/* 35 */     this.version = version.getVersion();
/*    */   }
/*    */   
/*    */   private static void setLengthField(ByteBuf buffer, int writerIndex, int length) {
/* 39 */     buffer.setInt(writerIndex, length);
/*    */   }
/*    */   
/*    */   private static void writeLengthField(ByteBuf buffer, int length) {
/* 43 */     buffer.writeInt(length);
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteBuf encode(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception {
/* 48 */     Set<CharSequence> names = frame.headers().names();
/* 49 */     int numHeaders = names.size();
/* 50 */     if (numHeaders == 0) {
/* 51 */       return Unpooled.EMPTY_BUFFER;
/*    */     }
/* 53 */     if (numHeaders > 65535) {
/* 54 */       throw new IllegalArgumentException("header block contains too many headers");
/*    */     }
/*    */     
/* 57 */     ByteBuf headerBlock = alloc.heapBuffer();
/* 58 */     writeLengthField(headerBlock, numHeaders);
/* 59 */     for (CharSequence name : names) {
/* 60 */       writeLengthField(headerBlock, name.length());
/* 61 */       ByteBufUtil.writeAscii(headerBlock, name);
/* 62 */       int savedIndex = headerBlock.writerIndex();
/* 63 */       int valueLength = 0;
/* 64 */       writeLengthField(headerBlock, valueLength);
/* 65 */       for (CharSequence value : frame.headers().getAll(name)) {
/* 66 */         int length = value.length();
/* 67 */         if (length > 0) {
/* 68 */           ByteBufUtil.writeAscii(headerBlock, value);
/* 69 */           headerBlock.writeByte(0);
/* 70 */           valueLength += length + 1;
/*    */         } 
/*    */       } 
/* 73 */       if (valueLength != 0) {
/* 74 */         valueLength--;
/*    */       }
/* 76 */       if (valueLength > 65535) {
/* 77 */         throw new IllegalArgumentException("header exceeds allowable length: " + name);
/*    */       }
/*    */       
/* 80 */       if (valueLength > 0) {
/* 81 */         setLengthField(headerBlock, savedIndex, valueLength);
/* 82 */         headerBlock.writerIndex(headerBlock.writerIndex() - 1);
/*    */       } 
/*    */     } 
/* 85 */     return headerBlock;
/*    */   }
/*    */   
/*    */   void end() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockRawEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */