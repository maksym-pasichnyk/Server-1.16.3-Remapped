/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public final class EpollSocketChannel
/*     */   extends AbstractEpollStreamChannel
/*     */   implements SocketChannel
/*     */ {
/*     */   private final EpollSocketChannelConfig config;
/*  42 */   private volatile Collection<InetAddress> tcpMd5SigAddresses = Collections.emptyList();
/*     */   
/*     */   public EpollSocketChannel() {
/*  45 */     super(LinuxSocket.newSocketStream(), false);
/*  46 */     this.config = new EpollSocketChannelConfig(this);
/*     */   }
/*     */   
/*     */   public EpollSocketChannel(int fd) {
/*  50 */     super(fd);
/*  51 */     this.config = new EpollSocketChannelConfig(this);
/*     */   }
/*     */   
/*     */   EpollSocketChannel(LinuxSocket fd, boolean active) {
/*  55 */     super(fd, active);
/*  56 */     this.config = new EpollSocketChannelConfig(this);
/*     */   }
/*     */   
/*     */   EpollSocketChannel(Channel parent, LinuxSocket fd, InetSocketAddress remoteAddress) {
/*  60 */     super(parent, fd, remoteAddress);
/*  61 */     this.config = new EpollSocketChannelConfig(this);
/*     */     
/*  63 */     if (parent instanceof EpollServerSocketChannel) {
/*  64 */       this.tcpMd5SigAddresses = ((EpollServerSocketChannel)parent).tcpMd5SigAddresses();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollTcpInfo tcpInfo() {
/*  72 */     return tcpInfo(new EpollTcpInfo());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollTcpInfo tcpInfo(EpollTcpInfo info) {
/*     */     try {
/*  81 */       this.socket.getTcpInfo(info);
/*  82 */       return info;
/*  83 */     } catch (IOException e) {
/*  84 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/*  90 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/*  95 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollSocketChannelConfig config() {
/* 100 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannel parent() {
/* 105 */     return (ServerSocketChannel)super.parent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
/* 110 */     return new EpollSocketChannelUnsafe();
/*     */   }
/*     */   
/*     */   private final class EpollSocketChannelUnsafe
/*     */     extends AbstractEpollStreamChannel.EpollStreamUnsafe {
/*     */     private EpollSocketChannelUnsafe() {}
/*     */     
/*     */     protected Executor prepareToClose() {
/*     */       try {
/* 119 */         if (EpollSocketChannel.this.isOpen() && EpollSocketChannel.this.config().getSoLinger() > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 124 */           ((EpollEventLoop)EpollSocketChannel.this.eventLoop()).remove(EpollSocketChannel.this);
/* 125 */           return (Executor)GlobalEventExecutor.INSTANCE;
/*     */         } 
/* 127 */       } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   void setTcpMd5Sig(Map<InetAddress, byte[]> keys) throws IOException {
/* 137 */     this.tcpMd5SigAddresses = TcpMd5Util.newTcpMd5Sigs(this, this.tcpMd5SigAddresses, keys);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */