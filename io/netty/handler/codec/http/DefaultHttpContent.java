/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
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
/*     */ public class DefaultHttpContent
/*     */   extends DefaultHttpObject
/*     */   implements HttpContent
/*     */ {
/*     */   private final ByteBuf content;
/*     */   
/*     */   public DefaultHttpContent(ByteBuf content) {
/*  32 */     if (content == null) {
/*  33 */       throw new NullPointerException("content");
/*     */     }
/*  35 */     this.content = content;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  40 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent copy() {
/*  45 */     return replace(this.content.copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent duplicate() {
/*  50 */     return replace(this.content.duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent retainedDuplicate() {
/*  55 */     return replace(this.content.retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent replace(ByteBuf content) {
/*  60 */     return new DefaultHttpContent(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/*  65 */     return this.content.refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent retain() {
/*  70 */     this.content.retain();
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent retain(int increment) {
/*  76 */     this.content.retain(increment);
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent touch() {
/*  82 */     this.content.touch();
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContent touch(Object hint) {
/*  88 */     this.content.touch(hint);
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/*  94 */     return this.content.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/*  99 */     return this.content.release(decrement);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return StringUtil.simpleClassName(this) + "(data: " + 
/* 105 */       content() + ", decoderResult: " + decoderResult() + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\DefaultHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */