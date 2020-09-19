/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.DefaultByteBufHolder;
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
/*     */ 
/*     */ 
/*     */ public abstract class WebSocketFrame
/*     */   extends DefaultByteBufHolder
/*     */ {
/*     */   private final boolean finalFragment;
/*     */   private final int rsv;
/*     */   
/*     */   protected WebSocketFrame(ByteBuf binaryData) {
/*  39 */     this(true, 0, binaryData);
/*     */   }
/*     */   
/*     */   protected WebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
/*  43 */     super(binaryData);
/*  44 */     this.finalFragment = finalFragment;
/*  45 */     this.rsv = rsv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinalFragment() {
/*  53 */     return this.finalFragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int rsv() {
/*  60 */     return this.rsv;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketFrame copy() {
/*  65 */     return (WebSocketFrame)super.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketFrame duplicate() {
/*  70 */     return (WebSocketFrame)super.duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketFrame retainedDuplicate() {
/*  75 */     return (WebSocketFrame)super.retainedDuplicate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     return StringUtil.simpleClassName(this) + "(data: " + contentToString() + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketFrame retain() {
/*  88 */     super.retain();
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketFrame retain(int increment) {
/*  94 */     super.retain(increment);
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketFrame touch() {
/* 100 */     super.touch();
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketFrame touch(Object hint) {
/* 106 */     super.touch(hint);
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public abstract WebSocketFrame replace(ByteBuf paramByteBuf);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */