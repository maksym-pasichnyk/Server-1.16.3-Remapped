/*     */ package io.netty.channel.sctp.nio;
/*     */ 
/*     */ import com.sun.nio.sctp.Association;
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import com.sun.nio.sctp.NotificationHandler;
/*     */ import com.sun.nio.sctp.SctpChannel;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.sctp.DefaultSctpChannelConfig;
/*     */ import io.netty.channel.sctp.SctpChannel;
/*     */ import io.netty.channel.sctp.SctpChannelConfig;
/*     */ import io.netty.channel.sctp.SctpMessage;
/*     */ import io.netty.channel.sctp.SctpNotificationHandler;
/*     */ import io.netty.channel.sctp.SctpServerChannel;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class NioSctpChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements SctpChannel
/*     */ {
/*  63 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*  65 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioSctpChannel.class);
/*     */   
/*     */   private final SctpChannelConfig config;
/*     */   
/*     */   private final NotificationHandler<?> notificationHandler;
/*     */   
/*     */   private static SctpChannel newSctpChannel() {
/*     */     try {
/*  73 */       return SctpChannel.open();
/*  74 */     } catch (IOException e) {
/*  75 */       throw new ChannelException("Failed to open a sctp channel.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSctpChannel() {
/*  83 */     this(newSctpChannel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSctpChannel(SctpChannel sctpChannel) {
/*  90 */     this((Channel)null, sctpChannel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSctpChannel(Channel parent, SctpChannel sctpChannel) {
/* 101 */     super(parent, sctpChannel, 1);
/*     */     try {
/* 103 */       sctpChannel.configureBlocking(false);
/* 104 */       this.config = (SctpChannelConfig)new NioSctpChannelConfig(this, sctpChannel);
/* 105 */       this.notificationHandler = (NotificationHandler<?>)new SctpNotificationHandler(this);
/* 106 */     } catch (IOException e) {
/*     */       try {
/* 108 */         sctpChannel.close();
/* 109 */       } catch (IOException e2) {
/* 110 */         if (logger.isWarnEnabled()) {
/* 111 */           logger.warn("Failed to close a partially initialized sctp channel.", e2);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 116 */       throw new ChannelException("Failed to enter non-blocking mode.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 122 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/* 127 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpServerChannel parent() {
/* 132 */     return (SctpServerChannel)super.parent();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelMetadata metadata() {
/* 137 */     return METADATA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Association association() {
/*     */     try {
/* 143 */       return javaChannel().association();
/* 144 */     } catch (IOException ignored) {
/* 145 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<InetSocketAddress> allLocalAddresses() {
/*     */     try {
/* 152 */       Set<SocketAddress> allLocalAddresses = javaChannel().getAllLocalAddresses();
/* 153 */       Set<InetSocketAddress> addresses = new LinkedHashSet<InetSocketAddress>(allLocalAddresses.size());
/* 154 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 155 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 157 */       return addresses;
/* 158 */     } catch (Throwable ignored) {
/* 159 */       return Collections.emptySet();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpChannelConfig config() {
/* 165 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<InetSocketAddress> allRemoteAddresses() {
/*     */     try {
/* 171 */       Set<SocketAddress> allLocalAddresses = javaChannel().getRemoteAddresses();
/* 172 */       Set<InetSocketAddress> addresses = new HashSet<InetSocketAddress>(allLocalAddresses.size());
/* 173 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 174 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 176 */       return addresses;
/* 177 */     } catch (Throwable ignored) {
/* 178 */       return Collections.emptySet();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SctpChannel javaChannel() {
/* 184 */     return (SctpChannel)super.javaChannel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 189 */     SctpChannel ch = javaChannel();
/* 190 */     return (ch.isOpen() && association() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress0() {
/*     */     try {
/* 196 */       Iterator<SocketAddress> i = javaChannel().getAllLocalAddresses().iterator();
/* 197 */       if (i.hasNext()) {
/* 198 */         return i.next();
/*     */       }
/* 200 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketAddress remoteAddress0() {
/*     */     try {
/* 209 */       Iterator<SocketAddress> i = javaChannel().getRemoteAddresses().iterator();
/* 210 */       if (i.hasNext()) {
/* 211 */         return i.next();
/*     */       }
/* 213 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 216 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 221 */     javaChannel().bind(localAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/* 226 */     if (localAddress != null) {
/* 227 */       javaChannel().bind(localAddress);
/*     */     }
/*     */     
/* 230 */     boolean success = false;
/*     */     try {
/* 232 */       boolean connected = javaChannel().connect(remoteAddress);
/* 233 */       if (!connected) {
/* 234 */         selectionKey().interestOps(8);
/*     */       }
/* 236 */       success = true;
/* 237 */       return connected;
/*     */     } finally {
/* 239 */       if (!success) {
/* 240 */         doClose();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doFinishConnect() throws Exception {
/* 247 */     if (!javaChannel().finishConnect()) {
/* 248 */       throw new Error();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 254 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 259 */     javaChannel().close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception {
/* 264 */     SctpChannel ch = javaChannel();
/*     */     
/* 266 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 267 */     ByteBuf buffer = allocHandle.allocate(config().getAllocator());
/* 268 */     boolean free = true;
/*     */     try {
/* 270 */       ByteBuffer data = buffer.internalNioBuffer(buffer.writerIndex(), buffer.writableBytes());
/* 271 */       int pos = data.position();
/*     */       
/* 273 */       MessageInfo messageInfo = ch.receive(data, (Object)null, this.notificationHandler);
/* 274 */       if (messageInfo == null) {
/* 275 */         return 0;
/*     */       }
/*     */       
/* 278 */       allocHandle.lastBytesRead(data.position() - pos);
/* 279 */       buf.add(new SctpMessage(messageInfo, buffer
/* 280 */             .writerIndex(buffer.writerIndex() + allocHandle.lastBytesRead())));
/* 281 */       free = false;
/* 282 */       return 1;
/* 283 */     } catch (Throwable cause) {
/* 284 */       PlatformDependent.throwException(cause);
/* 285 */       return -1;
/*     */     } finally {
/* 287 */       if (free) {
/* 288 */         buffer.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
/* 295 */     SctpMessage packet = (SctpMessage)msg;
/* 296 */     ByteBuf data = packet.content();
/* 297 */     int dataLen = data.readableBytes();
/* 298 */     if (dataLen == 0) {
/* 299 */       return true;
/*     */     }
/*     */     
/* 302 */     ByteBufAllocator alloc = alloc();
/* 303 */     boolean needsCopy = (data.nioBufferCount() != 1);
/* 304 */     if (!needsCopy && 
/* 305 */       !data.isDirect() && alloc.isDirectBufferPooled()) {
/* 306 */       needsCopy = true;
/*     */     }
/*     */ 
/*     */     
/* 310 */     if (needsCopy) {
/* 311 */       data = alloc.directBuffer(dataLen).writeBytes(data);
/*     */     }
/* 313 */     ByteBuffer nioData = data.nioBuffer();
/* 314 */     MessageInfo mi = MessageInfo.createOutgoing(association(), null, packet.streamIdentifier());
/* 315 */     mi.payloadProtocolID(packet.protocolIdentifier());
/* 316 */     mi.streamNumber(packet.streamIdentifier());
/* 317 */     mi.unordered(packet.isUnordered());
/*     */     
/* 319 */     int writtenBytes = javaChannel().send(nioData, mi);
/* 320 */     return (writtenBytes > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception {
/* 325 */     if (msg instanceof SctpMessage) {
/* 326 */       SctpMessage m = (SctpMessage)msg;
/* 327 */       ByteBuf buf = m.content();
/* 328 */       if (buf.isDirect() && buf.nioBufferCount() == 1) {
/* 329 */         return m;
/*     */       }
/*     */       
/* 332 */       return new SctpMessage(m.protocolIdentifier(), m.streamIdentifier(), m.isUnordered(), 
/* 333 */           newDirectBuffer((ReferenceCounted)m, buf));
/*     */     } 
/*     */     
/* 336 */     throw new UnsupportedOperationException("unsupported message type: " + 
/* 337 */         StringUtil.simpleClassName(msg) + " (expected: " + 
/* 338 */         StringUtil.simpleClassName(SctpMessage.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture bindAddress(InetAddress localAddress) {
/* 343 */     return bindAddress(localAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
/* 348 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 350 */         javaChannel().bindAddress(localAddress);
/* 351 */         promise.setSuccess();
/* 352 */       } catch (Throwable t) {
/* 353 */         promise.setFailure(t);
/*     */       } 
/*     */     } else {
/* 356 */       eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 359 */               NioSctpChannel.this.bindAddress(localAddress, promise);
/*     */             }
/*     */           });
/*     */     } 
/* 363 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture unbindAddress(InetAddress localAddress) {
/* 368 */     return unbindAddress(localAddress, newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise) {
/* 373 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 375 */         javaChannel().unbindAddress(localAddress);
/* 376 */         promise.setSuccess();
/* 377 */       } catch (Throwable t) {
/* 378 */         promise.setFailure(t);
/*     */       } 
/*     */     } else {
/* 381 */       eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 384 */               NioSctpChannel.this.unbindAddress(localAddress, promise);
/*     */             }
/*     */           });
/*     */     } 
/* 388 */     return (ChannelFuture)promise;
/*     */   }
/*     */   
/*     */   private final class NioSctpChannelConfig extends DefaultSctpChannelConfig {
/*     */     private NioSctpChannelConfig(NioSctpChannel channel, SctpChannel javaChannel) {
/* 393 */       super(channel, javaChannel);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void autoReadCleared() {
/* 398 */       NioSctpChannel.this.clearReadPending();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\sctp\nio\NioSctpChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */