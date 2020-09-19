/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ final class ComposedLastHttpContent
/*     */   implements LastHttpContent
/*     */ {
/*     */   private final HttpHeaders trailingHeaders;
/*     */   private DecoderResult result;
/*     */   
/*     */   ComposedLastHttpContent(HttpHeaders trailingHeaders) {
/*  28 */     this.trailingHeaders = trailingHeaders;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders trailingHeaders() {
/*  33 */     return this.trailingHeaders;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent copy() {
/*  38 */     LastHttpContent content = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
/*  39 */     content.trailingHeaders().set(trailingHeaders());
/*  40 */     return content;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent duplicate() {
/*  45 */     return copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent retainedDuplicate() {
/*  50 */     return copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent replace(ByteBuf content) {
/*  55 */     LastHttpContent dup = new DefaultLastHttpContent(content);
/*  56 */     dup.trailingHeaders().setAll(trailingHeaders());
/*  57 */     return dup;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent retain(int increment) {
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent retain() {
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent touch() {
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastHttpContent touch(Object hint) {
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  82 */     return Unpooled.EMPTY_BUFFER;
/*     */   }
/*     */ 
/*     */   
/*     */   public DecoderResult decoderResult() {
/*  87 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public DecoderResult getDecoderResult() {
/*  92 */     return decoderResult();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDecoderResult(DecoderResult result) {
/*  97 */     this.result = result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/* 102 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 112 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\ComposedLastHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */