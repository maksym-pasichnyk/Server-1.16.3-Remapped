/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
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
/*     */ public abstract class AbstractEpollServerChannel
/*     */   extends AbstractEpollChannel
/*     */   implements ServerChannel
/*     */ {
/*  31 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*     */   
/*     */   protected AbstractEpollServerChannel(int fd) {
/*  34 */     this(new LinuxSocket(fd), false);
/*     */   }
/*     */   
/*     */   AbstractEpollServerChannel(LinuxSocket fd) {
/*  38 */     this(fd, isSoErrorZero(fd));
/*     */   }
/*     */   
/*     */   AbstractEpollServerChannel(LinuxSocket fd, boolean active) {
/*  42 */     super((Channel)null, fd, Native.EPOLLIN, active);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/*  47 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/*  52 */     return loop instanceof EpollEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InetSocketAddress remoteAddress0() {
/*  57 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
/*  62 */     return new EpollServerSocketUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*  67 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception {
/*  72 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   final class EpollServerSocketUnsafe
/*     */     extends AbstractEpollChannel.AbstractEpollUnsafe
/*     */   {
/*     */     private final byte[] acceptedAddress;
/*     */     
/*     */     EpollServerSocketUnsafe() {
/*  81 */       this.acceptedAddress = new byte[26];
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
/*  86 */       channelPromise.setFailure(new UnsupportedOperationException());
/*     */     }
/*     */ 
/*     */     
/*     */     void epollInReady() {
/*  91 */       assert AbstractEpollServerChannel.this.eventLoop().inEventLoop();
/*  92 */       EpollChannelConfig epollChannelConfig = AbstractEpollServerChannel.this.config();
/*  93 */       if (AbstractEpollServerChannel.this.shouldBreakEpollInReady((ChannelConfig)epollChannelConfig)) {
/*  94 */         clearEpollIn0();
/*     */         return;
/*     */       } 
/*  97 */       EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/*  98 */       allocHandle.edgeTriggered(AbstractEpollServerChannel.this.isFlagSet(Native.EPOLLET));
/*     */       
/* 100 */       ChannelPipeline pipeline = AbstractEpollServerChannel.this.pipeline();
/* 101 */       allocHandle.reset((ChannelConfig)epollChannelConfig);
/* 102 */       allocHandle.attemptedBytesRead(1);
/* 103 */       epollInBefore();
/*     */       
/* 105 */       Throwable exception = null;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*     */         while (true) {
/*     */           
/* 112 */           try { allocHandle.lastBytesRead(AbstractEpollServerChannel.this.socket.accept(this.acceptedAddress));
/* 113 */             if (allocHandle.lastBytesRead() == -1) {
/*     */               break;
/*     */             }
/*     */             
/* 117 */             allocHandle.incMessagesRead(1);
/*     */             
/* 119 */             this.readPending = false;
/* 120 */             pipeline.fireChannelRead(AbstractEpollServerChannel.this.newChildChannel(allocHandle.lastBytesRead(), this.acceptedAddress, 1, this.acceptedAddress[0]));
/*     */             
/* 122 */             if (!allocHandle.continueReading())
/* 123 */               break;  } catch (Throwable t)
/* 124 */           { exception = t; break; }
/*     */         
/* 126 */         }  allocHandle.readComplete();
/* 127 */         pipeline.fireChannelReadComplete();
/*     */         
/* 129 */         if (exception != null) {
/* 130 */           pipeline.fireExceptionCaught(exception);
/*     */         }
/*     */       } finally {
/* 133 */         epollInFinally((ChannelConfig)epollChannelConfig);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 140 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   abstract Channel newChildChannel(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\AbstractEpollServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */