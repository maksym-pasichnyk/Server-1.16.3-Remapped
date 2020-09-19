/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.SocketAddress;
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
/*     */ public class DefaultAddressedEnvelope<M, A extends SocketAddress>
/*     */   implements AddressedEnvelope<M, A>
/*     */ {
/*     */   private final M message;
/*     */   private final A sender;
/*     */   private final A recipient;
/*     */   
/*     */   public DefaultAddressedEnvelope(M message, A recipient, A sender) {
/*  42 */     if (message == null) {
/*  43 */       throw new NullPointerException("message");
/*     */     }
/*     */     
/*  46 */     if (recipient == null && sender == null) {
/*  47 */       throw new NullPointerException("recipient and sender");
/*     */     }
/*     */     
/*  50 */     this.message = message;
/*  51 */     this.sender = sender;
/*  52 */     this.recipient = recipient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultAddressedEnvelope(M message, A recipient) {
/*  60 */     this(message, recipient, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public M content() {
/*  65 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public A sender() {
/*  70 */     return this.sender;
/*     */   }
/*     */ 
/*     */   
/*     */   public A recipient() {
/*  75 */     return this.recipient;
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/*  80 */     if (this.message instanceof ReferenceCounted) {
/*  81 */       return ((ReferenceCounted)this.message).refCnt();
/*     */     }
/*  83 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AddressedEnvelope<M, A> retain() {
/*  89 */     ReferenceCountUtil.retain(this.message);
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AddressedEnvelope<M, A> retain(int increment) {
/*  95 */     ReferenceCountUtil.retain(this.message, increment);
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 101 */     return ReferenceCountUtil.release(this.message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 106 */     return ReferenceCountUtil.release(this.message, decrement);
/*     */   }
/*     */ 
/*     */   
/*     */   public AddressedEnvelope<M, A> touch() {
/* 111 */     ReferenceCountUtil.touch(this.message);
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AddressedEnvelope<M, A> touch(Object hint) {
/* 117 */     ReferenceCountUtil.touch(this.message, hint);
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     if (this.sender != null) {
/* 124 */       return StringUtil.simpleClassName(this) + '(' + this.sender + " => " + this.recipient + ", " + this.message + ')';
/*     */     }
/*     */     
/* 127 */     return StringUtil.simpleClassName(this) + "(=> " + this.recipient + ", " + this.message + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\DefaultAddressedEnvelope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */