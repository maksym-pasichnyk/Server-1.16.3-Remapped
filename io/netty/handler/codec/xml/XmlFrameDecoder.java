/*     */ package io.netty.handler.codec.xml;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final int maxFrameLength;
/*     */   
/*     */   public XmlFrameDecoder(int maxFrameLength) {
/*  75 */     if (maxFrameLength < 1) {
/*  76 */       throw new IllegalArgumentException("maxFrameLength must be a positive int");
/*     */     }
/*  78 */     this.maxFrameLength = maxFrameLength;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  83 */     boolean openingBracketFound = false;
/*  84 */     boolean atLeastOneXmlElementFound = false;
/*  85 */     boolean inCDATASection = false;
/*  86 */     long openBracketsCount = 0L;
/*  87 */     int length = 0;
/*  88 */     int leadingWhiteSpaceCount = 0;
/*  89 */     int bufferLength = in.writerIndex();
/*     */     
/*  91 */     if (bufferLength > this.maxFrameLength) {
/*     */       
/*  93 */       in.skipBytes(in.readableBytes());
/*  94 */       fail(bufferLength);
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     for (int i = in.readerIndex(); i < bufferLength; i++) {
/*  99 */       byte readByte = in.getByte(i);
/* 100 */       if (!openingBracketFound && Character.isWhitespace(readByte))
/*     */       
/* 102 */       { leadingWhiteSpaceCount++; }
/* 103 */       else { if (!openingBracketFound && readByte != 60) {
/*     */           
/* 105 */           fail(ctx);
/* 106 */           in.skipBytes(in.readableBytes()); return;
/*     */         } 
/* 108 */         if (!inCDATASection && readByte == 60) {
/* 109 */           openingBracketFound = true;
/*     */           
/* 111 */           if (i < bufferLength - 1) {
/* 112 */             byte peekAheadByte = in.getByte(i + 1);
/* 113 */             if (peekAheadByte == 47) {
/*     */               
/* 115 */               int peekFurtherAheadIndex = i + 2;
/* 116 */               while (peekFurtherAheadIndex <= bufferLength - 1) {
/*     */                 
/* 118 */                 if (in.getByte(peekFurtherAheadIndex) == 62) {
/* 119 */                   openBracketsCount--;
/*     */                   break;
/*     */                 } 
/* 122 */                 peekFurtherAheadIndex++;
/*     */               } 
/* 124 */             } else if (isValidStartCharForXmlElement(peekAheadByte)) {
/* 125 */               atLeastOneXmlElementFound = true;
/*     */ 
/*     */               
/* 128 */               openBracketsCount++;
/* 129 */             } else if (peekAheadByte == 33) {
/* 130 */               if (isCommentBlockStart(in, i)) {
/*     */                 
/* 132 */                 openBracketsCount++;
/* 133 */               } else if (isCDATABlockStart(in, i)) {
/*     */                 
/* 135 */                 openBracketsCount++;
/* 136 */                 inCDATASection = true;
/*     */               } 
/* 138 */             } else if (peekAheadByte == 63) {
/*     */               
/* 140 */               openBracketsCount++;
/*     */             } 
/*     */           } 
/* 143 */         } else if (!inCDATASection && readByte == 47) {
/* 144 */           if (i < bufferLength - 1 && in.getByte(i + 1) == 62)
/*     */           {
/* 146 */             openBracketsCount--;
/*     */           }
/* 148 */         } else if (readByte == 62) {
/* 149 */           length = i + 1;
/*     */           
/* 151 */           if (i - 1 > -1) {
/* 152 */             byte peekBehindByte = in.getByte(i - 1);
/*     */             
/* 154 */             if (!inCDATASection) {
/* 155 */               if (peekBehindByte == 63) {
/*     */                 
/* 157 */                 openBracketsCount--;
/* 158 */               } else if (peekBehindByte == 45 && i - 2 > -1 && in.getByte(i - 2) == 45) {
/*     */                 
/* 160 */                 openBracketsCount--;
/*     */               } 
/* 162 */             } else if (peekBehindByte == 93 && i - 2 > -1 && in.getByte(i - 2) == 93) {
/*     */               
/* 164 */               openBracketsCount--;
/* 165 */               inCDATASection = false;
/*     */             } 
/*     */           } 
/*     */           
/* 169 */           if (atLeastOneXmlElementFound && openBracketsCount == 0L) {
/*     */             break;
/*     */           }
/*     */         }  }
/*     */     
/*     */     } 
/*     */     
/* 176 */     int readerIndex = in.readerIndex();
/* 177 */     int xmlElementLength = length - readerIndex;
/*     */     
/* 179 */     if (openBracketsCount == 0L && xmlElementLength > 0) {
/* 180 */       if (readerIndex + xmlElementLength >= bufferLength) {
/* 181 */         xmlElementLength = in.readableBytes();
/*     */       }
/*     */       
/* 184 */       ByteBuf frame = extractFrame(in, readerIndex + leadingWhiteSpaceCount, xmlElementLength - leadingWhiteSpaceCount);
/* 185 */       in.skipBytes(xmlElementLength);
/* 186 */       out.add(frame);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fail(long frameLength) {
/* 191 */     if (frameLength > 0L) {
/* 192 */       throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
/*     */     }
/*     */     
/* 195 */     throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + " - discarding");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void fail(ChannelHandlerContext ctx) {
/* 201 */     ctx.fireExceptionCaught((Throwable)new CorruptedFrameException("frame contains content before the xml starts"));
/*     */   }
/*     */   
/*     */   private static ByteBuf extractFrame(ByteBuf buffer, int index, int length) {
/* 205 */     return buffer.copy(index, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidStartCharForXmlElement(byte b) {
/* 220 */     return ((b >= 97 && b <= 122) || (b >= 65 && b <= 90) || b == 58 || b == 95);
/*     */   }
/*     */   
/*     */   private static boolean isCommentBlockStart(ByteBuf in, int i) {
/* 224 */     return (i < in.writerIndex() - 3 && in
/* 225 */       .getByte(i + 2) == 45 && in
/* 226 */       .getByte(i + 3) == 45);
/*     */   }
/*     */   
/*     */   private static boolean isCDATABlockStart(ByteBuf in, int i) {
/* 230 */     return (i < in.writerIndex() - 8 && in
/* 231 */       .getByte(i + 2) == 91 && in
/* 232 */       .getByte(i + 3) == 67 && in
/* 233 */       .getByte(i + 4) == 68 && in
/* 234 */       .getByte(i + 5) == 65 && in
/* 235 */       .getByte(i + 6) == 84 && in
/* 236 */       .getByte(i + 7) == 65 && in
/* 237 */       .getByte(i + 8) == 91);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */