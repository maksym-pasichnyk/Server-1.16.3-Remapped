/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
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
/*     */ public abstract class AbstractOioChannel
/*     */   extends AbstractChannel
/*     */ {
/*     */   protected static final int SO_TIMEOUT = 1000;
/*     */   boolean readPending;
/*     */   
/*  34 */   private final Runnable readTask = new Runnable()
/*     */     {
/*     */       public void run() {
/*  37 */         AbstractOioChannel.this.doRead();
/*     */       }
/*     */     };
/*  40 */   private final Runnable clearReadPendingRunnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  43 */         AbstractOioChannel.this.readPending = false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractOioChannel(Channel parent) {
/*  51 */     super(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractChannel.AbstractUnsafe newUnsafe() {
/*  56 */     return new DefaultOioUnsafe();
/*     */   }
/*     */   private final class DefaultOioUnsafe extends AbstractChannel.AbstractUnsafe { private DefaultOioUnsafe() {
/*  59 */       super(AbstractOioChannel.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/*  64 */       if (!promise.setUncancellable() || !ensureOpen(promise)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  69 */         boolean wasActive = AbstractOioChannel.this.isActive();
/*  70 */         AbstractOioChannel.this.doConnect(remoteAddress, localAddress);
/*     */ 
/*     */ 
/*     */         
/*  74 */         boolean active = AbstractOioChannel.this.isActive();
/*     */         
/*  76 */         safeSetSuccess(promise);
/*  77 */         if (!wasActive && active) {
/*  78 */           AbstractOioChannel.this.pipeline().fireChannelActive();
/*     */         }
/*  80 */       } catch (Throwable t) {
/*  81 */         safeSetFailure(promise, annotateConnectException(t, remoteAddress));
/*  82 */         closeIfClosed();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/*  89 */     return loop instanceof io.netty.channel.ThreadPerChannelEventLoop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doConnect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2) throws Exception;
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doBeginRead() throws Exception {
/* 100 */     if (this.readPending) {
/*     */       return;
/*     */     }
/*     */     
/* 104 */     this.readPending = true;
/* 105 */     eventLoop().execute(this.readTask);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doRead();
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean isReadPending() {
/* 116 */     return this.readPending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void setReadPending(final boolean readPending) {
/* 125 */     if (isRegistered()) {
/* 126 */       EventLoop eventLoop = eventLoop();
/* 127 */       if (eventLoop.inEventLoop()) {
/* 128 */         this.readPending = readPending;
/*     */       } else {
/* 130 */         eventLoop.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 133 */                 AbstractOioChannel.this.readPending = readPending;
/*     */               }
/*     */             });
/*     */       } 
/*     */     } else {
/* 138 */       this.readPending = readPending;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void clearReadPending() {
/* 146 */     if (isRegistered()) {
/* 147 */       EventLoop eventLoop = eventLoop();
/* 148 */       if (eventLoop.inEventLoop()) {
/* 149 */         this.readPending = false;
/*     */       } else {
/* 151 */         eventLoop.execute(this.clearReadPendingRunnable);
/*     */       } 
/*     */     } else {
/*     */       
/* 155 */       this.readPending = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\oio\AbstractOioChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */