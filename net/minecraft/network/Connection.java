/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.DefaultEventLoopGroup;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.channel.epoll.EpollEventLoopGroup;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Queue;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
/*     */ import net.minecraft.server.RunningOnDifferentThreadException;
/*     */ import net.minecraft.server.network.ServerGamePacketListenerImpl;
/*     */ import net.minecraft.server.network.ServerLoginPacketListenerImpl;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.LazyLoadedValue;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.MarkerManager;
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
/*     */ public class Connection
/*     */   extends SimpleChannelInboundHandler<Packet<?>>
/*     */ {
/*  57 */   private static final Logger LOGGER = LogManager.getLogger();
/*  58 */   public static final Marker ROOT_MARKER = MarkerManager.getMarker("NETWORK");
/*     */   
/*  60 */   public static final Marker PACKET_MARKER = MarkerManager.getMarker("NETWORK_PACKETS", ROOT_MARKER);
/*  61 */   public static final AttributeKey<ConnectionProtocol> ATTRIBUTE_PROTOCOL = AttributeKey.valueOf("protocol");
/*  62 */   public static final LazyLoadedValue<NioEventLoopGroup> NETWORK_WORKER_GROUP = new LazyLoadedValue(() -> new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build()));
/*     */ 
/*     */   
/*  65 */   public static final LazyLoadedValue<EpollEventLoopGroup> NETWORK_EPOLL_WORKER_GROUP = new LazyLoadedValue(() -> new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build()));
/*     */ 
/*     */   
/*  68 */   public static final LazyLoadedValue<DefaultEventLoopGroup> LOCAL_WORKER_GROUP = new LazyLoadedValue(() -> new DefaultEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build()));
/*     */ 
/*     */   
/*     */   private final PacketFlow receiving;
/*     */   
/*  73 */   private final Queue<PacketHolder> queue = Queues.newConcurrentLinkedQueue();
/*     */   private Channel channel;
/*     */   private SocketAddress address;
/*     */   private PacketListener packetListener;
/*     */   private Component disconnectedReason;
/*     */   private boolean encrypted;
/*     */   private boolean disconnectionHandled;
/*     */   private int receivedPackets;
/*     */   private int sentPackets;
/*     */   private float averageReceivedPackets;
/*     */   private float averageSentPackets;
/*     */   private int tickCount;
/*     */   private boolean handlingFault;
/*     */   
/*     */   public Connection(PacketFlow debug1) {
/*  88 */     this.receiving = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelActive(ChannelHandlerContext debug1) throws Exception {
/*  93 */     super.channelActive(debug1);
/*  94 */     this.channel = debug1.channel();
/*  95 */     this.address = this.channel.remoteAddress();
/*     */     try {
/*  97 */       setProtocol(ConnectionProtocol.HANDSHAKING);
/*  98 */     } catch (Throwable debug2) {
/*  99 */       LOGGER.fatal(debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setProtocol(ConnectionProtocol debug1) {
/* 104 */     this.channel.attr(ATTRIBUTE_PROTOCOL).set(debug1);
/* 105 */     this.channel.config().setAutoRead(true);
/* 106 */     LOGGER.debug("Enabled auto read");
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext debug1) throws Exception {
/* 111 */     disconnect((Component)new TranslatableComponent("disconnect.endOfStream"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext debug1, Throwable debug2) {
/* 116 */     if (debug2 instanceof SkipPacketException) {
/* 117 */       LOGGER.debug("Skipping packet due to errors", debug2.getCause());
/*     */       
/*     */       return;
/*     */     } 
/* 121 */     boolean debug3 = !this.handlingFault;
/* 122 */     this.handlingFault = true;
/*     */     
/* 124 */     if (!this.channel.isOpen()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     if (debug2 instanceof io.netty.handler.timeout.TimeoutException) {
/* 129 */       LOGGER.debug("Timeout", debug2);
/* 130 */       disconnect((Component)new TranslatableComponent("disconnect.timeout"));
/*     */     } else {
/* 132 */       TranslatableComponent translatableComponent = new TranslatableComponent("disconnect.genericReason", new Object[] { "Internal Exception: " + debug2 });
/*     */       
/* 134 */       if (debug3) {
/* 135 */         LOGGER.debug("Failed to sent packet", debug2);
/* 136 */         send((Packet<?>)new ClientboundDisconnectPacket((Component)translatableComponent), debug2 -> disconnect(debug1));
/* 137 */         setReadOnly();
/*     */       } else {
/* 139 */         LOGGER.debug("Double fault", debug2);
/* 140 */         disconnect((Component)translatableComponent);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void channelRead0(ChannelHandlerContext debug1, Packet<?> debug2) throws Exception {
/* 147 */     if (this.channel.isOpen()) {
/*     */       try {
/* 149 */         genericsFtw(debug2, this.packetListener);
/* 150 */       } catch (RunningOnDifferentThreadException runningOnDifferentThreadException) {}
/*     */       
/* 152 */       this.receivedPackets++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends PacketListener> void genericsFtw(Packet<T> debug0, PacketListener debug1) {
/* 158 */     debug0.handle(debug1);
/*     */   }
/*     */   
/*     */   public void setListener(PacketListener debug1) {
/* 162 */     Validate.notNull(debug1, "packetListener", new Object[0]);
/* 163 */     this.packetListener = debug1;
/*     */   }
/*     */   
/*     */   public void send(Packet<?> debug1) {
/* 167 */     send(debug1, null);
/*     */   }
/*     */   
/*     */   public void send(Packet<?> debug1, @Nullable GenericFutureListener<? extends Future<? super Void>> debug2) {
/* 171 */     if (isConnected()) {
/* 172 */       flushQueue();
/* 173 */       sendPacket(debug1, debug2);
/*     */     } else {
/* 175 */       this.queue.add(new PacketHolder(debug1, debug2));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendPacket(Packet<?> debug1, @Nullable GenericFutureListener<? extends Future<? super Void>> debug2) {
/* 180 */     ConnectionProtocol debug3 = ConnectionProtocol.getProtocolForPacket(debug1);
/* 181 */     ConnectionProtocol debug4 = (ConnectionProtocol)this.channel.attr(ATTRIBUTE_PROTOCOL).get();
/* 182 */     this.sentPackets++;
/*     */     
/* 184 */     if (debug4 != debug3) {
/* 185 */       LOGGER.debug("Disabled auto read");
/* 186 */       this.channel.config().setAutoRead(false);
/*     */     } 
/*     */     
/* 189 */     if (this.channel.eventLoop().inEventLoop()) {
/* 190 */       if (debug3 != debug4) {
/* 191 */         setProtocol(debug3);
/*     */       }
/* 193 */       ChannelFuture debug5 = this.channel.writeAndFlush(debug1);
/* 194 */       if (debug2 != null) {
/* 195 */         debug5.addListener(debug2);
/*     */       }
/* 197 */       debug5.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
/*     */     } else {
/* 199 */       this.channel.eventLoop().execute(() -> {
/*     */             if (debug1 != debug2) {
/*     */               setProtocol(debug1);
/*     */             }
/*     */             ChannelFuture debug5 = this.channel.writeAndFlush(debug3);
/*     */             if (debug4 != null) {
/*     */               debug5.addListener(debug4);
/*     */             }
/*     */             debug5.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void flushQueue() {
/* 213 */     if (this.channel == null || !this.channel.isOpen()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 218 */     synchronized (this.queue) {
/*     */       PacketHolder debug2;
/* 220 */       while ((debug2 = this.queue.poll()) != null) {
/* 221 */         sendPacket(debug2.packet, debug2.listener);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 227 */     flushQueue();
/*     */     
/* 229 */     if (this.packetListener instanceof ServerLoginPacketListenerImpl) {
/* 230 */       ((ServerLoginPacketListenerImpl)this.packetListener).tick();
/*     */     }
/*     */     
/* 233 */     if (this.packetListener instanceof ServerGamePacketListenerImpl) {
/* 234 */       ((ServerGamePacketListenerImpl)this.packetListener).tick();
/*     */     }
/*     */     
/* 237 */     if (this.channel != null) {
/* 238 */       this.channel.flush();
/*     */     }
/*     */     
/* 241 */     if (this.tickCount++ % 20 == 0) {
/* 242 */       tickSecond();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void tickSecond() {
/* 247 */     this.averageSentPackets = Mth.lerp(0.75F, this.sentPackets, this.averageSentPackets);
/* 248 */     this.averageReceivedPackets = Mth.lerp(0.75F, this.receivedPackets, this.averageReceivedPackets);
/* 249 */     this.sentPackets = 0;
/* 250 */     this.receivedPackets = 0;
/*     */   }
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 254 */     return this.address;
/*     */   }
/*     */   
/*     */   public void disconnect(Component debug1) {
/* 258 */     if (this.channel.isOpen()) {
/* 259 */       this.channel.close().awaitUninterruptibly();
/*     */       
/* 261 */       this.disconnectedReason = debug1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMemoryConnection() {
/* 266 */     return (this.channel instanceof io.netty.channel.local.LocalChannel || this.channel instanceof io.netty.channel.local.LocalServerChannel);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryptionKey(SecretKey debug1) {
/* 332 */     this.encrypted = true;
/* 333 */     this.channel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler)new CipherDecoder(Crypt.getCipher(2, debug1)));
/* 334 */     this.channel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler)new CipherEncoder(Crypt.getCipher(1, debug1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/* 342 */     return (this.channel != null && this.channel.isOpen());
/*     */   }
/*     */   
/*     */   public boolean isConnecting() {
/* 346 */     return (this.channel == null);
/*     */   }
/*     */   
/*     */   public PacketListener getPacketListener() {
/* 350 */     return this.packetListener;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Component getDisconnectedReason() {
/* 355 */     return this.disconnectedReason;
/*     */   }
/*     */   
/*     */   public void setReadOnly() {
/* 359 */     this.channel.config().setAutoRead(false);
/*     */   }
/*     */   
/*     */   public void setupCompression(int debug1) {
/* 363 */     if (debug1 >= 0) {
/* 364 */       if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder) {
/* 365 */         ((CompressionDecoder)this.channel.pipeline().get("decompress")).setThreshold(debug1);
/*     */       } else {
/* 367 */         this.channel.pipeline().addBefore("decoder", "decompress", (ChannelHandler)new CompressionDecoder(debug1));
/*     */       } 
/*     */       
/* 370 */       if (this.channel.pipeline().get("compress") instanceof CompressionEncoder) {
/* 371 */         ((CompressionEncoder)this.channel.pipeline().get("compress")).setThreshold(debug1);
/*     */       } else {
/* 373 */         this.channel.pipeline().addBefore("encoder", "compress", (ChannelHandler)new CompressionEncoder(debug1));
/*     */       } 
/*     */     } else {
/* 376 */       if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder) {
/* 377 */         this.channel.pipeline().remove("decompress");
/*     */       }
/*     */       
/* 380 */       if (this.channel.pipeline().get("compress") instanceof CompressionEncoder) {
/* 381 */         this.channel.pipeline().remove("compress");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleDisconnection() {
/* 387 */     if (this.channel == null || this.channel.isOpen()) {
/*     */       return;
/*     */     }
/*     */     
/* 391 */     if (this.disconnectionHandled) {
/* 392 */       LOGGER.warn("handleDisconnection() called twice");
/*     */     } else {
/* 394 */       this.disconnectionHandled = true;
/* 395 */       if (getDisconnectedReason() != null) {
/* 396 */         getPacketListener().onDisconnect(getDisconnectedReason());
/* 397 */       } else if (getPacketListener() != null) {
/* 398 */         getPacketListener().onDisconnect((Component)new TranslatableComponent("multiplayer.disconnect.generic"));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAverageReceivedPackets() {
/* 404 */     return this.averageReceivedPackets;
/*     */   }
/*     */ 
/*     */   
/*     */   static class PacketHolder
/*     */   {
/*     */     private final Packet<?> packet;
/*     */     
/*     */     @Nullable
/*     */     private final GenericFutureListener<? extends Future<? super Void>> listener;
/*     */ 
/*     */     
/*     */     public PacketHolder(Packet<?> debug1, @Nullable GenericFutureListener<? extends Future<? super Void>> debug2) {
/* 417 */       this.packet = debug1;
/* 418 */       this.listener = debug2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */