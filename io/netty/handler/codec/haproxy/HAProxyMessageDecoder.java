/*     */ package io.netty.handler.codec.haproxy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.ProtocolDetectionResult;
/*     */ import io.netty.util.CharsetUtil;
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
/*     */ public class HAProxyMessageDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static final int V1_MAX_LENGTH = 108;
/*     */   private static final int V2_MAX_LENGTH = 65551;
/*     */   private static final int V2_MIN_LENGTH = 232;
/*     */   private static final int V2_MAX_TLV = 65319;
/*     */   private static final int DELIMITER_LENGTH = 2;
/*  61 */   private static final byte[] BINARY_PREFIX = new byte[] { 13, 10, 13, 10, 0, 13, 10, 81, 85, 73, 84, 10 };
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
/*  76 */   private static final byte[] TEXT_PREFIX = new byte[] { 80, 82, 79, 88, 89 };
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
/*  87 */   private static final int BINARY_PREFIX_LENGTH = BINARY_PREFIX.length;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private static final ProtocolDetectionResult<HAProxyProtocolVersion> DETECTION_RESULT_V1 = ProtocolDetectionResult.detected(HAProxyProtocolVersion.V1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   private static final ProtocolDetectionResult<HAProxyProtocolVersion> DETECTION_RESULT_V2 = ProtocolDetectionResult.detected(HAProxyProtocolVersion.V2);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean discarding;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int discardedBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean finished;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   private int version = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int v2MaxHeaderSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HAProxyMessageDecoder() {
/* 131 */     this.v2MaxHeaderSize = 65551;
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
/*     */   public HAProxyMessageDecoder(int maxTlvSize) {
/* 145 */     if (maxTlvSize < 1) {
/* 146 */       this.v2MaxHeaderSize = 232;
/* 147 */     } else if (maxTlvSize > 65319) {
/* 148 */       this.v2MaxHeaderSize = 65551;
/*     */     } else {
/* 150 */       int calcMax = maxTlvSize + 232;
/* 151 */       if (calcMax > 65551) {
/* 152 */         this.v2MaxHeaderSize = 65551;
/*     */       } else {
/* 154 */         this.v2MaxHeaderSize = calcMax;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int findVersion(ByteBuf buffer) {
/* 164 */     int n = buffer.readableBytes();
/*     */     
/* 166 */     if (n < 13) {
/* 167 */       return -1;
/*     */     }
/*     */     
/* 170 */     int idx = buffer.readerIndex();
/* 171 */     return match(BINARY_PREFIX, buffer, idx) ? buffer.getByte(idx + BINARY_PREFIX_LENGTH) : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int findEndOfHeader(ByteBuf buffer) {
/* 179 */     int n = buffer.readableBytes();
/*     */ 
/*     */     
/* 182 */     if (n < 16) {
/* 183 */       return -1;
/*     */     }
/*     */     
/* 186 */     int offset = buffer.readerIndex() + 14;
/*     */ 
/*     */     
/* 189 */     int totalHeaderBytes = 16 + buffer.getUnsignedShort(offset);
/*     */ 
/*     */     
/* 192 */     if (n >= totalHeaderBytes) {
/* 193 */       return totalHeaderBytes;
/*     */     }
/* 195 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int findEndOfLine(ByteBuf buffer) {
/* 204 */     int n = buffer.writerIndex();
/* 205 */     for (int i = buffer.readerIndex(); i < n; i++) {
/* 206 */       byte b = buffer.getByte(i);
/* 207 */       if (b == 13 && i < n - 1 && buffer.getByte(i + 1) == 10) {
/* 208 */         return i;
/*     */       }
/*     */     } 
/* 211 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleDecode() {
/* 218 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 223 */     super.channelRead(ctx, msg);
/* 224 */     if (this.finished) {
/* 225 */       ctx.pipeline().remove((ChannelHandler)this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*     */     ByteBuf decoded;
/* 232 */     if (this.version == -1 && (
/* 233 */       this.version = findVersion(in)) == -1) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     if (this.version == 1) {
/* 241 */       decoded = decodeLine(ctx, in);
/*     */     } else {
/* 243 */       decoded = decodeStruct(ctx, in);
/*     */     } 
/*     */     
/* 246 */     if (decoded != null) {
/* 247 */       this.finished = true;
/*     */       try {
/* 249 */         if (this.version == 1) {
/* 250 */           out.add(HAProxyMessage.decodeHeader(decoded.toString(CharsetUtil.US_ASCII)));
/*     */         } else {
/* 252 */           out.add(HAProxyMessage.decodeHeader(decoded));
/*     */         } 
/* 254 */       } catch (HAProxyProtocolException e) {
/* 255 */         fail(ctx, null, (Exception)e);
/*     */       } 
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
/*     */   
/*     */   private ByteBuf decodeStruct(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
/* 270 */     int eoh = findEndOfHeader(buffer);
/* 271 */     if (!this.discarding) {
/* 272 */       if (eoh >= 0) {
/* 273 */         int i = eoh - buffer.readerIndex();
/* 274 */         if (i > this.v2MaxHeaderSize) {
/* 275 */           buffer.readerIndex(eoh);
/* 276 */           failOverLimit(ctx, i);
/* 277 */           return null;
/*     */         } 
/* 279 */         return buffer.readSlice(i);
/*     */       } 
/* 281 */       int length = buffer.readableBytes();
/* 282 */       if (length > this.v2MaxHeaderSize) {
/* 283 */         this.discardedBytes = length;
/* 284 */         buffer.skipBytes(length);
/* 285 */         this.discarding = true;
/* 286 */         failOverLimit(ctx, "over " + this.discardedBytes);
/*     */       } 
/* 288 */       return null;
/*     */     } 
/*     */     
/* 291 */     if (eoh >= 0) {
/* 292 */       buffer.readerIndex(eoh);
/* 293 */       this.discardedBytes = 0;
/* 294 */       this.discarding = false;
/*     */     } else {
/* 296 */       this.discardedBytes = buffer.readableBytes();
/* 297 */       buffer.skipBytes(this.discardedBytes);
/*     */     } 
/* 299 */     return null;
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
/*     */   private ByteBuf decodeLine(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
/* 313 */     int eol = findEndOfLine(buffer);
/* 314 */     if (!this.discarding) {
/* 315 */       if (eol >= 0) {
/* 316 */         int i = eol - buffer.readerIndex();
/* 317 */         if (i > 108) {
/* 318 */           buffer.readerIndex(eol + 2);
/* 319 */           failOverLimit(ctx, i);
/* 320 */           return null;
/*     */         } 
/* 322 */         ByteBuf frame = buffer.readSlice(i);
/* 323 */         buffer.skipBytes(2);
/* 324 */         return frame;
/*     */       } 
/* 326 */       int length = buffer.readableBytes();
/* 327 */       if (length > 108) {
/* 328 */         this.discardedBytes = length;
/* 329 */         buffer.skipBytes(length);
/* 330 */         this.discarding = true;
/* 331 */         failOverLimit(ctx, "over " + this.discardedBytes);
/*     */       } 
/* 333 */       return null;
/*     */     } 
/*     */     
/* 336 */     if (eol >= 0) {
/* 337 */       int delimLength = (buffer.getByte(eol) == 13) ? 2 : 1;
/* 338 */       buffer.readerIndex(eol + delimLength);
/* 339 */       this.discardedBytes = 0;
/* 340 */       this.discarding = false;
/*     */     } else {
/* 342 */       this.discardedBytes = buffer.readableBytes();
/* 343 */       buffer.skipBytes(this.discardedBytes);
/*     */     } 
/* 345 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void failOverLimit(ChannelHandlerContext ctx, int length) {
/* 350 */     failOverLimit(ctx, String.valueOf(length));
/*     */   }
/*     */   
/*     */   private void failOverLimit(ChannelHandlerContext ctx, String length) {
/* 354 */     int maxLength = (this.version == 1) ? 108 : this.v2MaxHeaderSize;
/* 355 */     fail(ctx, "header length (" + length + ") exceeds the allowed maximum (" + maxLength + ')', null);
/*     */   }
/*     */   private void fail(ChannelHandlerContext ctx, String errMsg, Exception e) {
/*     */     HAProxyProtocolException ppex;
/* 359 */     this.finished = true;
/* 360 */     ctx.close();
/*     */     
/* 362 */     if (errMsg != null && e != null) {
/* 363 */       ppex = new HAProxyProtocolException(errMsg, e);
/* 364 */     } else if (errMsg != null) {
/* 365 */       ppex = new HAProxyProtocolException(errMsg);
/* 366 */     } else if (e != null) {
/* 367 */       ppex = new HAProxyProtocolException(e);
/*     */     } else {
/* 369 */       ppex = new HAProxyProtocolException();
/*     */     } 
/* 371 */     throw ppex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProtocolDetectionResult<HAProxyProtocolVersion> detectProtocol(ByteBuf buffer) {
/* 378 */     if (buffer.readableBytes() < 12) {
/* 379 */       return ProtocolDetectionResult.needsMoreData();
/*     */     }
/*     */     
/* 382 */     int idx = buffer.readerIndex();
/*     */     
/* 384 */     if (match(BINARY_PREFIX, buffer, idx)) {
/* 385 */       return DETECTION_RESULT_V2;
/*     */     }
/* 387 */     if (match(TEXT_PREFIX, buffer, idx)) {
/* 388 */       return DETECTION_RESULT_V1;
/*     */     }
/* 390 */     return ProtocolDetectionResult.invalid();
/*     */   }
/*     */   
/*     */   private static boolean match(byte[] prefix, ByteBuf buffer, int idx) {
/* 394 */     for (int i = 0; i < prefix.length; i++) {
/* 395 */       byte b = buffer.getByte(idx + i);
/* 396 */       if (b != prefix[i]) {
/* 397 */         return false;
/*     */       }
/*     */     } 
/* 400 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\haproxy\HAProxyMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */