/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.bootstrap.ServerBootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollEventLoopGroup;
/*     */ import io.netty.channel.epoll.EpollServerSocketChannel;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.PacketDecoder;
/*     */ import net.minecraft.network.PacketEncoder;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RateKickingConnection;
/*     */ import net.minecraft.network.Varint21FrameDecoder;
/*     */ import net.minecraft.network.Varint21LengthFieldPrepender;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.LazyLoadedValue;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerConnectionListener
/*     */ {
/*  54 */   private static final Logger LOGGER = LogManager.getLogger();
/*  55 */   public static final LazyLoadedValue<NioEventLoopGroup> SERVER_EVENT_GROUP = new LazyLoadedValue(() -> new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build()));
/*     */ 
/*     */   
/*  58 */   public static final LazyLoadedValue<EpollEventLoopGroup> SERVER_EPOLL_EVENT_GROUP = new LazyLoadedValue(() -> new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build()));
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*     */   public volatile boolean running;
/*     */   
/*  64 */   private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
/*  65 */   private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   public ServerConnectionListener(MinecraftServer debug1) {
/*  68 */     this.server = debug1;
/*  69 */     this.running = true;
/*     */   }
/*     */   
/*     */   public void startTcpServerListener(@Nullable InetAddress debug1, int debug2) throws IOException {
/*  73 */     synchronized (this.channels) {
/*     */       Class<NioServerSocketChannel> clazz;
/*     */       LazyLoadedValue<NioEventLoopGroup> lazyLoadedValue;
/*  76 */       if (Epoll.isAvailable() && this.server.isEpollEnabled()) {
/*  77 */         Class<EpollServerSocketChannel> clazz1 = EpollServerSocketChannel.class;
/*  78 */         LazyLoadedValue<EpollEventLoopGroup> lazyLoadedValue1 = SERVER_EPOLL_EVENT_GROUP;
/*  79 */         LOGGER.info("Using epoll channel type");
/*     */       } else {
/*  81 */         clazz = NioServerSocketChannel.class;
/*  82 */         lazyLoadedValue = SERVER_EVENT_GROUP;
/*  83 */         LOGGER.info("Using default channel type");
/*     */       } 
/*     */       
/*  86 */       this.channels.add(((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap())
/*  87 */           .channel(clazz))
/*  88 */           .childHandler((ChannelHandler)new ChannelInitializer<Channel>()
/*     */             {
/*     */               protected void initChannel(Channel debug1) throws Exception {
/*     */                 try {
/*  92 */                   debug1.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*  93 */                 } catch (ChannelException channelException) {}
/*     */ 
/*     */                 
/*  96 */                 debug1.pipeline()
/*  97 */                   .addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30))
/*  98 */                   .addLast("legacy_query", (ChannelHandler)new LegacyQueryHandler(ServerConnectionListener.this))
/*     */                   
/* 100 */                   .addLast("splitter", (ChannelHandler)new Varint21FrameDecoder())
/* 101 */                   .addLast("decoder", (ChannelHandler)new PacketDecoder(PacketFlow.SERVERBOUND))
/*     */                   
/* 103 */                   .addLast("prepender", (ChannelHandler)new Varint21LengthFieldPrepender())
/* 104 */                   .addLast("encoder", (ChannelHandler)new PacketEncoder(PacketFlow.CLIENTBOUND));
/*     */                 
/* 106 */                 int debug2 = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
/* 107 */                 Connection debug3 = (debug2 > 0) ? (Connection)new RateKickingConnection(debug2) : new Connection(PacketFlow.SERVERBOUND);
/* 108 */                 ServerConnectionListener.this.connections.add(debug3);
/* 109 */                 debug1.pipeline().addLast("packet_handler", (ChannelHandler)debug3);
/* 110 */                 debug3.setListener((PacketListener)new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, debug3));
/*     */               }
/* 113 */             }).group((EventLoopGroup)lazyLoadedValue.get())
/* 114 */           .localAddress(debug1, debug2))
/* 115 */           .bind()
/* 116 */           .syncUninterruptibly());
/*     */     } 
/*     */   }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 150 */     this.running = false;
/*     */     
/* 152 */     for (ChannelFuture debug2 : this.channels) {
/*     */       try {
/* 154 */         debug2.channel().close().sync();
/* 155 */       } catch (InterruptedException debug3) {
/* 156 */         LOGGER.error("Interrupted whilst closing channel");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 162 */     synchronized (this.connections) {
/* 163 */       Iterator<Connection> debug2 = this.connections.iterator();
/*     */       
/* 165 */       while (debug2.hasNext()) {
/* 166 */         Connection debug3 = debug2.next();
/*     */         
/* 168 */         if (debug3.isConnecting()) {
/*     */           continue;
/*     */         }
/* 171 */         if (debug3.isConnected()) {
/*     */           try {
/* 173 */             debug3.tick();
/* 174 */           } catch (Exception debug4) {
/* 175 */             if (debug3.isMemoryConnection()) {
/* 176 */               throw new ReportedException(CrashReport.forThrowable(debug4, "Ticking memory connection"));
/*     */             }
/* 178 */             LOGGER.warn("Failed to handle packet for {}", debug3.getRemoteAddress(), debug4);
/* 179 */             TextComponent textComponent = new TextComponent("Internal server error");
/* 180 */             debug3.send((Packet)new ClientboundDisconnectPacket((Component)textComponent), debug2 -> debug0.disconnect(debug1));
/* 181 */             debug3.setReadOnly();
/*     */           } 
/*     */           continue;
/*     */         } 
/* 185 */         debug2.remove();
/* 186 */         debug3.handleDisconnection();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MinecraftServer getServer() {
/* 193 */     return this.server;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\network\ServerConnectionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */