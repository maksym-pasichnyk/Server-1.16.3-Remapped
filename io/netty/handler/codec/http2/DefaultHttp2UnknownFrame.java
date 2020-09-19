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
/*     */ public final class DefaultHttp2UnknownFrame
/*     */   extends DefaultByteBufHolder
/*     */   implements Http2UnknownFrame
/*     */ {
/*     */   private final byte frameType;
/*     */   private final Http2Flags flags;
/*     */   private Http2FrameStream stream;
/*     */   
/*     */   public DefaultHttp2UnknownFrame(byte frameType, Http2Flags flags) {
/*  31 */     this(frameType, flags, Unpooled.EMPTY_BUFFER);
/*     */   }
/*     */   
/*     */   public DefaultHttp2UnknownFrame(byte frameType, Http2Flags flags, ByteBuf data) {
/*  35 */     super(data);
/*  36 */     this.frameType = frameType;
/*  37 */     this.flags = flags;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameStream stream() {
/*  42 */     return this.stream;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame stream(Http2FrameStream stream) {
/*  47 */     this.stream = stream;
/*  48 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte frameType() {
/*  53 */     return this.frameType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2Flags flags() {
/*  58 */     return this.flags;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  63 */     return "UNKNOWN";
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame copy() {
/*  68 */     return replace(content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame duplicate() {
/*  73 */     return replace(content().duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame retainedDuplicate() {
/*  78 */     return replace(content().retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame replace(ByteBuf content) {
/*  83 */     return (new DefaultHttp2UnknownFrame(this.frameType, this.flags, content)).stream(stream());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame retain() {
/*  88 */     super.retain();
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame retain(int increment) {
/*  94 */     super.retain(increment);
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return StringUtil.simpleClassName(this) + "(frameType=" + frameType() + ", stream=" + stream() + ", flags=" + 
/* 101 */       flags() + ", content=" + contentToString() + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame touch() {
/* 106 */     super.touch();
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2UnknownFrame touch(Object hint) {
/* 112 */     super.touch(hint);
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 118 */     if (!(o instanceof DefaultHttp2UnknownFrame)) {
/* 119 */       return false;
/*     */     }
/* 121 */     DefaultHttp2UnknownFrame other = (DefaultHttp2UnknownFrame)o;
/* 122 */     return ((super.equals(other) && flags().equals(other.flags()) && 
/* 123 */       frameType() == other.frameType() && stream() == null && other.stream() == null) || 
/* 124 */       stream().equals(other.stream()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 129 */     int hash = super.hashCode();
/* 130 */     hash = hash * 31 + frameType();
/* 131 */     hash = hash * 31 + flags().hashCode();
/* 132 */     if (stream() != null) {
/* 133 */       hash = hash * 31 + stream().hashCode();
/*     */     }
/*     */     
/* 136 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2UnknownFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */