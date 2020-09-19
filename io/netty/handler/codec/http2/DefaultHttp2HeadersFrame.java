/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public final class DefaultHttp2HeadersFrame
/*     */   extends AbstractHttp2StreamFrame
/*     */   implements Http2HeadersFrame
/*     */ {
/*     */   private final Http2Headers headers;
/*     */   private final boolean endStream;
/*     */   private final int padding;
/*     */   
/*     */   public DefaultHttp2HeadersFrame(Http2Headers headers) {
/*  39 */     this(headers, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2HeadersFrame(Http2Headers headers, boolean endStream) {
/*  48 */     this(headers, endStream, 0);
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
/*     */   public DefaultHttp2HeadersFrame(Http2Headers headers, boolean endStream, int padding) {
/*  60 */     this.headers = (Http2Headers)ObjectUtil.checkNotNull(headers, "headers");
/*  61 */     this.endStream = endStream;
/*  62 */     Http2CodecUtil.verifyPadding(padding);
/*  63 */     this.padding = padding;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2HeadersFrame stream(Http2FrameStream stream) {
/*  68 */     super.stream(stream);
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  74 */     return "HEADERS";
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Headers headers() {
/*  79 */     return this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndStream() {
/*  84 */     return this.endStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public int padding() {
/*  89 */     return this.padding;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  94 */     return StringUtil.simpleClassName(this) + "(stream=" + stream() + ", headers=" + this.headers + ", endStream=" + this.endStream + ", padding=" + this.padding + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 100 */     if (!(o instanceof DefaultHttp2HeadersFrame)) {
/* 101 */       return false;
/*     */     }
/* 103 */     DefaultHttp2HeadersFrame other = (DefaultHttp2HeadersFrame)o;
/* 104 */     return (super.equals(other) && this.headers.equals(other.headers) && this.endStream == other.endStream && this.padding == other.padding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 110 */     int hash = super.hashCode();
/* 111 */     hash = hash * 31 + this.headers.hashCode();
/* 112 */     hash = hash * 31 + (this.endStream ? 0 : 1);
/* 113 */     hash = hash * 31 + this.padding;
/* 114 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2HeadersFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */