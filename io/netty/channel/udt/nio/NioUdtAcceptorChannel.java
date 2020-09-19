/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.ChannelUDT;
/*     */ import com.barchart.udt.nio.ServerSocketChannelUDT;
/*     */ import com.barchart.udt.nio.SocketChannelUDT;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.udt.DefaultUdtServerChannelConfig;
/*     */ import io.netty.channel.udt.UdtChannel;
/*     */ import io.netty.channel.udt.UdtChannelConfig;
/*     */ import io.netty.channel.udt.UdtServerChannel;
/*     */ import io.netty.channel.udt.UdtServerChannelConfig;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.ServerSocketChannel;
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
/*     */ @Deprecated
/*     */ public abstract class NioUdtAcceptorChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements UdtServerChannel
/*     */ {
/*  48 */   protected static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtAcceptorChannel.class);
/*     */   
/*  50 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*     */   
/*     */   private final UdtServerChannelConfig config;
/*     */   
/*     */   protected NioUdtAcceptorChannel(ServerSocketChannelUDT channelUDT) {
/*  55 */     super(null, (SelectableChannel)channelUDT, 16);
/*     */     try {
/*  57 */       channelUDT.configureBlocking(false);
/*  58 */       this.config = (UdtServerChannelConfig)new DefaultUdtServerChannelConfig((UdtChannel)this, (ChannelUDT)channelUDT, true);
/*  59 */     } catch (Exception e) {
/*     */       try {
/*  61 */         channelUDT.close();
/*  62 */       } catch (Exception e2) {
/*  63 */         if (logger.isWarnEnabled()) {
/*  64 */           logger.warn("Failed to close channel.", e2);
/*     */         }
/*     */       } 
/*  67 */       throw new ChannelException("Failed to configure channel.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected NioUdtAcceptorChannel(TypeUDT type) {
/*  72 */     this(NioUdtProvider.newAcceptorChannelUDT(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public UdtServerChannelConfig config() {
/*  77 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/*  82 */     javaChannel().socket().bind(localAddress, this.config.getBacklog());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/*  87 */     javaChannel().close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/*  93 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/*  98 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doFinishConnect() throws Exception {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
/* 108 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception {
/* 113 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 118 */     return javaChannel().socket().isBound();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ServerSocketChannelUDT javaChannel() {
/* 123 */     return (ServerSocketChannelUDT)super.javaChannel();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/* 128 */     return SocketUtils.localSocketAddress((ServerSocket)javaChannel().socket());
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 133 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 148 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception {
/* 153 */     SocketChannelUDT channelUDT = (SocketChannelUDT)SocketUtils.accept((ServerSocketChannel)javaChannel());
/* 154 */     if (channelUDT == null) {
/* 155 */       return 0;
/*     */     }
/* 157 */     buf.add(newConnectorChannel(channelUDT));
/* 158 */     return 1;
/*     */   }
/*     */   
/*     */   protected abstract UdtChannel newConnectorChannel(SocketChannelUDT paramSocketChannelUDT);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\nio\NioUdtAcceptorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */