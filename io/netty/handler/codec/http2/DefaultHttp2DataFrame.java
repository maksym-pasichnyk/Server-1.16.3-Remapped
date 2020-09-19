/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ public final class DefaultHttp2DataFrame
/*     */   extends AbstractHttp2StreamFrame
/*     */   implements Http2DataFrame
/*     */ {
/*     */   private final ByteBuf content;
/*     */   private final boolean endStream;
/*     */   private final int padding;
/*     */   private final int initialFlowControlledBytes;
/*     */   
/*     */   public DefaultHttp2DataFrame(ByteBuf content) {
/*  43 */     this(content, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame(boolean endStream) {
/*  52 */     this(Unpooled.EMPTY_BUFFER, endStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame(ByteBuf content, boolean endStream) {
/*  62 */     this(content, endStream, 0);
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
/*     */   public DefaultHttp2DataFrame(ByteBuf content, boolean endStream, int padding) {
/*  74 */     this.content = (ByteBuf)ObjectUtil.checkNotNull(content, "content");
/*  75 */     this.endStream = endStream;
/*  76 */     Http2CodecUtil.verifyPadding(padding);
/*  77 */     this.padding = padding;
/*  78 */     if (content().readableBytes() + padding > 2147483647L) {
/*  79 */       throw new IllegalArgumentException("content + padding must be <= Integer.MAX_VALUE");
/*     */     }
/*  81 */     this.initialFlowControlledBytes = content().readableBytes() + padding;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame stream(Http2FrameStream stream) {
/*  86 */     super.stream(stream);
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  92 */     return "DATA";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndStream() {
/*  97 */     return this.endStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public int padding() {
/* 102 */     return this.padding;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/* 107 */     if (this.content.refCnt() <= 0) {
/* 108 */       throw new IllegalReferenceCountException(this.content.refCnt());
/*     */     }
/* 110 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public int initialFlowControlledBytes() {
/* 115 */     return this.initialFlowControlledBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame copy() {
/* 120 */     return replace(content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame duplicate() {
/* 125 */     return replace(content().duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame retainedDuplicate() {
/* 130 */     return replace(content().retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame replace(ByteBuf content) {
/* 135 */     return new DefaultHttp2DataFrame(content, this.endStream, this.padding);
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/* 140 */     return this.content.refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 145 */     return this.content.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 150 */     return this.content.release(decrement);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame retain() {
/* 155 */     this.content.retain();
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame retain(int increment) {
/* 161 */     this.content.retain(increment);
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return StringUtil.simpleClassName(this) + "(stream=" + stream() + ", content=" + this.content + ", endStream=" + this.endStream + ", padding=" + this.padding + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame touch() {
/* 173 */     this.content.touch();
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2DataFrame touch(Object hint) {
/* 179 */     this.content.touch(hint);
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 185 */     if (!(o instanceof DefaultHttp2DataFrame)) {
/* 186 */       return false;
/*     */     }
/* 188 */     DefaultHttp2DataFrame other = (DefaultHttp2DataFrame)o;
/* 189 */     return (super.equals(other) && this.content.equals(other.content()) && this.endStream == other.endStream && this.padding == other.padding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 195 */     int hash = super.hashCode();
/* 196 */     hash = hash * 31 + this.content.hashCode();
/* 197 */     hash = hash * 31 + (this.endStream ? 0 : 1);
/* 198 */     hash = hash * 31 + this.padding;
/* 199 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2DataFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */