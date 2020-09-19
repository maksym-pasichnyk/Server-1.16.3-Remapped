/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.socket.DefaultServerSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.List;
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
/*     */ public class NioServerSocketChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements ServerSocketChannel
/*     */ {
/*  46 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*  47 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*     */   
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioServerSocketChannel.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ServerSocketChannelConfig config;
/*     */ 
/*     */ 
/*     */   
/*     */   private static ServerSocketChannel newSocket(SelectorProvider provider) {
/*     */     try {
/*  59 */       return provider.openServerSocketChannel();
/*  60 */     } catch (IOException e) {
/*  61 */       throw new ChannelException("Failed to open a server socket.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioServerSocketChannel() {
/*  72 */     this(newSocket(DEFAULT_SELECTOR_PROVIDER));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioServerSocketChannel(SelectorProvider provider) {
/*  79 */     this(newSocket(provider));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioServerSocketChannel(ServerSocketChannel channel) {
/*  86 */     super(null, channel, 16);
/*  87 */     this.config = (ServerSocketChannelConfig)new NioServerSocketChannelConfig(this, javaChannel().socket());
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/*  92 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/*  97 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerSocketChannelConfig config() {
/* 102 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 107 */     return javaChannel().socket().isBound();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ServerSocketChannel javaChannel() {
/* 117 */     return (ServerSocketChannel)super.javaChannel();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 122 */     return SocketUtils.localSocketAddress(javaChannel().socket());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 127 */     if (PlatformDependent.javaVersion() >= 7) {
/* 128 */       javaChannel().bind(localAddress, this.config.getBacklog());
/*     */     } else {
/* 130 */       javaChannel().socket().bind(localAddress, this.config.getBacklog());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 136 */     javaChannel().close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception {
/* 141 */     SocketChannel ch = SocketUtils.accept(javaChannel());
/*     */     
/*     */     try {
/* 144 */       if (ch != null) {
/* 145 */         buf.add(new NioSocketChannel((Channel)this, ch));
/* 146 */         return 1;
/*     */       } 
/* 148 */     } catch (Throwable t) {
/* 149 */       logger.warn("Failed to create a new channel from an accepted socket.", t);
/*     */       
/*     */       try {
/* 152 */         ch.close();
/* 153 */       } catch (Throwable t2) {
/* 154 */         logger.warn("Failed to close a socket.", t2);
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 165 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doFinishConnect() throws Exception {
/* 170 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 180 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
/* 185 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception {
/* 190 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private final class NioServerSocketChannelConfig extends DefaultServerSocketChannelConfig {
/*     */     private NioServerSocketChannelConfig(NioServerSocketChannel channel, ServerSocket javaSocket) {
/* 195 */       super(channel, javaSocket);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void autoReadCleared() {
/* 200 */       NioServerSocketChannel.this.clearReadPending();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean closeOnReadError(Throwable cause) {
/* 207 */     return super.closeOnReadError(cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\nio\NioServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */