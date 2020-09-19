/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.util.Map;
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
/*     */ public final class AsciiHeadersEncoder
/*     */ {
/*     */   private final ByteBuf buf;
/*     */   private final SeparatorType separatorType;
/*     */   private final NewlineType newlineType;
/*     */   
/*     */   public enum SeparatorType
/*     */   {
/*  36 */     COLON,
/*     */ 
/*     */ 
/*     */     
/*  40 */     COLON_SPACE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum NewlineType
/*     */   {
/*  50 */     LF,
/*     */ 
/*     */ 
/*     */     
/*  54 */     CRLF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsciiHeadersEncoder(ByteBuf buf) {
/*  62 */     this(buf, SeparatorType.COLON_SPACE, NewlineType.CRLF);
/*     */   }
/*     */   
/*     */   public AsciiHeadersEncoder(ByteBuf buf, SeparatorType separatorType, NewlineType newlineType) {
/*  66 */     if (buf == null) {
/*  67 */       throw new NullPointerException("buf");
/*     */     }
/*  69 */     if (separatorType == null) {
/*  70 */       throw new NullPointerException("separatorType");
/*     */     }
/*  72 */     if (newlineType == null) {
/*  73 */       throw new NullPointerException("newlineType");
/*     */     }
/*     */     
/*  76 */     this.buf = buf;
/*  77 */     this.separatorType = separatorType;
/*  78 */     this.newlineType = newlineType;
/*     */   }
/*     */   
/*     */   public void encode(Map.Entry<CharSequence, CharSequence> entry) {
/*  82 */     CharSequence name = entry.getKey();
/*  83 */     CharSequence value = entry.getValue();
/*  84 */     ByteBuf buf = this.buf;
/*  85 */     int nameLen = name.length();
/*  86 */     int valueLen = value.length();
/*  87 */     int entryLen = nameLen + valueLen + 4;
/*  88 */     int offset = buf.writerIndex();
/*  89 */     buf.ensureWritable(entryLen);
/*  90 */     writeAscii(buf, offset, name);
/*  91 */     offset += nameLen;
/*     */     
/*  93 */     switch (this.separatorType) {
/*     */       case LF:
/*  95 */         buf.setByte(offset++, 58);
/*     */         break;
/*     */       case CRLF:
/*  98 */         buf.setByte(offset++, 58);
/*  99 */         buf.setByte(offset++, 32);
/*     */         break;
/*     */       default:
/* 102 */         throw new Error();
/*     */     } 
/*     */     
/* 105 */     writeAscii(buf, offset, value);
/* 106 */     offset += valueLen;
/*     */     
/* 108 */     switch (this.newlineType) {
/*     */       case LF:
/* 110 */         buf.setByte(offset++, 10);
/*     */         break;
/*     */       case CRLF:
/* 113 */         buf.setByte(offset++, 13);
/* 114 */         buf.setByte(offset++, 10);
/*     */         break;
/*     */       default:
/* 117 */         throw new Error();
/*     */     } 
/*     */     
/* 120 */     buf.writerIndex(offset);
/*     */   }
/*     */   
/*     */   private static void writeAscii(ByteBuf buf, int offset, CharSequence value) {
/* 124 */     if (value instanceof AsciiString) {
/* 125 */       ByteBufUtil.copy((AsciiString)value, 0, buf, offset, value.length());
/*     */     } else {
/* 127 */       buf.setCharSequence(offset, value, CharsetUtil.US_ASCII);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\AsciiHeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */