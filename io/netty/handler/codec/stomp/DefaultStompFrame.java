/*     */ package io.netty.handler.codec.stomp;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.CharsetUtil;
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
/*     */ 
/*     */ 
/*     */ public class DefaultStompFrame
/*     */   extends DefaultStompHeadersSubframe
/*     */   implements StompFrame
/*     */ {
/*     */   private final ByteBuf content;
/*     */   
/*     */   public DefaultStompFrame(StompCommand command) {
/*  30 */     this(command, Unpooled.buffer(0));
/*     */   }
/*     */   
/*     */   public DefaultStompFrame(StompCommand command, ByteBuf content) {
/*  34 */     this(command, content, (DefaultStompHeaders)null);
/*     */   }
/*     */   
/*     */   DefaultStompFrame(StompCommand command, ByteBuf content, DefaultStompHeaders headers) {
/*  38 */     super(command, headers);
/*     */     
/*  40 */     if (content == null) {
/*  41 */       throw new NullPointerException("content");
/*     */     }
/*     */     
/*  44 */     this.content = content;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  49 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame copy() {
/*  54 */     return replace(this.content.copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame duplicate() {
/*  59 */     return replace(this.content.duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame retainedDuplicate() {
/*  64 */     return replace(this.content.retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame replace(ByteBuf content) {
/*  69 */     return new DefaultStompFrame(this.command, content, this.headers.copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/*  74 */     return this.content.refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame retain() {
/*  79 */     this.content.retain();
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame retain(int increment) {
/*  85 */     this.content.retain(increment);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame touch() {
/*  91 */     this.content.touch();
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StompFrame touch(Object hint) {
/*  97 */     this.content.touch(hint);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 103 */     return this.content.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 108 */     return this.content.release(decrement);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return "DefaultStompFrame{command=" + this.command + ", headers=" + this.headers + ", content=" + this.content
/*     */ 
/*     */       
/* 116 */       .toString(CharsetUtil.UTF_8) + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\DefaultStompFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */