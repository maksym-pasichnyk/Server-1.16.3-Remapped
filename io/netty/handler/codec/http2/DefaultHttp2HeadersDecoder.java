/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class DefaultHttp2HeadersDecoder
/*     */   implements Http2HeadersDecoder, Http2HeadersDecoder.Configuration
/*     */ {
/*     */   private static final float HEADERS_COUNT_WEIGHT_NEW = 0.2F;
/*     */   private static final float HEADERS_COUNT_WEIGHT_HISTORICAL = 0.8F;
/*     */   private final HpackDecoder hpackDecoder;
/*     */   private final boolean validateHeaders;
/*  39 */   private float headerArraySizeAccumulator = 8.0F;
/*     */   
/*     */   public DefaultHttp2HeadersDecoder() {
/*  42 */     this(true);
/*     */   }
/*     */   
/*     */   public DefaultHttp2HeadersDecoder(boolean validateHeaders) {
/*  46 */     this(validateHeaders, 8192L);
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
/*     */   public DefaultHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize) {
/*  58 */     this(validateHeaders, maxHeaderListSize, 32);
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
/*     */   public DefaultHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize, int initialHuffmanDecodeCapacity) {
/*  72 */     this(validateHeaders, new HpackDecoder(maxHeaderListSize, initialHuffmanDecodeCapacity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DefaultHttp2HeadersDecoder(boolean validateHeaders, HpackDecoder hpackDecoder) {
/*  80 */     this.hpackDecoder = (HpackDecoder)ObjectUtil.checkNotNull(hpackDecoder, "hpackDecoder");
/*  81 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */ 
/*     */   
/*     */   public void maxHeaderTableSize(long max) throws Http2Exception {
/*  86 */     this.hpackDecoder.setMaxHeaderTableSize(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public long maxHeaderTableSize() {
/*  91 */     return this.hpackDecoder.getMaxHeaderTableSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void maxHeaderListSize(long max, long goAwayMax) throws Http2Exception {
/*  96 */     this.hpackDecoder.setMaxHeaderListSize(max, goAwayMax);
/*     */   }
/*     */ 
/*     */   
/*     */   public long maxHeaderListSize() {
/* 101 */     return this.hpackDecoder.getMaxHeaderListSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public long maxHeaderListSizeGoAway() {
/* 106 */     return this.hpackDecoder.getMaxHeaderListSizeGoAway();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2HeadersDecoder.Configuration configuration() {
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers decodeHeaders(int streamId, ByteBuf headerBlock) throws Http2Exception {
/*     */     try {
/* 117 */       Http2Headers headers = newHeaders();
/* 118 */       this.hpackDecoder.decode(streamId, headerBlock, headers, this.validateHeaders);
/* 119 */       this.headerArraySizeAccumulator = 0.2F * headers.size() + 0.8F * this.headerArraySizeAccumulator;
/*     */       
/* 121 */       return headers;
/* 122 */     } catch (Http2Exception e) {
/* 123 */       throw e;
/* 124 */     } catch (Throwable e) {
/*     */ 
/*     */ 
/*     */       
/* 128 */       throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, e, e.getMessage(), new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int numberOfHeadersGuess() {
/* 137 */     return (int)this.headerArraySizeAccumulator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean validateHeaders() {
/* 145 */     return this.validateHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Http2Headers newHeaders() {
/* 153 */     return new DefaultHttp2Headers(this.validateHeaders, (int)this.headerArraySizeAccumulator);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2HeadersDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */