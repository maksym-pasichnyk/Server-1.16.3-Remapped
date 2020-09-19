/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.AbstractFuture;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class VoidChannelPromise
/*     */   extends AbstractFuture<Void>
/*     */   implements ChannelPromise
/*     */ {
/*     */   private final Channel channel;
/*     */   private final ChannelFutureListener fireExceptionListener;
/*     */   
/*     */   public VoidChannelPromise(Channel channel, boolean fireException) {
/*  38 */     if (channel == null) {
/*  39 */       throw new NullPointerException("channel");
/*     */     }
/*  41 */     this.channel = channel;
/*  42 */     if (fireException) {
/*  43 */       this.fireExceptionListener = new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/*  46 */             Throwable cause = future.cause();
/*  47 */             if (cause != null) {
/*  48 */               VoidChannelPromise.this.fireException0(cause);
/*     */             }
/*     */           }
/*     */         };
/*     */     } else {
/*  53 */       this.fireExceptionListener = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
/*  59 */     fail();
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
/*  65 */     fail();
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoidChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoidChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise await() throws InterruptedException {
/*  83 */     if (Thread.interrupted()) {
/*  84 */       throw new InterruptedException();
/*     */     }
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean await(long timeout, TimeUnit unit) {
/*  91 */     fail();
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean await(long timeoutMillis) {
/*  97 */     fail();
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise awaitUninterruptibly() {
/* 103 */     fail();
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
/* 109 */     fail();
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeoutMillis) {
/* 115 */     fail();
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Channel channel() {
/* 121 */     return this.channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setUncancellable() {
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancellable() {
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable cause() {
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise sync() {
/* 156 */     fail();
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise syncUninterruptibly() {
/* 162 */     fail();
/* 163 */     return this;
/*     */   }
/*     */   
/*     */   public VoidChannelPromise setFailure(Throwable cause) {
/* 167 */     fireException0(cause);
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise setSuccess() {
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryFailure(Throwable cause) {
/* 178 */     fireException0(cause);
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean trySuccess() {
/* 194 */     return false;
/*     */   }
/*     */   
/*     */   private static void fail() {
/* 198 */     throw new IllegalStateException("void future");
/*     */   }
/*     */ 
/*     */   
/*     */   public VoidChannelPromise setSuccess(Void result) {
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean trySuccess(Void result) {
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Void getNow() {
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelPromise unvoid() {
/* 218 */     ChannelPromise promise = new DefaultChannelPromise(this.channel);
/* 219 */     if (this.fireExceptionListener != null) {
/* 220 */       promise.addListener(this.fireExceptionListener);
/*     */     }
/* 222 */     return promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVoid() {
/* 227 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireException0(Throwable cause) {
/* 235 */     if (this.fireExceptionListener != null && this.channel.isRegistered())
/* 236 */       this.channel.pipeline().fireExceptionCaught(cause); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\VoidChannelPromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */