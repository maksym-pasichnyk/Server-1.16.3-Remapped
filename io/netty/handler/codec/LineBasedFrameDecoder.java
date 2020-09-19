/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.ByteProcessor;
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
/*     */ public class LineBasedFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final int maxLength;
/*     */   private final boolean failFast;
/*     */   private final boolean stripDelimiter;
/*     */   private boolean discarding;
/*     */   private int discardedBytes;
/*     */   private int offset;
/*     */   
/*     */   public LineBasedFrameDecoder(int maxLength) {
/*  52 */     this(maxLength, true, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineBasedFrameDecoder(int maxLength, boolean stripDelimiter, boolean failFast) {
/*  71 */     this.maxLength = maxLength;
/*  72 */     this.failFast = failFast;
/*  73 */     this.stripDelimiter = stripDelimiter;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  78 */     Object decoded = decode(ctx, in);
/*  79 */     if (decoded != null) {
/*  80 */       out.add(decoded);
/*     */     }
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
/*     */   protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
/*  93 */     int eol = findEndOfLine(buffer);
/*  94 */     if (!this.discarding) {
/*  95 */       if (eol >= 0) {
/*     */         ByteBuf frame;
/*  97 */         int i = eol - buffer.readerIndex();
/*  98 */         int delimLength = (buffer.getByte(eol) == 13) ? 2 : 1;
/*     */         
/* 100 */         if (i > this.maxLength) {
/* 101 */           buffer.readerIndex(eol + delimLength);
/* 102 */           fail(ctx, i);
/* 103 */           return null;
/*     */         } 
/*     */         
/* 106 */         if (this.stripDelimiter) {
/* 107 */           frame = buffer.readRetainedSlice(i);
/* 108 */           buffer.skipBytes(delimLength);
/*     */         } else {
/* 110 */           frame = buffer.readRetainedSlice(i + delimLength);
/*     */         } 
/*     */         
/* 113 */         return frame;
/*     */       } 
/* 115 */       int length = buffer.readableBytes();
/* 116 */       if (length > this.maxLength) {
/* 117 */         this.discardedBytes = length;
/* 118 */         buffer.readerIndex(buffer.writerIndex());
/* 119 */         this.discarding = true;
/* 120 */         this.offset = 0;
/* 121 */         if (this.failFast) {
/* 122 */           fail(ctx, "over " + this.discardedBytes);
/*     */         }
/*     */       } 
/* 125 */       return null;
/*     */     } 
/*     */     
/* 128 */     if (eol >= 0) {
/* 129 */       int length = this.discardedBytes + eol - buffer.readerIndex();
/* 130 */       int delimLength = (buffer.getByte(eol) == 13) ? 2 : 1;
/* 131 */       buffer.readerIndex(eol + delimLength);
/* 132 */       this.discardedBytes = 0;
/* 133 */       this.discarding = false;
/* 134 */       if (!this.failFast) {
/* 135 */         fail(ctx, length);
/*     */       }
/*     */     } else {
/* 138 */       this.discardedBytes += buffer.readableBytes();
/* 139 */       buffer.readerIndex(buffer.writerIndex());
/*     */     } 
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void fail(ChannelHandlerContext ctx, int length) {
/* 146 */     fail(ctx, String.valueOf(length));
/*     */   }
/*     */   
/*     */   private void fail(ChannelHandlerContext ctx, String length) {
/* 150 */     ctx.fireExceptionCaught(new TooLongFrameException("frame length (" + length + ") exceeds the allowed maximum (" + this.maxLength + ')'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int findEndOfLine(ByteBuf buffer) {
/* 160 */     int totalLength = buffer.readableBytes();
/* 161 */     int i = buffer.forEachByte(buffer.readerIndex() + this.offset, totalLength - this.offset, ByteProcessor.FIND_LF);
/* 162 */     if (i >= 0) {
/* 163 */       this.offset = 0;
/* 164 */       if (i > 0 && buffer.getByte(i - 1) == 13) {
/* 165 */         i--;
/*     */       }
/*     */     } else {
/* 168 */       this.offset = totalLength;
/*     */     } 
/* 170 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\LineBasedFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */