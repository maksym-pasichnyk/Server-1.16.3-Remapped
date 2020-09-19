/*     */ package io.netty.handler.codec.json;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
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
/*     */ public class JsonObjectDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static final int ST_CORRUPTED = -1;
/*     */   private static final int ST_INIT = 0;
/*     */   private static final int ST_DECODING_NORMAL = 1;
/*     */   private static final int ST_DECODING_ARRAY_STREAM = 2;
/*     */   private int openBraces;
/*     */   private int idx;
/*     */   private int lastReaderIndex;
/*     */   private int state;
/*     */   private boolean insideString;
/*     */   private final int maxObjectLength;
/*     */   private final boolean streamArrayElements;
/*     */   
/*     */   public JsonObjectDecoder() {
/*  58 */     this(1048576);
/*     */   }
/*     */   
/*     */   public JsonObjectDecoder(int maxObjectLength) {
/*  62 */     this(maxObjectLength, false);
/*     */   }
/*     */   
/*     */   public JsonObjectDecoder(boolean streamArrayElements) {
/*  66 */     this(1048576, streamArrayElements);
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
/*     */   public JsonObjectDecoder(int maxObjectLength, boolean streamArrayElements) {
/*  79 */     if (maxObjectLength < 1) {
/*  80 */       throw new IllegalArgumentException("maxObjectLength must be a positive int");
/*     */     }
/*  82 */     this.maxObjectLength = maxObjectLength;
/*  83 */     this.streamArrayElements = streamArrayElements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  88 */     if (this.state == -1) {
/*  89 */       in.skipBytes(in.readableBytes());
/*     */       
/*     */       return;
/*     */     } 
/*  93 */     if (this.idx > in.readerIndex() && this.lastReaderIndex != in.readerIndex()) {
/*  94 */       this.idx = in.readerIndex() + this.idx - this.lastReaderIndex;
/*     */     }
/*     */ 
/*     */     
/*  98 */     int idx = this.idx;
/*  99 */     int wrtIdx = in.writerIndex();
/*     */     
/* 101 */     if (wrtIdx > this.maxObjectLength) {
/*     */       
/* 103 */       in.skipBytes(in.readableBytes());
/* 104 */       reset();
/* 105 */       throw new TooLongFrameException("object length exceeds " + this.maxObjectLength + ": " + wrtIdx + " bytes discarded");
/*     */     } 
/*     */ 
/*     */     
/* 109 */     for (; idx < wrtIdx; idx++) {
/* 110 */       byte c = in.getByte(idx);
/* 111 */       if (this.state == 1) {
/* 112 */         decodeByte(c, in, idx);
/*     */ 
/*     */ 
/*     */         
/* 116 */         if (this.openBraces == 0) {
/* 117 */           ByteBuf json = extractObject(ctx, in, in.readerIndex(), idx + 1 - in.readerIndex());
/* 118 */           if (json != null) {
/* 119 */             out.add(json);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 124 */           in.readerIndex(idx + 1);
/*     */ 
/*     */           
/* 127 */           reset();
/*     */         } 
/* 129 */       } else if (this.state == 2) {
/* 130 */         decodeByte(c, in, idx);
/*     */         
/* 132 */         if (!this.insideString && ((this.openBraces == 1 && c == 44) || (this.openBraces == 0 && c == 93)))
/*     */         {
/*     */           
/* 135 */           for (int i = in.readerIndex(); Character.isWhitespace(in.getByte(i)); i++) {
/* 136 */             in.skipBytes(1);
/*     */           }
/*     */ 
/*     */           
/* 140 */           int idxNoSpaces = idx - 1;
/* 141 */           while (idxNoSpaces >= in.readerIndex() && Character.isWhitespace(in.getByte(idxNoSpaces))) {
/* 142 */             idxNoSpaces--;
/*     */           }
/*     */           
/* 145 */           ByteBuf json = extractObject(ctx, in, in.readerIndex(), idxNoSpaces + 1 - in.readerIndex());
/* 146 */           if (json != null) {
/* 147 */             out.add(json);
/*     */           }
/*     */           
/* 150 */           in.readerIndex(idx + 1);
/*     */           
/* 152 */           if (c == 93) {
/* 153 */             reset();
/*     */           }
/*     */         }
/*     */       
/* 157 */       } else if (c == 123 || c == 91) {
/* 158 */         initDecoding(c);
/*     */         
/* 160 */         if (this.state == 2)
/*     */         {
/* 162 */           in.skipBytes(1);
/*     */         }
/*     */       }
/* 165 */       else if (Character.isWhitespace(c)) {
/* 166 */         in.skipBytes(1);
/*     */       } else {
/* 168 */         this.state = -1;
/* 169 */         throw new CorruptedFrameException("invalid JSON received at byte position " + idx + ": " + 
/* 170 */             ByteBufUtil.hexDump(in));
/*     */       } 
/*     */     } 
/*     */     
/* 174 */     if (in.readableBytes() == 0) {
/* 175 */       this.idx = 0;
/*     */     } else {
/* 177 */       this.idx = idx;
/*     */     } 
/* 179 */     this.lastReaderIndex = in.readerIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteBuf extractObject(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
/* 187 */     return buffer.retainedSlice(index, length);
/*     */   }
/*     */   
/*     */   private void decodeByte(byte c, ByteBuf in, int idx) {
/* 191 */     if ((c == 123 || c == 91) && !this.insideString) {
/* 192 */       this.openBraces++;
/* 193 */     } else if ((c == 125 || c == 93) && !this.insideString) {
/* 194 */       this.openBraces--;
/* 195 */     } else if (c == 34) {
/*     */ 
/*     */       
/* 198 */       if (!this.insideString) {
/* 199 */         this.insideString = true;
/*     */       } else {
/* 201 */         int backslashCount = 0;
/* 202 */         idx--;
/* 203 */         while (idx >= 0 && 
/* 204 */           in.getByte(idx) == 92) {
/* 205 */           backslashCount++;
/* 206 */           idx--;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 212 */         if (backslashCount % 2 == 0)
/*     */         {
/* 214 */           this.insideString = false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initDecoding(byte openingBrace) {
/* 221 */     this.openBraces = 1;
/* 222 */     if (openingBrace == 91 && this.streamArrayElements) {
/* 223 */       this.state = 2;
/*     */     } else {
/* 225 */       this.state = 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reset() {
/* 230 */     this.insideString = false;
/* 231 */     this.state = 0;
/* 232 */     this.openBraces = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\json\JsonObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */