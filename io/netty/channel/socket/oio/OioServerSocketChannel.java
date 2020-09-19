/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.oio.AbstractOioMessageChannel;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class OioServerSocketChannel
/*     */   extends AbstractOioMessageChannel
/*     */   implements ServerSocketChannel
/*     */ {
/*  46 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioServerSocketChannel.class);
/*     */   
/*  48 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 1);
/*     */   
/*     */   private static ServerSocket newServerSocket() {
/*     */     try {
/*  52 */       return new ServerSocket();
/*  53 */     } catch (IOException e) {
/*  54 */       throw new ChannelException("failed to create a server socket", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   final ServerSocket socket;
/*  59 */   final Lock shutdownLock = new ReentrantLock();
/*     */ 
/*     */   
/*     */   private final OioServerSocketChannelConfig config;
/*     */ 
/*     */   
/*     */   public OioServerSocketChannel() {
/*  66 */     this(newServerSocket());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OioServerSocketChannel(ServerSocket socket) {
/*  75 */     super(null);
/*  76 */     if (socket == null) {
/*  77 */       throw new NullPointerException("socket");
/*     */     }
/*     */     
/*  80 */     boolean success = false;
/*     */     try {
/*  82 */       socket.setSoTimeout(1000);
/*  83 */       success = true;
/*  84 */     } catch (IOException e) {
/*  85 */       throw new ChannelException("Failed to set the server socket timeout.", e);
/*     */     } finally {
/*     */       
/*  88 */       if (!success) {
/*     */         try {
/*  90 */           socket.close();
/*  91 */         } catch (IOException e) {
/*  92 */           if (logger.isWarnEnabled()) {
/*  93 */             logger.warn("Failed to close a partially initialized socket.", e);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  99 */     this.socket = socket;
/* 100 */     this.config = new DefaultOioServerSocketChannelConfig(this, socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 105 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 110 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   public OioServerSocketChannelConfig config() {
/* 115 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 125 */     return !this.socket.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 130 */     return (isOpen() && this.socket.isBound());
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 135 */     return SocketUtils.localSocketAddress(this.socket);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 140 */     this.socket.bind(localAddress, this.config.getBacklog());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 145 */     this.socket.close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception {
/* 150 */     if (this.socket.isClosed()) {
/* 151 */       return -1;
/*     */     }
/*     */     
/*     */     try {
/* 155 */       Socket s = this.socket.accept();
/*     */       try {
/* 157 */         buf.add(new OioSocketChannel((Channel)this, s));
/* 158 */         return 1;
/* 159 */       } catch (Throwable t) {
/* 160 */         logger.warn("Failed to create a new channel from an accepted socket.", t);
/*     */         try {
/* 162 */           s.close();
/* 163 */         } catch (Throwable t2) {
/* 164 */           logger.warn("Failed to close a socket.", t2);
/*     */         } 
/*     */       } 
/* 167 */     } catch (SocketTimeoutException socketTimeoutException) {}
/*     */ 
/*     */     
/* 170 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/* 175 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception {
/* 180 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 186 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 196 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void setReadPending(boolean readPending) {
/* 202 */     super.setReadPending(readPending);
/*     */   }
/*     */   
/*     */   final void clearReadPending0() {
/* 206 */     clearReadPending();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\oio\OioServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */