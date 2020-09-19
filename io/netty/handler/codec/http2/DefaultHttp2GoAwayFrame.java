/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.DefaultByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ public final class DefaultHttp2GoAwayFrame
/*     */   extends DefaultByteBufHolder
/*     */   implements Http2GoAwayFrame
/*     */ {
/*     */   private final long errorCode;
/*     */   private final int lastStreamId;
/*     */   private int extraStreamIds;
/*     */   
/*     */   public DefaultHttp2GoAwayFrame(Http2Error error) {
/*  40 */     this(error.code());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2GoAwayFrame(long errorCode) {
/*  49 */     this(errorCode, Unpooled.EMPTY_BUFFER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2GoAwayFrame(Http2Error error, ByteBuf content) {
/*  59 */     this(error.code(), content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2GoAwayFrame(long errorCode, ByteBuf content) {
/*  69 */     this(-1, errorCode, content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DefaultHttp2GoAwayFrame(int lastStreamId, long errorCode, ByteBuf content) {
/*  79 */     super(content);
/*  80 */     this.errorCode = errorCode;
/*  81 */     this.lastStreamId = lastStreamId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  86 */     return "GOAWAY";
/*     */   }
/*     */ 
/*     */   
/*     */   public long errorCode() {
/*  91 */     return this.errorCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public int extraStreamIds() {
/*  96 */     return this.extraStreamIds;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame setExtraStreamIds(int extraStreamIds) {
/* 101 */     if (extraStreamIds < 0) {
/* 102 */       throw new IllegalArgumentException("extraStreamIds must be non-negative");
/*     */     }
/* 104 */     this.extraStreamIds = extraStreamIds;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastStreamId() {
/* 110 */     return this.lastStreamId;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame copy() {
/* 115 */     return new DefaultHttp2GoAwayFrame(this.lastStreamId, this.errorCode, content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame duplicate() {
/* 120 */     return (Http2GoAwayFrame)super.duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame retainedDuplicate() {
/* 125 */     return (Http2GoAwayFrame)super.retainedDuplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame replace(ByteBuf content) {
/* 130 */     return (new DefaultHttp2GoAwayFrame(this.errorCode, content)).setExtraStreamIds(this.extraStreamIds);
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame retain() {
/* 135 */     super.retain();
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame retain(int increment) {
/* 141 */     super.retain(increment);
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame touch() {
/* 147 */     super.touch();
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2GoAwayFrame touch(Object hint) {
/* 153 */     super.touch(hint);
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 159 */     if (!(o instanceof DefaultHttp2GoAwayFrame)) {
/* 160 */       return false;
/*     */     }
/* 162 */     DefaultHttp2GoAwayFrame other = (DefaultHttp2GoAwayFrame)o;
/* 163 */     return (this.errorCode == other.errorCode && this.extraStreamIds == other.extraStreamIds && super.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 168 */     int hash = super.hashCode();
/* 169 */     hash = hash * 31 + (int)(this.errorCode ^ this.errorCode >>> 32L);
/* 170 */     hash = hash * 31 + this.extraStreamIds;
/* 171 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 176 */     return StringUtil.simpleClassName(this) + "(errorCode=" + this.errorCode + ", content=" + content() + ", extraStreamIds=" + this.extraStreamIds + ", lastStreamId=" + this.lastStreamId + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2GoAwayFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */