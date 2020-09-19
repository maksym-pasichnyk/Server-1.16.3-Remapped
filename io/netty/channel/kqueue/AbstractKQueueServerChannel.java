/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import java.net.InetSocketAddress;
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
/*     */ public abstract class AbstractKQueueServerChannel
/*     */   extends AbstractKQueueChannel
/*     */   implements ServerChannel
/*     */ {
/*  32 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*     */   
/*     */   AbstractKQueueServerChannel(BsdSocket fd) {
/*  35 */     this(fd, isSoErrorZero(fd));
/*     */   }
/*     */   
/*     */   AbstractKQueueServerChannel(BsdSocket fd, boolean active) {
/*  39 */     super((Channel)null, fd, active);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/*  44 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/*  49 */     return loop instanceof KQueueEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InetSocketAddress remoteAddress0() {
/*  54 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
/*  59 */     return new KQueueServerSocketUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*  64 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception {
/*  69 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/*  76 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   abstract Channel newChildChannel(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) throws Exception;
/*     */   
/*     */   final class KQueueServerSocketUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe {
/*     */     KQueueServerSocketUnsafe() {
/*  83 */       this.acceptedAddress = new byte[26];
/*     */     }
/*     */     private final byte[] acceptedAddress;
/*     */     void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
/*  87 */       assert AbstractKQueueServerChannel.this.eventLoop().inEventLoop();
/*  88 */       KQueueChannelConfig kQueueChannelConfig = AbstractKQueueServerChannel.this.config();
/*  89 */       if (AbstractKQueueServerChannel.this.shouldBreakReadReady((ChannelConfig)kQueueChannelConfig)) {
/*  90 */         clearReadFilter0();
/*     */         return;
/*     */       } 
/*  93 */       ChannelPipeline pipeline = AbstractKQueueServerChannel.this.pipeline();
/*  94 */       allocHandle.reset((ChannelConfig)kQueueChannelConfig);
/*  95 */       allocHandle.attemptedBytesRead(1);
/*  96 */       readReadyBefore();
/*     */       
/*  98 */       Throwable exception = null;
/*     */       try {
/*     */         while (true) {
/*     */           
/* 102 */           try { int acceptFd = AbstractKQueueServerChannel.this.socket.accept(this.acceptedAddress);
/* 103 */             if (acceptFd == -1) {
/*     */               
/* 105 */               allocHandle.lastBytesRead(-1);
/*     */               break;
/*     */             } 
/* 108 */             allocHandle.lastBytesRead(1);
/* 109 */             allocHandle.incMessagesRead(1);
/*     */             
/* 111 */             this.readPending = false;
/* 112 */             pipeline.fireChannelRead(AbstractKQueueServerChannel.this.newChildChannel(acceptFd, this.acceptedAddress, 1, this.acceptedAddress[0]));
/*     */             
/* 114 */             if (!allocHandle.continueReading())
/* 115 */               break;  } catch (Throwable t)
/* 116 */           { exception = t; break; }
/*     */         
/* 118 */         }  allocHandle.readComplete();
/* 119 */         pipeline.fireChannelReadComplete();
/*     */         
/* 121 */         if (exception != null) {
/* 122 */           pipeline.fireExceptionCaught(exception);
/*     */         }
/*     */       } finally {
/* 125 */         readReadyFinally((ChannelConfig)kQueueChannelConfig);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\AbstractKQueueServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */